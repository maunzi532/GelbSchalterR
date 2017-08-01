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
	public void setzeOptionen(int xp, int yp, int hp, int xw, int yw)
	{
		BFeld akf = schalterR.feld[yp][xp];
		if(akf.blockFarbe == schalterR.farbeAktuell && akf.hoehe >= hp)
			return;
		char verboten = 'n';
		if(akf.blockFarbe != 'n' && akf.hoehe > hp)
			verboten = akf.blockFarbe;
		for(int r = 0; r <= 3; r++)
		{
			int xm = r != 3 ? r - 1 : 0;
			int ym = r != 0 ? r - 2 : 0;
			for(int i = 1; i <= laenge; i++)
			{
				if(xp + i * xm < 0 || yp + i * ym < 0 || xp + i * xm >= xw || yp + i * ym >= yw)
					break;
				BFeld f = schalterR.feld[yp + i * ym][xp + i * xm];
				if(f.getBlockedH2() > hp)
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

	public boolean benutze(int num, boolean cl, boolean main, boolean lvm)
	{
		setzeR(schalterR.xp, schalterR.yp, g1.get(num).x, g1.get(num).y);
		D3C zo = g1.get(num);
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