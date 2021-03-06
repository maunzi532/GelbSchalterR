package block.item;

import block.*;
import block.state.*;
import shift.*;

public class Wuerfel extends Item
{
	public char farbe;
	public D3C ort;

	public Wuerfel(char farbe)
	{
		id = 9 + (farbe - 'A');
		this.farbe = farbe;
	}

	public Wuerfel(char farbe, D3C ort)
	{
		id = 9 + (farbe - 'A');
		this.farbe = farbe;
		this.ort = ort;
	}

	@Override
	public void setzeOptionen(int xp, int yp, int hp, int xw, int yw, BFeld fp)
	{
		if(ort == null)
		{
			if(fp.wuerfelPlatzierbar(true) == hp)
				option(xp, yp, hp, 0);
			for(int r = 0; r <= 3; r++)
			{
				int xm = r != 3 ? r - 1 : 0;
				int ym = r != 0 ? r - 2 : 0;
				if(xp + xm < 0 || yp + ym < 0 || xp + xm >= xw || yp + ym >= yw)
					continue;
				BFeld nf = schalterR.feld[yp + ym][xp + xm];
				if(nf.wuerfelPlatzierbar(true) == hp)
					option(xp + xm, yp + ym, hp, r + 1);
			}
		}
		else
		{
			boolean t0 = false;
			if(fp.wspender == farbe && fp.bodenH() == hp)
			{
				option(xp, yp, hp, 0);
				t0 = true;
			}
			if(!t0)
			{
				BFeld tf = schalterR.feld[ort.y][ort.x];
				if(tf.wporter)
				{
					option(ort.x, ort.y, ort.h, 0);
					t0 = true;
				}
			}
			int taste = -1;
			int x1 = xp;
			int y1 = yp;
			if(ort.x == xp && ort.y == yp)
				taste = 0;
			else
				for(int r = 0; r <= 3; r++)
				{
					int xm = r != 3 ? r - 1 : 0;
					int ym = r != 0 ? r - 2 : 0;
					if(ort.x == xp + xm && ort.y == yp + ym)
					{
						x1 = xp + xm;
						y1 = yp + ym;
						taste = r + 1;
						break;
					}
				}
			if(taste >= 1)
			{
				if(t0 && taste == 0)
					return;
				BFeld nf = schalterR.feld[y1][x1];
				if(nf.wuerfelPlatzierbar(false) == hp)
					option(x1, y1, hp, taste);
			}
		}
	}

	@Override
	public String edgeText(int edge)
	{
		if(edge == 3)
			return "9";
		return null;
	}

	public boolean benutze(int num, boolean cl, boolean lvm)
	{
		setzeR(schalterR.xp, schalterR.yp, g1.get(num).x, g1.get(num).y);
		D3C zo = g1.get(num);
		BFeld nf = schalterR.feld[zo.y][zo.x];
		if(ort == null)
		{
			ort = zo;
			nf.wuerfel1 = this;
		}
		else
		{
			BFeld of = schalterR.feld[ort.y][ort.x];
			if(of.wporter && (zo.x != schalterR.xp || zo.y != schalterR.yp) && zo.x == ort.x && zo.y == ort.y)
				schalterR.gehen(ort, true);
			of.wuerfel1 = null;
			ort = null;
		}
		schalterR.showItems.actionTaken();
		return true;
	}

	@Override
	public void vernichten()
	{
		if(ort != null)
		{
			BFeld nf = schalterR.feld[ort.y][ort.x];
			nf.wuerfel1 = null;
		}
	}

	@Override
	public String marker(boolean hier)
	{
		return "B";
	}

	@Override
	public Item kopie(SchalterR schalterR)
	{
		Wuerfel i1 = new Wuerfel(farbe, ort);
		i1.schalterR = schalterR;
		return i1;
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof Wuerfel && farbe == ((Wuerfel) o).farbe;
	}

	@Override
	public String bildname()
	{
		return "Würfel" + (ort != null ? "1" : "") + farbe;
	}

	@Override
	public ItemD saveState()
	{
		if(ort != null)
			return new ItemD(id, (int) farbe, ort.x, ort.y, ort.h);
		return new ItemD(id, (int) farbe);
	}
}