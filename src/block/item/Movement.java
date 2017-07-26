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
	public Item kopie(BlockLab blockLab)
	{
		Movement i1 = new Movement();
		i1.blockLab = blockLab;
		return i1;
	}

	public boolean weg()
	{
		return false;
	}

	public void setzeOptionen(int xp, int yp, int hoeheA, int[][] geht, int[][] gehtT)
	{
		for(int r = 0; r <= 3; r++)
		{
			int xm = r != 3 ? r - 1 : 0;
			int ym = r != 0 ? r - 2 : 0;
			if(xp + xm < 0 || yp + ym < 0 || xp + xm >= blockLab.xw || yp + ym >= blockLab.yw)
				continue;
			int b = feldBegehbar(xp, yp, xp + xm, yp + ym, hoeheA, r);
			if(b > 0)
			{
				geht[yp + ym][xp + xm] = b;
				gehtT[r + 1][0] = b;
				gehtT[r + 1][1] = xp + xm;
				gehtT[r + 1][2] = yp + ym;
			}
		}
	}

	private int feldBegehbar(int xp, int yp, int xf, int yf, int hoeheA, int richtung)
	{
		Integer ph = blockLab.feld[yp][xp].getH(richtung, false);
		if(blockLab.feld[yp][xp].bodenH() != hoeheA)
			ph = hoeheA;
		Integer fh = blockLab.feld[yf][xf].getH((richtung + 2) % 4, true);
		if(ph == null || fh == null)
			return 0;
		if(Objects.equals(ph, fh))
			return fh;
		return SIN.cheatmode ? blockLab.hoeheA : 0;
	}

	public boolean benutze(int[][] gehtT, int r, boolean main)
	{
		if(blockLab.pfadmodus)
		{
			if(r > 0)
			{
				blockLab.setRichtung(r - 1);
				if(r == 1 && blockLab.xp > 0)
					blockLab.xp--;
				else if(r == 2 && blockLab.yp > 0)
					blockLab.yp--;
				else if(r == 3 && blockLab.xp < blockLab.xw - 1)
					blockLab.xp++;
				else if(r == 4 && blockLab.yp < blockLab.yw - 1)
					blockLab.yp++;
			}
			blockLab.angleichen();
			return true;
		}
		if(gehtT[r][0] <= 0)
		{
			if(r > 0 && TA.take[r + 36] == 2)
				blockLab.setRichtung(r - 1);
			return false;
		}
		if(r > 0)
			blockLab.setRichtung(r - 1);
		blockLab.xp = gehtT[r][1];
		blockLab.yp = gehtT[r][2];
		blockLab.hoeheA = gehtT[r][0];
		blockLab.feld[gehtT[r][2]][gehtT[r][1]].gehen();
		return true;
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