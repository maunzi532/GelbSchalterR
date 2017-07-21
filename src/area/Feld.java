package area;

public interface Feld
{
	int bodenH();

	int visualH();

	void addToRender(Area area, boolean darauf);
}