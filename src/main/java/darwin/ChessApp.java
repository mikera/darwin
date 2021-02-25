package darwin;

import javax.swing.JFrame;

public class ChessApp {

	public static void main (String[] args) {
		
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	JFrame frame=new JFrame("Darwin Chess");
            	
            	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            	 
                // Add a board
                BoardPanel panel = new BoardPanel();
                panel.board=BitBoard.START;
                frame.getContentPane().add(panel);
         
                // Display the frame
                frame.pack();
                frame.setVisible(true);
            }
        });
	}
}
