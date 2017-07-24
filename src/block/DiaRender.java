package block;

import java.awt.*;
import java.util.*;
import tex.*;

public class DiaRender extends Render3
{
	public DiaRender(int fh, double minh, double maxh, ArrayList<R3t> teile)
	{
		height = fh;
		this.minh = minh;
		this.maxh = maxh;
		this.teile = teile;
		rerender = true;
	}

	public static DiaRender gib(double minh, double maxh, int edg, double start, double scale, Color dia, int fh)
	{
		ArrayList<R3t> teile1 = new ArrayList<>();
		R3p oben = new R3p(0.5, 0.5, maxh);
		R3p unten = new R3p(0.5, 0.5, minh);
		R3p last = dreh(start, scale, (minh + maxh) / 2);
		for(int i = 1; i <= edg; i++)
		{
			R3p neu = dreh(start + i / (double) edg, scale, (minh + maxh) / 2);
			teile1.add(new R3t(false, dia, oben, last, neu));
			teile1.add(new R3t(false, dia, unten, last, neu));
			last = neu;
		}
		return new DiaRender(fh, minh, maxh, teile1);
	}

	private static R3p dreh(double dreh, double scale, double mh)
	{
		return new R3p(Math.sin(dreh * Math.PI * 2) / 2 * scale + 0.5, Math.cos(dreh * Math.PI * 2) / 2 * scale + 0.5, mh);
	}
}