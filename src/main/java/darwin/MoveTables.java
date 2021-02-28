package darwin;

public class MoveTables {
	/**
	 * Move targets for knights, indexed by bit position
	 */
	public static final long[] NTARGETS=new long[64];
	public static int[] NSHIFTS=new int[] {-33,-31,-18,-14,14,18,31,33 };
	
	/**
	 * Move targets for kings, indexed by bit position
	 */
	public static final long[] KTARGETS=new long[64];
	public static int[] KSHIFTS=new int[] {-17, -16, -15, -1,1,15,16,17 };
	
	/**
	 * Move targets for sliding pieces indexed by ray direction * bit position
	 * Organised in "rays" from 12 O'Clock going clockwise
	 */
	public static final long[] RAYTARGETS=new long[8*64];
	public static int[] RAYSHIFTS=new int[] {16, 17, 1, -15,-16,-17,-1,15 };
	
	
	static {
		for (int rank=0; rank<=7; rank++) {
			for (int file=0; file<=7; file++) {
				int ix=file+rank*8;
				byte pos=Util.pos(rank,file);
				
				// Knight moves
				for (int i=0; i<8; i++) {
					int shift=NSHIFTS[i];
					int tpos=pos+shift;
					if ((tpos&0x88)==tpos) {
						NTARGETS[ix]|=Util.bit((byte)tpos);
					}
				}
				
				// King moves
				for (int i=0; i<8; i++) {
					int shift=KSHIFTS[i];
					int tpos=pos+shift;
					if ((tpos&0x88)==tpos) {
						KTARGETS[ix]|=Util.bit((byte)tpos);
					}
				}
				
				// Ray moves
				for (int ray=0; ray<=7; ray++) {
					int shift=RAYSHIFTS[ray];
					int tpos=0;
					for (int d=0; d<8; d++) {
						tpos+=shift;
						if ((tpos&0x88)==tpos) {
							RAYTARGETS[ix+ray*64]|=Util.bit((byte)tpos);
						} else {
							break; // ray reached edge of board
						}
					}
				}
			}
		}
	}
}
