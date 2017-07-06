package block;

public class Schalterpistole extends Item
{
	int laenge;

	public Schalterpistole(int level, int laenge)
	{
		super(level);
		this.laenge = laenge;
	}

	@Override
	public Item kopie(BlockLab blockLab)
	{
		Schalterpistole i1 = new Schalterpistole(level, laenge);
		i1.blockLab = blockLab;
		return i1;
	}

	public void setzeOptionen(int xp, int yp, int hoeheA, int[][] geht)
	{
		for(int r = 1; r <= 4; r++)
		{
			int xm = r != 4 ? r - 2 : 0;
			int ym = r != 1 ? r - 3 : 0;
			for(int i = 1; i < laenge; i++)
			{
				if(xp + i * xm < 0 || yp + i * ym < 0 || xp + i * xm >= blockLab.xw || yp + i * ym >= blockLab.yw)
					break;
				BFeld f = blockLab.feld[yp + i * ym][xp + i * xm];
				if(f.getAH() > hoeheA)
					break;
				if(f.schalter != 'n' && f.hoehe == hoeheA)
					geht[yp + i * ym][xp + i * xm] = hoeheA;
			}
		}
	}

	public boolean benutze(int xp, int yp, int hoeheA, int[][] geht, int r)
	{
		return false;
	}

	public boolean benutze(int xp, int yp, int hoeheA, int[][] geht, int x, int y)
	{
		if(x < 0 || y < 0 || x >= blockLab.xw || y >= blockLab.yw || geht[y][x] <= 0)
			return false;
		blockLab.farbeAktuell = blockLab.feld[y][x].schalter;
		return true;
	}

	@Override
	public String bildname()
	{
		return "Schalterpistole";
	}
}