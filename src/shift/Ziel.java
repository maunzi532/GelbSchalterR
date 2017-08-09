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

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof Ziel)) return false;
		if(!super.equals(o)) return false;
		Ziel ziel = (Ziel) o;
		return id == ziel.id && nummer == ziel.nummer && taste == ziel.taste &&
				(von != null ? von.equals(ziel.von) : ziel.von == null);
	}

	@Override
	public int hashCode()
	{
		int result = super.hashCode();
		result = 31 * result + (von != null ? von.hashCode() : 0);
		result = 31 * result + id;
		result = 31 * result + nummer;
		result = 31 * result + taste;
		return result;
	}
}