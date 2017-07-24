package tex;

import java.awt.*;
import java.util.*;

public abstract class Render3 extends Render
{
	public ArrayList<R3t> teile;
	public double minh;
	public double maxh;

	protected Render3(int height, boolean rerender)
	{
		super(height, rerender);
	}

	public void teile(Graphics2D gd, int i, int th, int mh, int tw)
	{
		int hi = i - height * th;
		for(R3t teil : teile)
			teil.d(gd, hi, th, mh - i, tw);
	}
}