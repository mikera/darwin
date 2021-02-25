package darwin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BitBoardTest {

	@Test public void testFEN() {
		String fen="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
		BitBoard bb=BitBoard.fromFEN(fen);
		
		assertEquals("e1",Util.square(bb.wk));
		assertEquals("e8",Util.square(bb.bk));
		assertEquals("d1",Util.square(bb.wq));
		assertEquals("d8",Util.square(bb.bq));
		
		assertEquals(0x00FF000000000000L,bb.bp);
		assertEquals(0x000000000000FF00L,bb.wp);
		assertEquals(0x0000000000000081L,bb.wr);
		assertEquals(0x4200000000000000L,bb.bn);

	}
}
