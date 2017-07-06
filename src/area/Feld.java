package area;

public abstract class Feld
{
	public int hoehe;

	public int visualH()
	{
		return hoehe;
	}

	public abstract void addToRender(Area area, boolean darauf);
}