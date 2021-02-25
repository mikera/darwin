package darwin;

public class BitBoard {
	public long bp;
	public long br;
	public long bn;
	public long bb;
	public long bq;
	public long bk;
	public long wp;
	public long wr;
	public long wn;
	public long wb;
	public long wq;
	public long wk;
	
	public boolean whiteMove;
	public long enPassantTarget;
	
	public byte castles; // 1=K 2=Q 4=k, 8=q
	public int halfMoves; // half moves since last pawn move or capture
	
	public static BitBoard fromFEN(String s) {
		String[] ss=s.trim().split(" ");
		BitBoard bb=new BitBoard();
		
		// Parse piece positions
		String[] ps=ss[0].split("/");
		for (int rank=7; rank>=0; rank--) {
			String p=ps[7-rank];
			int file=0;
			int ix=0;
			while (file<8) {
				char c=p.charAt(ix++);
				long b=Util.bit(rank,file);
				switch (c) {
				case 'K': bb.wk+=b; break;
				case 'Q': bb.wq+=b; break;
				case 'R': bb.wr+=b; break;
				case 'B': bb.wb+=b; break;
				case 'N': bb.wn+=b; break;
				case 'P': bb.wp+=b; break;
				case 'k': bb.bk+=b; break;
				case 'q': bb.bq+=b; break;
				case 'r': bb.br+=b; break;
				case 'b': bb.bb+=b; break;
				case 'n': bb.bn+=b; break;
				case 'p': bb.bp+=b; break;
				default: file+=c-'1';
				}
				file+=1;
			}
		}
		
		bb.whiteMove=(ss[1].charAt(0)=='w');
		bb.castles=parseCastles(ss[2]);
		bb.enPassantTarget=(ss[3].charAt(0)=='-')?0L:Util.bit(ss[3]);
		bb.halfMoves=Integer.parseInt(ss[4]);
		
		return bb;
	}

	private static byte parseCastles(String c) {
		return (byte)((c.contains("K")?1:0)+(c.contains("Q")?2:0)+(c.contains("k")?4:0)+(c.contains("q")?8:0));
	}
	
}
