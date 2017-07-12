package block;

import laderLC.*;

public abstract class Item
{
	public BlockLab blockLab;
	public int level;
	public int disable;

	public Item(){}

	public Item(int level)
	{
		this.level = level;
	}

	public abstract Item kopie(BlockLab blockLab);

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

	public boolean enabled(int disable)
	{
		return true;
	}

	public void setzeOptionen(int xp, int yp, int hoeheA, int[][] geht, int[][] gehtT){}

	public boolean benutze(int xp, int yp, int hoeheA, int[][] gehtT, int r)
	{
		return false;
	}

	public boolean benutze(int xp, int yp, int hoeheA, int[][] geht, int x, int y)
	{
		if(x < 0 || y < 0 || x >= blockLab.xw || y >= blockLab.yw || geht[y][x] <= 0)
			return false;
		blockLab.xp = x;
		blockLab.yp = y;
		blockLab.hoeheA = geht[y][x];
		blockLab.feld[y][x].gehen();
		return true;
	}

	public String speichername()
	{
		return getClass().getSimpleName().toLowerCase();
	}

	public abstract String bildname();

	private static final KXS IKL2 = new KXS(false, false, true, true, false);

	public void lies(String build, int errStart, int errEnd, ErrorVial vial)
	{
		level = 1;
		if(build.startsWith("{") && build.endsWith("}"))
			build = build.substring(1, build.length() - 1);
		LC2.superwaguh(build, errStart, vial, IKL2, this, "lies2");
	}

	public void lies2(String value, Integer errStart, Integer errEnd, ErrorVial vial, String textKey)
	{
		try
		{
			if(textKey.toLowerCase().equals("level"))
				level = Integer.parseInt(value);
		}catch(Exception e)
		{
			vial.add(new CError("Invalides Setzen eines Werts", errStart, errEnd));
		}
	}

	public void speichern(StringBuilder sb)
	{
		sb.append("level = ").append(String.valueOf(level));
	}

	public void speichernZ(StringBuilder sb, String key, String value)
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
}