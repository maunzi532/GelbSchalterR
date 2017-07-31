package block.state;

public class BlockD
{
	final int x, y;

	public BlockD(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof BlockD)) return false;
		BlockD blockD = (BlockD) o;
		return x == blockD.x && y == blockD.y;
	}

	@Override
	public int hashCode()
	{
		int result = x;
		result = 31 * result + y;
		return result;
	}
}