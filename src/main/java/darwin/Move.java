package darwin;

/**
 * Class representing a move
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
}
