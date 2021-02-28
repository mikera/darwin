package darwin;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoveGenTest {

	public void testPawnMoves() {
		BitBoard bb=BitBoard.START;
		
		MoveGen mg=new MoveGen();
		mg.setup(bb);
		mg.genPawnMoves();
		
		assertEquals(16,mg.count);
	}
	
	public void testPawnCaptures() {
		String fen="rnbqkbnr/8/8/pppppppp/PPPPPPPP/8/8/RNBQKBNR w KQkq - 0 9";
		BitBoard bb=BitBoard.fromFEN(fen);

		MoveGen mg=new MoveGen();
		mg.setup(bb);
		mg.genPawnMoves();
		assertEquals(14,mg.count);
		for (int i=0; i<14; i++) {
			int move=mg.getMove(i);
			assertEquals(Piece.BP,Move.capturePiece(move));
		}
	}
}
