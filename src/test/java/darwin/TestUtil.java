package darwin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestUtil {

	@Test public void testPosConversions() {
		for (int rank=0; rank<8; rank++) {
			for (int file=0; file<8; file++) {
				byte pos=Util.pos(rank,file);
				
				long bit=Util.bit(pos);
				assertEquals(1,Long.bitCount(bit));
				
				assertEquals(pos,Util.pos(bit));
				
				String square=Util.square(pos);
				assertEquals(pos,Util.pos(square),"Square: "+square);
			}
		}
	}
}
