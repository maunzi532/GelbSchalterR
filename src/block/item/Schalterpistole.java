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

	public Schalterpistole(int level, int priority, int laenge)
	{
		super(level, priority, laenge);
	}

	@Override
	public Item kopie(SchalterR schalterR)
	{
		Schalterpistole i1 = new Schalterpistole(level, priority, laenge);
		i1.schalterR = schalterR;
		return i1;
	}

	@Override
	public void setzeOptionen(int xp, int yp, int hp, int xw, int yw, BFeld fp)
	{
		char verboten = 'n';
		if(fp.aufBoden())
		{
			if(fp.farbeAktiv() || fp.schalter != 'n')
				return;
			verboten = fp.blockFarbe;
		}
		else
		{
			if(fp.farbeAktiv() && fp.sonstH > hp)
				return;
			if(fp.hoehe > hp)
				verboten = fp.blockFarbe;
		}
		for(int r = 0; r <= 3; r++)
		{
			int xm = r != 3 ? r - 1 : 0;
			int ym = r != 0 ? r - 2 : 0;
			for(int i = 1; i <= laenge; i++)
			{

				int xf = xp + i * xm;
				int yf = yp + i * ym;
				if(xf < 0 || yf < 0 || xf >= xw || yf >= yw)
					break;
				BFeld f = schalterR.feld[yf][xf];
				if(f.getBlockH() > hp)
					break;
				if(f.schalter != 'n' && f.aufBoden())
				{
					if(!f.farbeAktiv() && f.schalter != verboten)
						option(xf, yf, hp, r + 1);
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
		return new ItemD(3, level, priority, laenge);
	}
}