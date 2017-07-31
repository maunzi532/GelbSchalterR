package block.item;

import block.*;
import block.state.*;
import java.util.*;
import laderLC.*;
import shift.*;

public abstract class Item
{
	public SchalterR schalterR;
	public int level;

	public Item()
	{
		level = 1;
	}

	public Item(int level)
	{
		this.level = level;
	}

	public abstract Item kopie(SchalterR schalterR);

	public void loescher()
	{
		if(level > 0)
			level--;
	}

	//return true falls eg teleporter oder nach benutzung
	public boolean weg()
	{
		return level == 0;
	}

	public int id;
	public ArrayList<Ziel<Item>> g1;
	public int[][] g2;
	public int[] g3;

	public void setzeOptionen1(int xp, int yp, int hp, int xw, int yw, int id)
	{
		this.id = id;
		g1 = new ArrayList<>();
		g2 = new int[yw][xw];
		g3 = new int[5];
		setzeOptionen(xp, yp, hp);
	}

	public void setzeOptionen(int xp, int yp, int hp){}

	public void option(int x, int y, int h, int key)
	{
		g1.add(new Ziel<>(x, y, h, this, id, g1.size()));
		g2[y][x] = g1.size();
		if(key >= 0)
			g3[key] = g1.size();
	}

	public boolean benutze(int num, boolean lvm)
	{
		setzeR(schalterR.xp, schalterR.yp, g1.get(num).x, g1.get(num).y);
		if(lvm && level > 0)
			level--;
		gzo(g1.get(num));
		return true;
	}

	public boolean benutze(int r, boolean main, boolean lvm)
	{
		if(g3[r] <= 0)
			return false;
		if(r > 0)
			schalterR.setRichtung(r - 1);
		if(lvm && level > 0)
			level--;
		gzo(g1.get(g3[r] - 1));
		return true;
	}

	public boolean benutze(int x, int y, boolean main, boolean lvm)
	{
		if(x < 0 || y < 0 || x >= schalterR.xw || y >= schalterR.yw || g2[y][x] <= 0)
			return false;
		setzeR(schalterR.xp, schalterR.yp, x, y);
		if(lvm && level > 0)
			level--;
		gzo(g1.get(g2[y][x] - 1));
		return true;
	}

	private void gzo(D3C zo)
	{
		schalterR.xp = zo.x;
		schalterR.yp = zo.y;
		schalterR.hp = zo.h;
		schalterR.feld[zo.y][zo.x].gehen();
	}

	void setzeR(int xa, int ya, int xn, int yn)
	{
		if(xn < xa)
			schalterR.setRichtung(0);
		else if(yn < ya)
			schalterR.setRichtung(1);
		else if(xn > xa)
			schalterR.setRichtung(2);
		else if(yn > ya)
			schalterR.setRichtung(3);
	}

	public String speichername()
	{
		return getClass().getSimpleName().toLowerCase();
	}

	public abstract String bildname();

	private static final KXS IKL2 = new KXS(false, false, true, true, false);

	public void lies(String build, int errStart, ErrorVial vial)
	{
		build = LC2.removeKlammernVllt(build);
		LC2.superwaguh(build, errStart, vial, IKL2, this, "lies2");
	}

	@SuppressWarnings("unused")
	public void lies2(String value, Integer errStart, Integer errEnd, ErrorVial vial, String textKey)
	{
		try
		{
			if(textKey.toLowerCase().equals("level"))
				level = Integer.parseInt(value);
			else
				vial.add(new CError("Unbekannter Wert: " + textKey, errStart, errEnd));
		}catch(Exception e)
		{
			vial.add(new CError("Invalides Setzen eines Werts", errStart, errEnd));
		}
	}

	public void speichern(StringBuilder sb)
	{
		sb.append("level = ").append(String.valueOf(level));
	}

	void speichernZ(StringBuilder sb, String key, String value)
	{
		sb.append(", ").append(key);
		if(value != null)
			sb.append(" = ").append(value);
	}

	@Override
	public boolean equals(Object o)
	{
		return o != null && getClass() == o.getClass();
	}

	public abstract ItemD saveState();
}