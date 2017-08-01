package area;

public interface Feld<T extends Area>
{
	int daraufH();

	int markH();

	void addToRender(T area, boolean darauf, int xcp, int ycp);
}