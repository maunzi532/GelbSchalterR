package block.item;

import block.*;
import block.state.*;

public class Schalterpistole extends LaengeItem
{
	public Schalterpistole()
	{
		laenge = 5;
	}

	public Schalterpistole(int level, int laenge)
	{
		super(level, laenge);
	}

	@Override
	public Item kopie(BlockLab blockLab)
	{
		Schalterpistole i1 = new Schalterpistole(level, laenge);
		i1.blockLab = blockLab;
		return i1;
	}

	public void setzeOptionen(int xp, int yp, int hoeheA, int[][] geht, int[][] gehtT)
	{
		for(int r = 0; r <= 3; r++)
		{
			int xm = r != 3 ? r - 1 : 0;
			int ym = r != 0 ? r - 2 : 0;
			for(int i = 1; i <= laenge; i++)
			{
				if(xp + i * xm < 0 || yp + i * ym < 0 || xp + i * xm >= blockLab.xw || yp + i * ym >= blockLab.yw)
					break;
				BFeld f = blockLab.feld[yp + i * ym][xp + i * xm];
				if(f.getAH() > hoeheA)
					break;
				if(f.schalter != 'n' && f.bodenH() == hoeheA)
				{
					geht[yp + i * ym][xp + i * xm] = hoeheA;
					gehtT[r + 1][0] = hoeheA;
					gehtT[r + 1][1] = xp + i * xm;
					gehtT[r + 1][2] = yp + i * ym;
					break;
				}
			}
		}
	}

	@Override
	public boolean benutze(int[][] gehtT, int r, boolean main)
	{
		if(gehtT[r][0] <= 0)
			return false;
		if(r > 0)
			blockLab.setRichtung(r - 1);
		blockLab.farbeAktuell = blockLab.feld[gehtT[r][2]][gehtT[r][1]].schalter;
		return true;
	}

	@Override
	public boolean benutze(int[][] geht, int x, int y, boolean main)
	{
		if(x < 0 || y < 0 || x >= blockLab.xw || y >= blockLab.yw || geht[y][x] <= 0)
			return false;
		setzeR(blockLab.xp, blockLab.yp, x, y);
		blockLab.farbeAktuell = blockLab.feld[y][x].schalter;
		return true;
	}

	@Override
	public String bildname()
	{
		return "Schalterpistole";
	}

	@Override
	public ItemD saveState()
	{
		return new ItemD(3, level, laenge);
	}
}