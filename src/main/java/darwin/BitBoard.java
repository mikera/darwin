package darwin;


public final class BitBoard {
	public static final BitBoard START = fromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
	
	private long black;
	private long white;
	private long pawn;
	private long bishop;
	private long rook;
	private long king;
	
	public long bn;
	public long wn;
	
	public long white() {
		return white;
	}
	
	public long black() {
		return black;
	}
	
	public long occupied() {
		return white|black;
	}
	
	public long p() {
		return pawn;
	}
	
	public long p(boolean side) {
		return pawn&(side?white:black);
	}
	
	public long wp() {
		return pawn&white;
	}
	
	public long bp() {
		return pawn&black;
	}
	
	public long k() {
		return king;
	}
	
	public long k(boolean side) {
		return king&(side?white:black);
	}
	
	public long wk() {
		return king&white;
	}
	
	public long bk() {
		return king&black;
	}
	
	public long b() {
		return (bishop&~rook);
	}
	
	public long b(boolean side) {
		return (bishop&~rook)&(side?white:black);
	}
	
	public long wb() {
		return (bishop&~rook)&white;
	}
	
	public long bb() {
		return (bishop&~rook)&black;
	}
	
	public long q() {
		return (bishop&rook);
	}
	
	public long q(boolean side) {
		return (bishop&rook)&(side?white:black);
	}
	
	public long wq() {
		return (bishop&rook)&white;
	}
	
	public long bq() {
		return (bishop&rook)&black;
	}
	
	public long r() {
		return (rook&~bishop);
	}
	
	public long r(boolean side) {
		return (rook&~bishop)&(side?white:black);
	}
	
	public long wr() {
		return (rook&~bishop)&white;
	}
	
	public long br() {
		return (rook&~bishop)&black;
	}



	
	public boolean whiteMove;
	public long enPassantTarget; // bit location of en passant target (capturable) pawn
	
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
				case 'K': bb.king+=b; bb.white+=b; break;
				case 'Q': bb.rook+=b; bb.bishop+=b; bb.white+=b; break;
				case 'R': bb.rook+=b; bb.white+=b; break;
				case 'B': bb.bishop+=b; bb.white+=b; break;
				case 'N': bb.wn+=b; bb.white+=b; break;
				case 'P': bb.pawn+=b; bb.white+=b; break;
				case 'k': bb.king+=b; bb.black+=b; break;
				case 'q': bb.rook+=b; bb.bishop+=b; bb.black+=b; break;
				case 'r': bb.rook+=b; bb.black+=b; break;
				case 'b': bb.bishop+=b; bb.black+=b; break;
				case 'n': bb.bn+=b; bb.black+=b; break;
				case 'p': bb.pawn+=b; bb.black+=b; break;
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
	
	public byte getPiece(byte pos) {
		return getPiece(Util.bit(pos));
	}

	public byte getPiece(long bit) {
		if ((white&bit)!=0) {
			// Order by likelihood
			if ((pawn&bit)!=0) return Piece.WP;
			if ((rook&bit)!=0) {
				return ((bishop&bit)==0)?Piece.WR:Piece.WQ;
			}
			if ((bishop&bit)!=0) return Piece.WB;
			if ((wn&bit)!=0) return Piece.WN;
			if ((king&bit)!=0) return Piece.WK;
		} else if ((black&bit)!=0) {
			if ((pawn&bit)!=0) return Piece.BP;
			if ((rook&bit)!=0) {
				return ((bishop&bit)==0)?Piece.BR:Piece.BQ;
			}
			if ((bishop&bit)!=0) return Piece.BB;
			if ((king&bit)!=0) return Piece.BK;
			if ((bn&bit)!=0) return Piece.BN;
		}
		return Piece.NONE;
	}
	
	public byte getPiece(int rank, int file) {
		return getPiece(Util.bit(rank,file));
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
