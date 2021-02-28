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
		genPawnAdvances();
		genPawnCaptures();
	}

	private void genPawnCaptures() {
		// TODO Auto-generated method stub
		
	}

	private void genPawnAdvances() {
		boolean whiteMove=this.whiteMove;
		long pawns=whiteMove?bb.wp:bb.bp;
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
	
	
}
