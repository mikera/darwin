package darwin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BitBoardTest {

	@Test public void testFENStartingPosition() {
		String fen="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
		BitBoard bb=BitBoard.fromFEN(fen);
		
		assertEquals("e1",Util.square(bb.wk()));
		assertEquals("e8",Util.square(bb.bk()));
		assertEquals("d1",Util.square(bb.wq()));
		assertEquals("d8",Util.square(bb.bq()));
		
		assertEquals(0x00FF000000000000L,bb.bp());
		assertEquals(0x000000000000FF00L,bb.wp());
		assertEquals(0x0000000000000081L,bb.wr());
		assertEquals(0x4200000000000000L,bb.bn());
		
		assertEquals(fen,bb.toFEN());

	}
	
	@Test public void testConfigutations() {
		String fen="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
		BitBoard bb=BitBoard.fromFEN(fen);
		ensureEquivalents(bb);
	}
	
	public void ensureEquivalents(BitBoard bb) {
		assertEquals(bb.wn(),bb.n(true));
		assertEquals(bb.bn(),bb.n(false));
		assertEquals(bb.n(),bb.wn()|bb.bn());
		
		assertEquals(bb.wq(),bb.q(true));
		assertEquals(bb.bq(),bb.q(false));
		assertEquals(bb.q(),bb.wq()|bb.bq());

		assertEquals(bb.wk(),bb.k(true));
		assertEquals(bb.bk(),bb.k(false));
		assertEquals(bb.k(),bb.wk()|bb.bk());

		assertEquals(bb.wb(),bb.b(true));
		assertEquals(bb.bb(),bb.b(false));
		assertEquals(bb.b(),bb.wb()|bb.bb());

		assertEquals(bb.wr(),bb.r(true));
		assertEquals(bb.br(),bb.r(false));
		assertEquals(bb.r(),bb.wr()|bb.br());

		assertEquals(bb.wp(),bb.p(true));
		assertEquals(bb.bp(),bb.p(false));
		assertEquals(bb.p(),bb.wp()|bb.bp());

		assertEquals(bb.white(),bb.wp()|bb.wn()|bb.wb()|bb.wr()|bb.wq()|bb.wk());
		assertEquals(bb.black(),bb.bp()|bb.bn()|bb.bb()|bb.br()|bb.bq()|bb.bk());

	}
}
