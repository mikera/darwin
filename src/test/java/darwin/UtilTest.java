package darwin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UtilTest {

	@Test public void testPosConversions() {
		long allBits=0L;
		
		for (int rank=0; rank<8; rank++) {
			for (int file=0; file<8; file++) {
				byte pos=Util.pos(rank,file);
				assertEquals(rank,Util.rank(pos));
				assertEquals(file,Util.file(pos));
				
				long bit=Util.bit(pos);
				allBits|=bit;
				assertEquals(1,Long.bitCount(bit));
				
				assertEquals(pos,Util.pos(bit));
				
				String square=Util.square(pos);
				assertEquals(pos,Util.pos(square),"Square: "+square);
				assertEquals(bit,Util.bit(square),"Square: "+square);
			}
		}
		assertEquals(0xFFFFFFFFFFFFFFFFL,allBits);
	}
}
