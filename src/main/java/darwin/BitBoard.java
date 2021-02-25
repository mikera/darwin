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
	public int fullMoves; // full moves of game (starts at 1, and is incremented after Black's move)
	
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
		bb.fullMoves=Integer.parseInt(ss[5]);
		
		return bb;
	}
	
	public String toFEN() {
		String[] ss=new String[6];
		
		StringBuilder sb=new StringBuilder();
		for (int rank=7; rank>=0; rank--) {
			int empty=0;
			for (int file=0; file<8; file++) {
				long bit=Util.bit(rank, file);
				byte piece=getPiece(bit);
				if (piece>0) {
					if (empty>0) sb.append(empty);
					char pieceName=Piece.toFEN(piece);
					sb.append(pieceName);
					empty=0;
				} else {
					empty+=1;
				}
			}
			if (empty>0) sb.append(empty);
			if (rank>0) sb.append("/");
		}
		ss[0]=sb.toString();
		ss[1]=whiteMove?"w":"b";
		ss[2]=castleString();
		ss[3]=(enPassantTarget==0L)?"-":Util.square(enPassantTarget);
		ss[4]=Integer.toString(halfMoves);
		ss[5]=Integer.toString(fullMoves);
		
		return String.join(" ",ss);
	}

	public byte getPiece(long bit) {
		// Order by likelihood
		if ((wp&bit)!=0) return Piece.WP;
		if ((bp&bit)!=0) return Piece.BP;
		if ((wr&bit)!=0) return Piece.WR;
		if ((br&bit)!=0) return Piece.BR;
		if ((wb&bit)!=0) return Piece.WB;
		if ((bb&bit)!=0) return Piece.BB;
		if ((wn&bit)!=0) return Piece.WN;
		if ((bn&bit)!=0) return Piece.BN;
		if ((wk&bit)!=0) return Piece.WK;
		if ((bk&bit)!=0) return Piece.BK;
		if ((wq&bit)!=0) return Piece.WQ;
		if ((bq&bit)!=0) return Piece.BQ;
		return 0;
		
	}

	private static byte parseCastles(String c) {
		return (byte)((c.contains("K")?1:0)+(c.contains("Q")?2:0)+(c.contains("k")?4:0)+(c.contains("q")?8:0));
	}
	
	private String castleString() {
		String s="";
		if ((castles&1)!=0) s+="K";
		if ((castles&2)!=0) s+="Q";
		if ((castles&4)!=0) s+="k";
		if ((castles&8)!=0) s+="q";
		if (s.length()==0) s="-";
		return s;
	}
	
}
