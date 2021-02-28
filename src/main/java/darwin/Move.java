package darwin;

/**
 * Class representing a move in a 32-bit value
 * 
 * Bytes from high to low
 * byte 0: promotion piece
 * byte 1: capture piece
 * byte 2: source square
 * byte 3: target square
 */
public class Move {

	public final int value;
	
	public Move(int move) {
		this.value=move;
	}
	
	public static final int PROMOTE_MASK=0xFF000000;
	public static final int CAPTURE_MASK=0xFF0000;
	public static final int SOURCE_MASK=0x8800;
	public static final int TARGET_MASK=0x88;
	
	public byte source() {
		return source (value);
	}
	
	public static byte source(int move) {
		return (byte)((move & SOURCE_MASK)>>8);
	}
	
	public byte target() {
		return source (value);
	}
	
	public static byte target(int move) {
		return (byte)((move & TARGET_MASK));
	}
	
	public boolean isCapture() {
		return isCapture(value);
	}
	
	public boolean isCapture(int move) {
		return (move & CAPTURE_MASK)!=0;
	}
	
	public boolean isPromote() {
		return isPromote(value);
	}
	
	public boolean isPromote(int move) {
		return (move & PROMOTE_MASK)!=0;
	}
	
	public byte capturePiece() {
		return capturePiece(value);
	}
	
	public static byte capturePiece(int move) {
		return (byte)((move & CAPTURE_MASK)>>16);
	}
	
	public byte promotePiece() {
		return promotePiece(value);
	}
	
	public static byte promotePiece(int move) {
		return (byte)((move & CAPTURE_MASK)>>16);
	}
}
