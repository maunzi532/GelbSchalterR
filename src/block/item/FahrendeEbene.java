package block.item;

import block.*;
import block.state.*;
import shift.*;

public class FahrendeEbene extends Item
{
	public D3C start;
	private D3C ort;

	public FahrendeEbene(D3C start)
	{
		id = 8;
		level = -1;
		this.start = start;
		ort = start;
	}

	public FahrendeEbene(D3C start, D3C ort)
	{
		id = 8;
		level = -1;
		this.start = start;
		this.ort = ort;
	}

	@Override
	public Item kopie(SchalterR schalterR)
	{
		FahrendeEbene i1 = new FahrendeEbene(new D3C(start.x, start.y, start.h));
		i1.schalterR = schalterR;
		return i1;
	}

	@Override
	public boolean weg()
	{
		if(schalterR.xp != ort.x || schalterR.yp != ort.y || schalterR.hp != ort.h)
			level = 0;
		if(level == 0 && (schalterR.xp != start.x || schalterR.yp != start.y))
		{
			schalterR.feld[start.y][start.x].fahrebene1 = null;
			return true;
		}
		return false;
	}

	@Override
	public void setzeOptionen(int xp, int yp, int hp, int xw, int yw, BFeld fp)
	{
		if(schalterR.ebeneRichtung >= 4 || level == 0)
			return;
		int xm = schalterR.ebeneRichtung != 3 ? schalterR.ebeneRichtung - 1 : 0;
		int ym = schalterR.ebeneRichtung != 0 ? schalterR.ebeneRichtung - 2 : 0;
		if(xp + xm < 0 || yp + ym < 0 || xp + xm >= xw || yp + ym >= yw)
			return;
		BFeld nf = schalterR.feld[yp + ym][xp + xm];
		if(nf.ebenH() >= hp)
			return;
		option(xp + xm, yp + ym, hp, schalterR.ebeneRichtung + 1);
	}

	@Override
	public boolean benutze(int num, boolean cl, boolean lvm)
	{
		schalterR.setRichtung(schalterR.ebeneRichtung);
		ort = g1.get(num);
		schalterR.gehen(ort, false);
		return true;
	}

	@Override
	public String bildname()
	{
		return "Fahrebene";
	}

	@Override
	public ItemD saveState()
	{
		return new ItemD(id, start.x, start.y, start.h, ort.x, ort.y, ort.h);
	}
}