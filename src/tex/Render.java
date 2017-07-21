package tex;

public class Render
{
	final String what;
	final String text;
	public int height;
	public boolean rerender;

	public Render(String what, int height)
	{
		this.what = what;
		text = null;
		this.height = height;
	}

	public Render(String what, String text, int height)
	{
		this.what = what;
		this.text = text;
		this.height = height;
	}
}