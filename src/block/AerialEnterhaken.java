package block;

public class AerialEnterhaken extends Item
{
	boolean doppelt;
	int laenge;

	public AerialEnterhaken(int level, boolean doppelt, int laenge)
	{
		super(level);
		this.doppelt = doppelt;
		this.laenge = laenge;
	}

	@Override
	public Item kopie(BlockLab blockLab)
	{
		AerialEnterhaken i1 = new AerialEnterhaken(level, doppelt, laenge);
		i1.blockLab = blockLab;
		return i1;
	}

	public void setzeOptionen(int xp, int yp, int hoeheA, int[][] geht)
	{
		int ix = xp - laenge;
		if(ix < 0)
			ix = 0;
		int iy = yp - laenge;
		if(iy < 0)
			iy = 0;
		for(; ix <= xp + laenge && ix < geht[0].length; ix++)
			for(int iy2 = iy; iy2 <= yp + laenge && iy2 < geht.length; iy2++)
				geht[iy2][ix] = erreichbar(xp, yp, hoeheA, ix, iy2);
	}

	public int erreichbar(int xp, int yp, int hoeheA, int ix, int iy)
	{
		BFeld zf = blockLab.feld[iy][ix];
		if(zf.enterstange < 0)
			return 0;
		if(zf.enterstange != zf.hoehe && !doppelt)
			return 0;
		int xd = (ix - xp) * (ix - xp);
		int yd = (iy - yp) * (iy - yp);
		int zd = (zf.enterstange - hoeheA) * (zf.enterstange - hoeheA);
		if(xd + yd + zd > laenge * laenge)
			return 0;
		int xn = ix > xp ? xp : ix;
		int xh = ix > xp ? ix : xp;
		int yn = iy > yp ? yp : iy;
		int yh = iy > yp ? iy : yp;
		for(; xn <= xh; xn++)
			for(int yn2 = yn; yn2 <= yh; yn2++)
				if(yn2 != yp && xn != xp && blockLab.feld[yn2][xn].getAH() > hoeheA)
					return 0;
		return zf.enterstange;
	}

	@Override
	public String bildname()
	{
		return doppelt ? "Doppelhaken" : "Enterhaken";
	}
}