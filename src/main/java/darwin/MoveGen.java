package darwin;

/**
 * Move generator
 */
public class MoveGen {
	BitBoard bb=null;
	/**
	 * Bitmap for all white pieces
	 */
	long whites=0L; 
	
	/**
	 * Bitmap for all black pieces
	 */
	long blacks=0L; 
	
	boolean whiteMove=true;
	
	/**
	 * Bitmap for pieces delivering check
	 */
	long checks = 0L;
	
	/**
	 * Bitmap for enemy pieces that are pinning
	 */
	long pinners = 0L;
	
	/**
	 * Bitmap for own pieces that are pinned
	 */
	long pinned = 0L;



	public int count=0;
	public int[] moves = new int [40];
	
	public void addMove(int move) {
		ensureCapacity(count+1);
		moves[count++]=move;
	}

	private void ensureCapacity(int capacity) {
		int cc=moves.length;
		if (cc>=capacity) return;
		int[] newMoves=new int[cc*2];
		System.arraycopy(moves, 0, newMoves, 0, count);
		moves=newMoves;
	}
	
	public void setup(BitBoard bb) {
		this.bb=bb;
		this.whiteMove=bb.whiteMove;
		whites=bb.white();
		blacks=bb.black();
	}
	
	private void computeChecks() {
		long checks=0L;
		boolean whiteMove=this.whiteMove;
		long king=bb.k(whiteMove);
		
		int ix=Util.boardIndex(king);
		
		// pawn checks
		long possiblePawnChecks=whiteMove?MoveTables.PCAPSW[ix]:MoveTables.PCAPSB[ix];
		checks|=possiblePawnChecks&(bb.p(!whiteMove));

		// knight checks
		checks|=(whiteMove?bb.bn:bb.wn)&MoveTables.NTARGETS[ix];
		
		// ray checks
		for (int ray=0; ray<8; ray++) {
			long raymask=MoveTables.RAYTARGETS[ix*8+ray];
			long enemy = (whiteMove?blacks:whites)&raymask;
			if (enemy==0L) continue;
			boolean upRight=(ray<=2)||(ray==7); // direction of bits
			
			// keep enemy piece closest to king
			enemy=upRight?Long.lowestOneBit(enemy):Long.highestOneBit(enemy);
			
			// check if enemy piece is capable of delivering a ray check
			byte enemyPiece=bb.getPiece(enemy);
			switch (enemyPiece&Piece.PIECE_MASK) {
				case Piece.Q: break;
				case Piece.R: if ((ray&1)==0) break; else continue;
				case Piece.B: if ((ray&1)==1) break; else continue;
				default: continue;
			}
			
			// check for blocking friendly piece
			long friend = (whiteMove?whites:blacks)&raymask;
			if (friend!=0) {
				int enemyIndex=Util.boardIndex(enemy);
				int enemyRay=(ray+4)&0x7;
				long enemyAttack=MoveTables.RAYTARGETS[enemyIndex*8+enemyRay];
				friend&=enemyAttack;
				if (friend!=0) {
					if (Long.bitCount(friend)==1) {
						// we have a pinned piece
						this.pinners|=enemy;
						this.pinned|=friend;
					}
					continue; // blocking piece
				}
			}
		}

		
		this.checks= checks;
	}

	public void gen(BitBoard bb) {
		setup(bb);
		computeChecks();
		
		if (checks!=0) {
			// need to bail out if more than one check
			int checkCount=Long.bitCount(checks);
			if (checkCount>1) {
				genKingMoves();
				return;
			}
		}
		
		genKingMoves();
		genQueenMoves();
		genRookMoves();
		genBishopMoves();
		genKnightMoves();
		genPawnMoves();
	}
	
	public void genKingMoves() {
		// TODO
	}
	
	public void genQueenMoves() {
		// TODO
	}
	
	public void genRookMoves() {
		// TODO
	}
	
	public void genBishopMoves() {
		// TODO
	}
	
	public void genKnightMoves() {
		long knights=whiteMove?bb.wn:bb.bn;
		while(knights!=0) {
			long knight=Util.topBit(knights);
					
			// only possible to move non-pinned knights
			if ((knight&pinned)==0) {
				int ix=Util.boardIndex(knight);
				long possible=MoveTables.NTARGETS[ix]&~(whiteMove?whites:blacks);
				
				while(possible!=0L) {
					long tbit=Util.topBit(possible);
					addMoveNotPinned(knight,tbit);
					possible&=~tbit;
				}
			}
			knights&=~knight;
		}
	}

