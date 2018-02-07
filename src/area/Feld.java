package area;

import java.util.*;
import tex.*;

public interface Feld
{
	int markH();

	ArrayList<Render> addToRender(RenderCreater rc, boolean darauf, boolean preview);
}