package darwin;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Images {

	static final BufferedImage WK = loadPieceImage("wk");
	static final BufferedImage WQ = loadPieceImage("wq");
	static final BufferedImage WR = loadPieceImage("wr");
	static final BufferedImage WB = loadPieceImage("wb");
	static final BufferedImage WN = loadPieceImage("wn");
	static final BufferedImage WP = loadPieceImage("wp");
	static final BufferedImage BK = loadPieceImage("bk");
	static final BufferedImage BQ = loadPieceImage("bq");
	static final BufferedImage BR = loadPieceImage("br");
	static final BufferedImage BB = loadPieceImage("bb");
	static final BufferedImage BN = loadPieceImage("bn");
	static final BufferedImage BP = loadPieceImage("bp");

	private static BufferedImage loadPieceImage(String name) {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(name + ".png");
			BufferedImage bi = ImageIO.read(is);

			// Copy over into a new BufferedImage of desired type. Seems to help performance?
			BufferedImage newImage = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = newImage.createGraphics();
			g.drawImage(bi, 0, 0, null);
			g.dispose();
			return newImage;
		} catch (Throwable e) {
			e.printStackTrace();
			throw new Error(e);
		}
	}

	public static BufferedImage getPieceImage(byte piece) {
		switch (piece) {
		case Piece.WK:
			return WK;
		case Piece.WQ:
			return WQ;
		case Piece.WR:
			return WR;
		case Piece.WB:
			return WB;
		case Piece.WN:
			return WN;
		case Piece.WP:
			return WP;
		case Piece.BK:
			return BK;
		case Piece.BQ:
			return BQ;
		case Piece.BR:
			return BR;
		case Piece.BB:
			return BB;
		case Piece.BN:
			return BN;
		case Piece.BP:
			return BP;
		}
		throw new Error("Invalid pice: " + piece);
	}

}
