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
		whites=bb.wk|bb.wq|bb.wr|bb.wb|bb.wn|bb.wp;
		blacks=bb.bk|bb.bq|bb.br|bb.bb|bb.bn|bb.bp;
	}
	
	private void computeChecks() {
		long checks=0L;
		boolean whiteMove=this.whiteMove;
		long king=whiteMove?bb.wk:bb.bk;
		
		int ix=Util.boardIndex(king);
		
		// pawn checks
		long possiblePawnChecks=whiteMove?MoveTables.PCAPSW[ix]:MoveTables.PCAPSB[ix];
		checks|=possiblePawnChecks&(whiteMove?bb.bp:bb.wp);

		// knight checks
		checks|=(whiteMove?bb.bn:bb.wn)&MoveTables.NTARGETS[ix];
		
		// ray checks
		for (int ray=0; ray<8; ray++) {
			long raymask=MoveTables.RAYTARGETS[ix*8+ray];
			long enemy = (whiteMove?blacks:whites)&raymask;
			if (enemy==0L) continue;
			boolean upRight=MoveTables.RAYSHIFTS[ray]>0; // direction of bits
			
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
		// TODO
	}



	
	public void genPawnMoves() {
		long pawns=whiteMove?bb.wp:bb.bp;
		genPawnAdvances(pawns);
		genPawnCaptures(pawns);
	}

	private void genPawnCaptures(long pawns) {
		long targets=(whiteMove?blacks:whites)|bb.enPassantTarget;
		long right=(pawns&0x00FEFEFEFEFEFE00L)&(whiteMove?(targets>>9):(targets<<7));
		long lefft=(pawns&0x007F7F7F7F7F7F00L)&(whiteMove?(targets>>7):(targets<<9));
		
		for (long bit=Long.highestOneBit(right); bit!=0; right&=(~bit)) {
			byte pos=Util.pos(bit);
			addPawnCaptures(pos,(byte)(pos+(whiteMove?17:-15)));
		}
		
		for (long bit=Long.highestOneBit(lefft); bit!=0; lefft&=(~bit)) {
			byte pos=Util.pos(bit);
			addPawnCaptures(pos,(byte)(pos+(whiteMove?17:-15)));
		}
	}

	private void addPawnCaptures(byte source, byte target) {
		byte targetPiece=bb.getPiece(target);
		
		// TODO: promotion
		// int targetRank=Util.rank(target);
		// boolean promote= (targetRank==(whiteMove?7:0));
		
		int move=(targetPiece<<16)|(source<<8)|target;
		addMove(move);	
	}

	private void genPawnAdvances(long pawns) {
		boolean whiteMove=this.whiteMove;
		long blocks=whites|blacks;
		long push1=pawns&~(whiteMove?(blocks>>8):(blocks<<8));
		long push2=push1&(whiteMove?0x0000000000FF00L:0x00FF000000000000L)&~(whiteMove?(blocks>>16):(blocks<<16));
		
		for (long bit=Long.highestOneBit(push1); bit!=0; push1&=(~bit)) {
			byte pos=Util.pos(bit);
			int rank=Util.rank(pos);
			if (rank==(whiteMove?6:1)) {
				// TODO promotion moves
			} else {
				addPawnAdvance(pos,(byte)(pos+(whiteMove?16:-16)));
			}
		}
		
		for (long bit=Long.highestOneBit(push2); bit!=0; push2&=(~bit)) {
			byte pos=Util.pos(bit);
			int rank=Util.rank(pos);
			if (rank==(whiteMove?6:1)) {
				// TODO promotion moves
			} else {
				addPawnAdvance(pos,(byte)(pos+(whiteMove?32:-32)));
			}
		}
		
	}

	private void addPawnAdvance(byte source, byte target) {
		int move=(source<<8)|target;
		addMove(move);
	}

	public int getMove(int i) {
		return moves[i];
	}
	
	
}
