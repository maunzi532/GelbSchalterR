package shift;

public class Ziel extends D3C implements Comparable<Ziel>
{
	public final PreItem von;
	private final int id;
	public final int nummer;
	public final int taste;

	public Ziel(int x, int y, int h, PreItem von, int id, int nummer, int taste)
	{
		super(x, y, h);
		this.von = von;
		this.id = id;
		this.nummer = nummer;
		this.taste = taste;
	}

	public Ziel(int x, int y, int h)
	{
		super(x, y, h);
		von = null;
		id = -1;
		nummer = -1;
		taste = -1;
	}

	@Override
	public int compareTo(Ziel ziel)
	{
		if(ziel.y != y)
			return ziel.y - y;
		if(ziel.x != x)
			return ziel.x - x;
		if(ziel.h != h)
			return ziel.h - h;
		return id - ziel.id;
	}
}