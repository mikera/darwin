package darwin;

public class Util {

	/**
	 * Get bit representation of a position.
	 * @param pos
	 * @return Long with single bit set
	 */
	public static long bit(byte pos) {
		int file=pos&0x7;
		int rank=(pos>>3)&0x7;
		return 1L<<(rank*8+file);
	}
	
	/**
	 * Get rank co-ordinate from a position byte
	 * @param pos
	 * @return Rank in format 0..7
	 */
	public static int rank(byte pos) {
		int rank=(pos>>3)&0x7;
		return rank;
	}
	
	/**
	 * Get file co-ordinate from a position byte 
	 * @param pos
	 * @return File in format 0..7
	 */
	public static int file(byte pos) {
		int file=pos&0x7;
		return file;
	}
	
	/**
	 * Get bit representation of rank and file co-ordinates (expressed as 0..7)
	 * @param rank Rank of square
	 * @param file File of square
	 * @return Long with single bit set
	 */
	public static long bit(int rank, int file) {
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
		return pos(rank,file);
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
		return (byte) (file+rank*8);
	}
	
	/**
	 * Gets a position byte from a single bit
	 */
	public static byte pos(long bit) {
		byte p=0;
		if ((bit&0xFFFFFFFF00000000L)!=0) p+=32;
		if ((bit&0xFFFF0000FFFF0000L)!=0) p+=16;
		if ((bit&0xFF00FF00FF00FF00L)!=0) p+=8;
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
		int rank=(pos>>3)&0x7;

		StringBuilder sb=new StringBuilder(2);
		sb.append("abcdefgh".charAt(file));
		sb.append("12345678".charAt(rank));
		return sb.toString();
	}

	/**
	 * Gets the square representing by a bit string
	 * @param bit Long value with single bit set
	 * @return String representation of sqaure
	 */
	public static String square(long bit) {
		return square(pos(bit));
	}

	public static boolean validPos(int rank, int file) {
		return ((rank&0x7)==rank)&&((file&0x7)==file);
	}

	/**
	 * Gets the board index for the highest bit in the given long. -1 if there is no bit set.
	 * @param bit
	 * @return
	 */
	public static int boardIndex(long bit) {
		return 63-Long.numberOfLeadingZeros(bit);
	}

	public static int boardIndex(int rank, int file) {
		return (rank*8)+file;
	}

	public static long topBit(long bits) {
		if (bits==0L) return 0L;
		return Long.highestOneBit(bits);
	}


	
}
