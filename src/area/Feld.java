package area;

import java.util.*;
import tex.*;

public interface Feld
{
	int daraufH();

	int markH();

	ArrayList<Render> addToRender(RenderCreater rc, boolean darauf, boolean preview);
}