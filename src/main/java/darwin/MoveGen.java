package darwin;

/**
 * Move generator
 */
public class MoveGen {
	BitBoard bb=null;
	long whites=0L; // all white pieces
	long blacks=0L; // all black peices
	boolean whiteMove=true;

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
	
	public void gen(BitBoard bb) {
		setup(bb);
		genPawnMoves();
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
