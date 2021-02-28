package darwin;

public class MoveTables {
	/**
	 * Move targets for knights, indexed by bit position
	 */
	public static final long[] NTARGETS=new long[64];
	public static int[][] NSHIFTS=new int[][] {{-2,-1},{-2,1},{-1,-2},{-1,2},{2,-1},{2,1},{1,-2},{1,2}};
	
	/**
	 * Move targets for kings, indexed by bit position
	 */
	public static final long[] KTARGETS=new long[64];
	public static int[][] KSHIFTS=new int[][] {{1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1}};
	
	/**
	 * Capture targets for pawns, indexed by bit position
	 */
	public static final long[] PCAPSW=new long[64];
	public static final long[] PCAPSB=new long[64];

	
	/**
	 * Move targets for sliding pieces indexed by bit position * ray direction
	 * Organised in "rays" from 12 O'Clock going clockwise
	 */
	public static final long[] RAYTARGETS=new long[64*8];
	
	/**
	 * Capture targets for Queens, indexed by bit position
	 */
	public static final long[] QTARGETS=new long[64];
	
	/**
	 * Capture targets for Rooks, indexed by bit position
	 */
	public static final long[] RTARGETS=new long[64];

	/**
	 * Capture targets for Bishops, indexed by bit position
	 */
	public static final long[] BTARGETS=new long[64];

	
	static {
		for (int rank=0; rank<=7; rank++) {
			for (int file=0; file<=7; file++) {
				int ix=Util.pos(rank,file);
				
				// Knight moves
				for (int i=0; i<8; i++) {
					int[] shift=NSHIFTS[i];
					int trank=rank+shift[0];
					int tfile=file+shift[1];
					if (Util.validPos(trank,tfile)) {
						NTARGETS[ix]|=Util.bit(trank,tfile);
					}
				}
				
				// King moves
				for (int i=0; i<8; i++) {
					int[] shift=KSHIFTS[i];
					int trank=rank+shift[0];
					int tfile=file+shift[1];
					if (Util.validPos(trank,tfile)) {
						KTARGETS[ix]|=Util.bit(trank,tfile);
					}
				}
				
				// Pawn captures left
				if (file>0) {
					if (rank<7) PCAPSW[ix]|=Util.bit(rank+1, file-1);
					if (rank>0) PCAPSB[ix]|=Util.bit(rank-1, file-1);
				}
				
				// Pawn captures right
				if (file>0) {
					if (rank<7) PCAPSW[ix]|=Util.bit(rank+1, file+1);
					if (rank>0) PCAPSB[ix]|=Util.bit(rank-1, file+1);
				}
				
				// Ray moves
				for (int ray=0; ray<=7; ray++) {
					int[] shift=KSHIFTS[ray];
					long targets=0L;
					
					// loop over distances until we hit invalid position
					for (int d=1; d<=7; d++) {
						int trank=rank+shift[0]*d;
						int tfile=file+shift[1]*d;
						if (Util.validPos(trank,tfile)) {
							targets|=Util.bit(trank,tfile);
						} else {
							break; // ray reached edge of board
						}
					}
					
					RAYTARGETS[ix*8+ray]=targets;
					QTARGETS[ix]|=targets;
					if ((ray&1)==0) {
						RTARGETS[ix]|=targets;
					} else {
						BTARGETS[ix]|=targets;	
					}
				}
			}
		}
	}
}
