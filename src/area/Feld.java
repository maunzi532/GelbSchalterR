package area;

public interface Feld
{
	int daraufH();

	int markH();

	void addToRender(Area area, boolean darauf, int xcp, int ycp);
}