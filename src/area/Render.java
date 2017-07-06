package area;

public class Render
{
	String what;
	String text;
	int y;
	int x;
	int height;

	public Render(String what, int y, int x, int height)
	{
		this.what = what;
		this.y = y;
		this.x = x;
		this.height = height;
	}

	public Render(String what, String text, int y, int x, int height)
	{
		this.what = what;
		this.text = text;
		this.y = y;
		this.x = x;
		this.height = height;
	}
}