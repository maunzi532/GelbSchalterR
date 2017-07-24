package block.item;

import block.*;
import laderLC.*;

public abstract class LaengeItem extends Item
{
	int laenge;

	public LaengeItem(){}

	public LaengeItem(int level, int laenge)
	{
		super(level);
		this.laenge = laenge;
	}

	public boolean benutze(int[][] gehtT, int r)
	{
		if(gehtT[r][0] <= 0)
			return false;
		if(r > 0)
			SRD.setRichtung((r - 1) / 4d);
		blockLab.xp = gehtT[r][1];
		blockLab.yp = gehtT[r][2];
		blockLab.hoeheA = gehtT[r][0];
		level--;
		blockLab.feld[gehtT[r][2]][gehtT[r][1]].gehen();
		return true;
	}

	@Override
	public boolean benutze(int[][] geht, int x, int y)
	{
		if(x < 0 || y < 0 || x >= blockLab.xw || y >= blockLab.yw || geht[y][x] <= 0)
			return false;
		setzeR(blockLab.xp, blockLab.yp, x, y);
		blockLab.xp = x;
		blockLab.yp = y;
		blockLab.hoeheA = geht[y][x];
		level--;
		blockLab.feld[y][x].gehen();
		return true;
	}

	public void lies2(String value, Integer errStart, Integer errEnd, ErrorVial vial, String textKey)
	{
		try
		{
			if(textKey.toLowerCase().equals("level"))
				level = Integer.parseInt(value);
			else if(textKey.toLowerCase().equals("länge"))
				laenge = Integer.parseInt(value);
			else
				vial.add(new CError("Unbekannter Wert: " + textKey, errStart, errEnd));
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