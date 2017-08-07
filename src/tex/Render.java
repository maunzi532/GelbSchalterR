package tex;

public class Render
{
	final String what;
	final boolean sth;
	protected final int height;
	protected final boolean rerender;

	protected Render(int height, boolean rerender)
	{
		what = null;
		sth = true;
		this.height = height;
		this.rerender = rerender;
	}

	public Render(String what, int height)
	{
		this.what = what;
		sth = true;
		this.height = height;
		rerender = false;
	}

	public Render(int height, String text)
	{
		this.what = text;
		sth = false;
		this.height = height;
		rerender = false;
	}
}