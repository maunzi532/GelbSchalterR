package block;

import laderLC.*;

public class Fluegel extends Item
{
	int laenge;

	public Fluegel()
	{
		level = 2;
		laenge = 5;
	}

	public Fluegel(int level, int laenge)
	{
		super(level);
		this.laenge = laenge;
	}

	@Override
	public Item kopie(BlockLab blockLab)
	{
		Fluegel i1 = new Fluegel(level, laenge);
		i1.blockLab = blockLab;
		return i1;
	}

	public void setzeOptionen(int xp, int yp, int hoeheA, int[][] geht, int[][] gehtT)
	{
		for(int r = 0; r <= 3; r++)
		{
			int xm = r != 3 ? r - 1 : 0;
			int ym = r != 0 ? r - 2 : 0;
			for(int i = 1; i < laenge; i++)
			{
				if(xp + i * xm < 0 || yp + i * ym < 0 || xp + i * xm >= blockLab.xw || yp + i * ym >= blockLab.yw)
					break;
				BFeld f = blockLab.feld[yp + i * ym][xp + i * xm];
				if(f.getAH() > hoeheA)
					break;
				if(f.hoehe == hoeheA)
				{
					if(i > 1)
					{
						geht[yp + i * ym][xp + i * xm] = hoeheA;
						gehtT[r + 1][0] = hoeheA;
						gehtT[r + 1][1] = xp + i * xm;
						gehtT[r + 1][2] = yp + i * ym;
					}
					break;
				}
			}
		}
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

	public boolean benutze(int xp, int yp, int hoeheA, int[][] geht, int x, int y)
	{
		boolean re = super.benutze(xp, yp, hoeheA, geht, x, y);
		level--;
		return re;
	}

	@Override
	public String bildname()
	{
		return "Flügel";
	}

	public void lies2(String value, Integer errStart, Integer errEnd, ErrorVial vial, String textKey)
	{
		try
		{
			if(textKey.toLowerCase().equals("level"))
				level = Integer.parseInt(value);
			if(textKey.toLowerCase().equals("länge"))
				laenge = Integer.parseInt(value);
		}catch(Exception e)
		{
			vial.add(new CError("Invalides Setzen eines Werts", errStart, errEnd));
		}
	}

	public void speichern(StringBuilder sb)
	{
		super.speichern(sb);
		speichernZ(sb, "länge", String.valueOf(laenge));
	}
}