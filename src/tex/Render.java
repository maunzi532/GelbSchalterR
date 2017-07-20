package tex;

public class Render
{
	String what;
	String text;
	int height;

	public Render(String what, int height)
	{
		this.what = what;
		this.height = height;
	}

	public Render(String what, String text, int height)
	{
		this.what = what;
		this.text = text;
		this.height = height;
	}
}