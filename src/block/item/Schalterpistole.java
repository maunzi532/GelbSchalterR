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

	@Override
	public void setzeOptionen(int xp, int yp, int hoeheA)
	{
		BFeld akf = blockLab.feld[yp][xp];
		if(akf.blockFarbe == blockLab.farbeAktuell && akf.hoehe >= blockLab.hoeheA)
			return;
		char verboten = 'n';
		if(akf.blockFarbe != 'n' && akf.hoehe > blockLab.hoeheA)
			verboten = akf.blockFarbe;
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
					if(f.schalter != verboten)
						option(xp + i * xm, yp + i * ym, hoeheA, r + 1);
					break;
				}
			}
		}
	}

	public boolean benutze(int num, boolean lvm)
	{
		setzeR(blockLab.xp, blockLab.yp, g1.get(num)[0], g1.get(num)[1]);
		int[] zo = g1.get(num);
		blockLab.farbeAktuell = blockLab.feld[zo[1]][zo[0]].schalter;
		return true;
	}

	@Override
	public boolean benutze(int r, boolean main, boolean lvm)
	{
		if(g3[r] <= 0)
			return false;
		if(r > 0)
			blockLab.setRichtung(r - 1);
		int[] zo = g1.get(g3[r] - 1);
		blockLab.farbeAktuell = blockLab.feld[zo[1]][zo[0]].schalter;
		return true;
	}

	@Override
	public boolean benutze(int x, int y, boolean main, boolean lvm)
	{
		if(x < 0 || y < 0 || x >= blockLab.xw || y >= blockLab.yw || g2[y][x] <= 0)
			return false;
		setzeR(blockLab.xp, blockLab.yp, x, y);
		int[] zo = g1.get(g2[y][x] - 1);
		blockLab.farbeAktuell = blockLab.feld[zo[1]][zo[0]].schalter;
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