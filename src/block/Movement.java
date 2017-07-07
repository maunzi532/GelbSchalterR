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

	public void setzeOptionen(int xp, int yp, int hoeheA, int[][] geht)
	{
		if(yp + 1 < blockLab.yw)
			geht[yp + 1][xp] = feldBegehbar(xp, yp, yp + 1, xp, hoeheA, 3);
		if(xp + 1 < blockLab.xw)
			geht[yp][xp + 1] = feldBegehbar(xp, yp, yp, xp + 1, hoeheA, 2);
		if(yp - 1 >= 0)
			geht[yp - 1][xp] = feldBegehbar(xp, yp, yp - 1, xp, hoeheA, 1);
		if(xp - 1 >= 0)
			geht[yp][xp - 1] = feldBegehbar(xp, yp, yp, xp - 1, hoeheA, 0);
	}

	private int feldBegehbar(int xp, int yp, int yf, int xf, int hoeheA, int richtung)
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

	public boolean benutze(int xp, int yp, int hoeheA, int[][] geht, int r)
	{
		int xt = xp + (r != 4 ? r - 2 : 0);
		int yt = yp + (r != 1 ? r - 3 : 0);
		return benutze(xp, yp, hoeheA, geht, xt, yt);
	}

	@Override
	public String bildname()
	{
		return "Spieler";
	}
}