package block;

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
		if(blockLab.feld[yp][xp].hoehe != hoeheA)
			ph = hoeheA;
		Integer fh = blockLab.feld[yf][xf].getH((richtung + 2) % 4, true);
		if(ph == null || fh == null)
			return 0;
		if(Objects.equals(ph, fh))
			return fh;
		//Cheat Mode
		return 1;
	}

	public boolean benutze(int xp, int yp, int hoeheA, int[][] gehtT, int r)
	{
		if(gehtT[r][0] <= 0)
			return false;
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
}