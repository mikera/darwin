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
}