	public void genPawnMoves() {
		long pawns=bb.p(whiteMove);
		genPawnAdvances(pawns);
		genPawnCaptures(pawns);
	}

	private void genPawnCaptures(long pawns) {
		long targets=(whiteMove?blacks:whites)|bb.enPassantTarget;
		
		long right=(pawns&0x007F7F7F7F7F7F00L)&(whiteMove?(targets>>9):(targets<<7));
		
		long lefft=(pawns&0x00FEFEFEFEFEFE00L)&(whiteMove?(targets>>7):(targets<<9));
		
		while(right!=0) {
			long pawn=Util.topBit(right);
			addPawnCapture(pawn,whiteMove?(pawn<<9):(pawn>>7));
			right&=~pawn;
		}
		
		while(lefft!=0) {
			long pawn=Util.topBit(lefft);
			addPawnCapture(pawn,whiteMove?(pawn<<7):(pawn>>9));
			lefft&=~pawn;
		}
	}

	private void addPawnCapture(long source, long target) {
		byte targetPiece=bb.getPiece(target);
		// TODO: captured piece for en passant
		
		// TODO: promotion
		// int targetRank=Util.rank(target);
		// boolean promote= (targetRank==(whiteMove?7:0));
		
		byte spos=Util.pos(source);
		byte tpos=Util.pos(target);
		int move=(targetPiece<<16)|(spos<<8)|tpos;
		addMove(move);	
	}
	
	/**
	 * Add a move, checking for pins
	 * @param sourcePos
	 * @param targetBit
	 */
	@SuppressWarnings("unused")
	private void addMove(long sourceBit, long targetBit) {
		if (pinPrevents(sourceBit,targetBit)) return;
		addMoveNotPinned(sourceBit,targetBit);
	}
	
	/**
	 * Check if a pin is preventing a piece move
	 * @param sourceBit
	 * @param targetBit
	 * @return
	 */
	private boolean pinPrevents(long sourceBit, long targetBit) {
		if ((sourceBit&pinned)!=0) {
			// need to check destination square is on same ray as king
			long king=bb.k(whiteMove);
			byte kingPos=Util.pos(king);
			for (int ray=0; ray<=7; ray++) {
				long kingRay=MoveTables.RAYTARGETS[kingPos*8+ray];
				if ((kingRay&sourceBit)!=0L) {
					if ((kingRay&targetBit)==0L) return true;
					return false;
				}
			}
			throw new Error("Piece on "+Util.square(sourceBit)+" marked as pinned but not on ray from king at "+Util.square(kingPos));
		}
		return false;
	}
		
	/**
	 * Add a move, without checking for pins
	 * @param sourcePos
	 * @param targetBit
	 */
	private void addMoveNotPinned(long sourceBit, long targetBit) {
		byte sourcePos=Util.pos(sourceBit);
		byte targetPos=Util.pos(targetBit);
		long captureBit=targetBit&(whiteMove?blacks:whites);
		if (captureBit!=0) {
			byte targetPiece=bb.getPiece(targetBit); 
			int move=(targetPiece<<16)|(sourcePos<<8)|targetPos;
			addMove(move);	
		} else {
			int move=(sourcePos<<8)|targetPos;
			addMove(move);	
		}
	}

	private void genPawnAdvances(long pawns) {
		boolean whiteMove=this.whiteMove;
		long blocks=whites|blacks;
		long push1=pawns&~(whiteMove?(blocks>>8):(blocks<<8));
		long push2=push1&(whiteMove?0x0000000000FF00L:0x00FF000000000000L)&~(whiteMove?(blocks>>16):(blocks<<16));
		
		while (push1!=0) {
			long pawn=Util.topBit(push1);
			addPawnAdvance(pawn,whiteMove?(pawn<<8):(pawn>>8));
			push1&=~pawn;
		}
		
		while(push2!=0) {
			long pawn=Util.topBit(push2);
			addPawnAdvance(pawn,whiteMove?(pawn<<16):(pawn>>16));
			push2&=~pawn;
		}
		
	}

	private void addPawnAdvance(long source, long target) {
		if (pinPrevents(source,target)) return;
		// TODO: promotion moves
		addMoveNotPinned(source,target);
	}

	public int getMove(int i) {
		return moves[i];
	}
	
	
}
