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
	
	public void testMoveTables() {
		assertEquals(0x0000000000000302L,MoveTables.KTARGETS[0]);
		assertEquals(0x0000000000020400L,MoveTables.NTARGETS[0]);
		
		assertEquals(0x0000000000020400L,MoveTables.RAYTARGETS[0+2*64]);
		assertEquals(0x0101010101010100L,MoveTables.RAYTARGETS[0+0*64]);

		assertEquals(0x08080808F8080808L,MoveTables.RTARGETS[Util.boardIndex(3,3)]); // ROOK on d4
		assertEquals(0x8041221400142241L,MoveTables.BTARGETS[Util.boardIndex(3,3)]); // Bishop on d4
		
		for (int i=0; i<64; i++) {
			long rt=MoveTables.RTARGETS[i];
			long bt=MoveTables.BTARGETS[i];
			long qt=MoveTables.BTARGETS[i];
			long kt=MoveTables.KTARGETS[i];
			long nt=MoveTables.NTARGETS[i];

			assertEquals(qt,bt|rt,"Queen moves should equal bishops+rooks");
			assertEquals(0,bt&rt,"Rooks and bishops have no common moves");
			assertEquals(0,qt&nt,"Queens and knights have no common moves");
			assertEquals(kt,qt&kt,"Queen moves should contain all king moves");
		}

	}
}
