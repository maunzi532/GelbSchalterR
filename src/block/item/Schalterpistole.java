package block.item;

import block.*;
import block.state.*;
import shift.*;

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
	public Item kopie(SchalterR schalterR)
	{
		Schalterpistole i1 = new Schalterpistole(level, laenge);
		i1.schalterR = schalterR;
		return i1;
	}

	@Override
	public void setzeOptionen(int xp, int yp, int hp)
	{
		BFeld akf = schalterR.feld[yp][xp];
		if(akf.blockFarbe == schalterR.farbeAktuell && akf.hoehe >= schalterR.hp)
			return;
		char verboten = 'n';
		if(akf.blockFarbe != 'n' && akf.hoehe > schalterR.hp)
			verboten = akf.blockFarbe;
		for(int r = 0; r <= 3; r++)
		{
			int xm = r != 3 ? r - 1 : 0;
			int ym = r != 0 ? r - 2 : 0;
			for(int i = 1; i <= laenge; i++)
			{
				if(xp + i * xm < 0 || yp + i * ym < 0 || xp + i * xm >= schalterR.xw || yp + i * ym >= schalterR.yw)
					break;
				BFeld f = schalterR.feld[yp + i * ym][xp + i * xm];
				if(f.getAH() > hp)
					break;
				if(f.schalter != 'n' && f.bodenH() == hp)
				{
					if(f.schalter != verboten)
						option(xp + i * xm, yp + i * ym, hp, r + 1);
					break;
				}
			}
		}
	}

	public boolean benutze(int num, boolean lvm)
	{
		setzeR(schalterR.xp, schalterR.yp, g1.get(num).x, g1.get(num).y);
		D3C zo = g1.get(num);
		schalterR.farbeAktuell = schalterR.feld[zo.y][zo.x].schalter;
		return true;
	}

	@Override
	public boolean benutze(int r, boolean main, boolean lvm)
	{
		if(g3[r] <= 0)
			return false;
		if(r > 0)
			schalterR.setRichtung(r - 1);
		D3C zo = g1.get(g3[r] - 1);
		schalterR.farbeAktuell = schalterR.feld[zo.y][zo.x].schalter;
		return true;
	}

	@Override
	public boolean benutze(int x, int y, boolean main, boolean lvm)
	{
		if(x < 0 || y < 0 || x >= schalterR.xw || y >= schalterR.yw || g2[y][x] <= 0)
			return false;
		setzeR(schalterR.xp, schalterR.yp, x, y);
		D3C zo = g1.get(g2[y][x] - 1);
		schalterR.farbeAktuell = schalterR.feld[zo.y][zo.x].schalter;
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