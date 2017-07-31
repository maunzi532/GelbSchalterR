package block.item;

import area.*;
import block.*;
import block.state.*;
import java.util.*;

public class Movement extends Item
{
	public Movement()
	{
		level = -1;
	}

	@Override
	public Item kopie(SchalterR schalterR)
	{
		Movement i1 = new Movement();
		i1.schalterR = schalterR;
		return i1;
	}

	@Override
	public boolean weg()
	{
		return false;
	}

	@Override
	public void setzeOptionen(int xp, int yp, int hp)
	{
		BFeld f = schalterR.feld[yp][xp];
		if(f.lift)
			option(xp, yp, schalterR.hp + (f.liftOben() ? -1 : 1), 0);
		for(int r = 0; r <= 3; r++)
		{
			int xm = r != 3 ? r - 1 : 0;
			int ym = r != 0 ? r - 2 : 0;
			if(xp + xm < 0 || yp + ym < 0 || xp + xm >= schalterR.xw || yp + ym >= schalterR.yw)
				continue;
			int b = feldBegehbar(xp, yp, xp + xm, yp + ym, hp, r);
			if(b >= 0)
				option(xp + xm, yp + ym, b, r + 1);
		}
	}

	private int feldBegehbar(int xp, int yp, int xf, int yf, int hoeheA, int richtung)
	{
		Integer ph = schalterR.feld[yp][xp].getH(richtung, false);
		if(schalterR.feld[yp][xp].bodenH() != hoeheA)
			ph = hoeheA;
		Integer fh = schalterR.feld[yf][xf].getH((richtung + 2) % 4, true);
		if(ph == null || fh == null)
			return -1;
		if(Objects.equals(ph, fh))
			return fh;
		return SIN.cheatmode ? schalterR.hp : -1;
	}

	@Override
	public boolean benutze(int r, boolean main, boolean lvm)
	{
		if(schalterR.cheatmode != null && schalterR.cheatmode.pfadmodus)
		{
			if(r > 0)
			{
				schalterR.setRichtung(r - 1);
				if(r == 1 && schalterR.xp > 0)
					schalterR.xp--;
				else if(r == 2 && schalterR.yp > 0)
					schalterR.yp--;
				else if(r == 3 && schalterR.xp < schalterR.xw - 1)
					schalterR.xp++;
				else if(r == 4 && schalterR.yp < schalterR.yw - 1)
					schalterR.yp++;
			}
			schalterR.angleichen();
			return true;
		}
		if(g3[r] <= 0)
		{
			if(r > 0 && TA.take[r + 36] == 2)
				schalterR.setRichtung(r - 1);
			return false;
		}
		return super.benutze(r, main, false);
	}

	@Override
	public String bildname()
	{
		return "Spieler";
	}

	@Override
	public ItemD saveState()
	{
		return new ItemD(0);
	}
}