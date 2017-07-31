package area;

public interface Feld
{
	int visualH();

	int texZero();

	int markH();

	void addToRender(Area area, boolean darauf, int xcp, int ycp);
}