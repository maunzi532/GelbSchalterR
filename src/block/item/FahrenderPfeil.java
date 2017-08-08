package block.item;

import block.*;
import block.state.*;
import shift.*;

public class FahrenderPfeil extends Item
{
	private int richtung;
	private D3C ort;
	private boolean weg;

	public FahrenderPfeil(){}

	public FahrenderPfeil(int richtung, int xp, int yp, int hp)
	{
		level = -1;
		this.richtung = richtung;
		ort = new D3C(xp, yp, hp);

	}

	@Override
	public Item kopie(SchalterR schalterR)
	{
		FahrenderPfeil i1 = new FahrenderPfeil(richtung, ort.x, ort.y, ort.h);
		i1.schalterR = schalterR;
		return i1;
	}

	@Override
	public boolean weg()
	{
		return weg || schalterR.xp != ort.x || schalterR.yp != ort.y || schalterR.hp != ort.h;
	}

	@Override
	public void setzeOptionen(int xp, int yp, int hp, int xw, int yw, BFeld fp)
	{
		if(fp.aufBoden())
			option(xp, yp, hp, 0);
		int xm = richtung != 3 ? richtung - 1 : 0;
		int ym = richtung != 0 ? richtung - 2 : 0;
		if(xp + xm < 0 || yp + ym < 0 || xp + xm >= xw || yp + ym >= yw)
			return;
		if(fp.aufBoden() && !fp.weggehengeht(richtung))
			return;
		BFeld nf = schalterR.feld[yp + ym][xp + xm];
		if(nf.aufBoden() && !nf.betretengeht((richtung + 2) % 4))
			return;
		if(nf.bodenH() > hp)
			return;
		option(xp + xm, yp + ym, hp, richtung + 1);
	}

	@Override
	public boolean benutze(int num, boolean cl, boolean main, boolean lvm)
	{
		if(g1.get(num).equals(ort))
			weg = true;
		else
			schalterR.setRichtung(richtung);
		ort = g1.get(num);
		gehen(ort);
		return true;
	}

	@Override
	public String bildname()
	{
		return "Enterpfeil";
	}

	@Override
	public ItemD saveState()
	{
		return new ItemD(6, level, priority, richtung, ort.x, ort.y, ort.h);
	}
}