package darwin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BoardPanel extends JPanel {
	
	public BitBoard board;
	
	public Color WHITE_SQUARE=new Color(255,230,140);
	public Color BLACK_SQUARE=new Color(130,80,40);

	public BoardPanel() {
		setPreferredSize(new Dimension(512,512));
	}
	
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension size = this.getSize();
        int d = Math.min(size.width, size.height);
        int sd=d/8;
        for (int rank=0; rank<8; rank++) {
        	for (int file=0; file<8; file++) {
        		int px=sd*(file);
        		int py=sd*(7-rank);
        		g.setColor(((rank+file)&1)==0?BLACK_SQUARE:WHITE_SQUARE);
        		g.fillRect(px, py, sd, sd);
        		
        		if (board!=null) {
        			byte piece=board.getPiece(rank, file);
        			if (piece!=0) {
        				BufferedImage image=Images.getPieceImage(piece);
        				g.drawImage(image, px,py,sd,sd,null);
        			}
        		}
        		
        	}
        }
    }
}
