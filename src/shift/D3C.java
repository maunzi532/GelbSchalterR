package shift;

public class D3C
{
	public final int x, y, h;

	public D3C(int x, int y, int h)
	{
		this.x = x;
		this.y = y;
		this.h = h;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof D3C)) return false;
		D3C d3C = (D3C) o;
		return x == d3C.x && y == d3C.y && h == d3C.h;
	}

	@Override
	public int hashCode()
	{
		int result = x;
		result = 31 * result + y;
		result = 31 * result + h;
		return result;
	}
}