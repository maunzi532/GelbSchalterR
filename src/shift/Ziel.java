package shift;

public class Ziel<T> extends D3C implements Comparable<Ziel>
{
	public final T von;
	public final int id;
	public final int key;

	public Ziel(int x, int y, int h, T von, int id, int key)
	{
		super(x, y, h);
		this.von = von;
		this.id = id;
		this.key = key;
	}

	public Ziel(int x, int y, int h)
	{
		super(x, y, h);
		von = null;
		id = -1;
		key = -1;
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