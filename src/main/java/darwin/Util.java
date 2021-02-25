package darwin;

public class Util {

	/**
	 * Get bit representation of a position.
	 * @param pos
	 * @return Long with single bit set
	 */
	public static long bit(byte pos) {
		int file=pos&0x7;
		int rank=(pos>>4)&0x7;
		return 1L<<(rank*8+file);
	}
	
	/**
	 * Gets a position byte from a String in format "e6"
	 * @param s
	 * @return Position byte
	 */
	public static byte pos(String square) {
		int file=square.charAt(0)-'a';
		int rank=square.charAt(1)-'1';
		return (byte) (file+rank*16);
	}
	
	/**
	 * Get bit representation of a position expressed as a String e.g. "d7".
	 * @param pos
	 * @return Long with single bit set
	 */
	public static long bit(String square) {
		return bit(pos(square));
	}
	
	/**
	 * Gets a position byte from a rank and file
	 */
	public static byte pos(int rank, int file) {
		return (byte) (file+rank*16);
	}
	
	/**
	 * Gets a position byte from a single bit
	 */
	public static byte pos(long bit) {
		byte p=0;
		if ((bit&0xFFFFFFFF00000000L)!=0) p+=64;
		if ((bit&0xFFFF0000FFFF0000L)!=0) p+=32;
		if ((bit&0xFF00FF00FF00FF00L)!=0) p+=16;
		if ((bit&0xF0F0F0F0F0F0F0F0L)!=0) p+=4;
		if ((bit&0xCCCCCCCCCCCCCCCCL)!=0) p+=2;
		if ((bit&0xAAAAAAAAAAAAAAAAL)!=0) p+=1;
		return p;
	}

	/**
	 * Gets the string representing a square in "c4" format
	 * @param pos Byte representing board position
	 * @return String representation of sqaure
	 */
	public static String square(byte pos) {
		int file=pos&0x7;
		int rank=(pos>>4)&0x7;

		StringBuilder sb=new StringBuilder(2);
		sb.append("abcdefgh".charAt(file));
		sb.append("12345678".charAt(rank));
		return sb.toString();
	}
	
}
