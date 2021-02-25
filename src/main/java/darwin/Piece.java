package darwin;

public class Piece {

	public static byte K=6;
	public static byte Q=5;
	public static byte R=4;
	public static byte B=3;
	public static byte N=2;
	public static byte P=1;
	
	public static int PIECE_MASK=0xF;
	public static int COLOUR_MASK=0x30;
	
	public static byte WHITE=16;
	public static byte BLACK=32;
	
	public static byte WK=(byte) (WHITE+K);
	public static byte WQ=(byte) (WHITE+Q);
	public static byte WR=(byte) (WHITE+R);
	public static byte WB=(byte) (WHITE+B);
	public static byte WN=(byte) (WHITE+N);
	public static byte WP=(byte) (WHITE+P);
	
	public static byte BK=(byte) (BLACK+K);
	public static byte BQ=(byte) (BLACK+Q);
	public static byte BR=(byte) (BLACK+R);
	public static byte BB=(byte) (BLACK+B);
	public static byte BN=(byte) (BLACK+N);
	public static byte BP=(byte) (BLACK+P);
	
	public static char toFEN(byte piece) {
		int p=piece&PIECE_MASK;
		boolean white=(piece&COLOUR_MASK)==WHITE;
		return (white?"-PNBRQK":"-pnbrqk").charAt(p);
	}


}
