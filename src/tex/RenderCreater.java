package tex;

import java.util.*;

public class RenderCreater
{
	public ArrayList<Render> renders = new ArrayList<>();
	public int sh;

	public void add(Render r)
	{
		renders.add(r);
	}

	public void addw(String name)
	{
		renders.add(new Render(name, sh));
	}

	public void addt(String text, int h)
	{
		renders.add(new Render(h, text));
	}

	public void addm(String name, int h)
	{
		renders.add(new Render(name, h));
	}

	public void addgz(String name, int h)
	{
		if(h > 0)
			renders.add(new Render(name, h));
	}
}