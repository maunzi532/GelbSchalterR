package tex;

public class Render
{
	final String what;
	final String text;
	public final int height;
	public final boolean rerender;

	protected Render(int height, boolean rerender)
	{
		what = null;
		text = null;
		this.height = height;
		this.rerender = rerender;
	}

	public Render(String what, int height)
	{
		this.what = what;
		text = null;
		this.height = height;
		rerender = false;
	}

	public Render(String what, String text, int height)
	{
		this.what = what;
		this.text = text;
		this.height = height;
		rerender = false;
	}
}