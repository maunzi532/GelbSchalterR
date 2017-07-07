package block;

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

	public abstract String bildname();

	@Override
	public boolean equals(Object o)
	{
		return o != null && getClass() == o.getClass();
	}
}