package block.item;

import block.*;
import block.state.*;
import shift.*;

public class FahrenderPfeil extends Item
{
	private int richtung;
	private D3C ort;

	public FahrenderPfeil(int richtung, D3C ort)
	{
		id = 7;
		level = -1;
		this.richtung = richtung;
		this.ort = ort;
	}

	@Override
	public Item kopie(SchalterR schalterR)
	{
		FahrenderPfeil i1 = new FahrenderPfeil(richtung, new D3C(ort.x, ort.y, ort.h));
		i1.schalterR = schalterR;
		return i1;
	}

	@Override
	public boolean weg()
	{
		return level == 0 || schalterR.xp != ort.x || schalterR.yp != ort.y || schalterR.hp != ort.h;
	}

	@Override
	public void setzeOptionen(int xp, int yp, int hp, int xw, int yw, BFeld fp)
	{
		if(schalterR.aufEben())
			option(xp, yp, hp, 0);
		int xm = richtung != 3 ? richtung - 1 : 0;
		int ym = richtung != 0 ? richtung - 2 : 0;
		if(xp + xm < 0 || yp + ym < 0 || xp + xm >= xw || yp + ym >= yw)
			return;
		if(!fp.weggehengeht(hp, richtung))
			return;
		BFeld nf = schalterR.feld[yp + ym][xp + xm];
		if(!nf.betretengeht(hp, (richtung + 2) % 4))
			return;
		if(nf.ebenH() > hp)
			return;
		option(xp + xm, yp + ym, hp, richtung + 1);
	}

	@Override
	public boolean benutze(int num, boolean cl, boolean charge, boolean lvm)
	{
		if(g1.get(num).equals(ort))
			level = 0;
		else
			schalterR.setRichtung(richtung);
		ort = g1.get(num);
		schalterR.gehen(ort);
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
		return new ItemD(id, richtung, ort.x, ort.y, ort.h);
	}
}