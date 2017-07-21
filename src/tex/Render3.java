package tex;

import java.awt.*;
import java.util.*;

public abstract class Render3 extends Render
{
	public ArrayList<R3t> teile;
	public double minh;
	public double maxh;

	public Render3()
	{
		super(null, 0);
	}

	public void teile(Graphics2D gd, int i, int th, int mh, int tw)
	{
		int hi = i - height * th;
		for(R3t teil : teile)
			teil.d(gd, hi, th, mh - i, tw);
	}
}