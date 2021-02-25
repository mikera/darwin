package darwin;

public class Piece {

	public static final byte K=6;
	public static final byte Q=5;
	public static final byte R=4;
	public static final byte B=3;
	public static final byte N=2;
	public static final byte P=1;
	
	public static final int PIECE_MASK=0xF;
	public static final int COLOUR_MASK=0x30;
	
	public static final byte WHITE=16;
	public static final byte BLACK=32;
	
	public static final byte WK=(byte) (WHITE+K);
	public static final byte WQ=(byte) (WHITE+Q);
	public static final byte WR=(byte) (WHITE+R);
	public static final byte WB=(byte) (WHITE+B);
	public static final byte WN=(byte) (WHITE+N);
	public static final byte WP=(byte) (WHITE+P);
	
	public static final byte BK=(byte) (BLACK+K);
	public static final byte BQ=(byte) (BLACK+Q);
	public static final byte BR=(byte) (BLACK+R);
	public static final byte BB=(byte) (BLACK+B);
	public static final byte BN=(byte) (BLACK+N);
	public static final byte BP=(byte) (BLACK+P);
	
	public static char toFEN(byte piece) {
		int p=piece&PIECE_MASK;
		boolean white=(piece&COLOUR_MASK)==WHITE;
		return (white?"-PNBRQK":"-pnbrqk").charAt(p);
	}


}
