package block.item;

import block.*;
import block.state.*;
import java.util.*;
import laderLC.*;
import shift.*;

public abstract class Item implements PreItem
{
	SchalterR schalterR;
	int level;
	public boolean disabled;

	Item()
	{
		level = 1;
	}

	Item(int level)
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

	private int id;
	private boolean[] tasten;
	public final ArrayList<Ziel> g1 = new ArrayList<>();

	public void setzeOptionen1(int xp, int yp, int hp, int xw, int yw, int id, boolean[] tasten)
	{
		this.id = id;
		this.tasten = tasten;
		g1.clear();
		setzeOptionen(xp, yp, hp, xw, yw, schalterR.feld[yp][xp]);
	}

	void setzeOptionen(int xp, int yp, int hp, int xw, int yw, BFeld fp){}

	public void noMovement()
	{
		g1.clear();
	}

	void option(int x, int y, int h, int taste)
	{
		if(taste >= 0 && !tasten[taste])
			tasten[taste] = true;
		else
			taste = -1;
		g1.add(new Ziel(x, y, h, this, id, g1.size(), taste));
	}

	@Override
	public String marker()
	{
		return "A";
	}

	@Override
	public String symbol(int taste)
	{
		if(taste == 0)
			return "L";
		if(taste < 0)
			return null;
		return String.valueOf(taste - 1);
	}

	public boolean benutze(int num, boolean cl, boolean main, boolean lvm)
	{
		setzeR(schalterR.xp, schalterR.yp, g1.get(num).x, g1.get(num).y);
		if(lvm && level > 0)
			level--;
		gehen(g1.get(num));
		return true;
	}

	private void gehen(D3C ziel)
	{
		schalterR.xp = ziel.x;
		schalterR.yp = ziel.y;
		schalterR.hp = ziel.h;
		schalterR.feld[ziel.y][ziel.x].gehen();
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