package area;

import java.awt.*;
import java.util.*;
import tex.*;

public class SpielerRender extends Render3
{
	public static Color w = Color.GRAY;
	public static Color w1 = Color.RED;

	public SpielerRender(double minh, double maxh, ArrayList<R3t> teile)
	{
		this.minh = minh;
		this.maxh = maxh;
		this.teile = teile;
		rerender = true;
	}

	public static SpielerRender gib(double minh, double maxh, int edg, double dreh, double scale)
	{
		ArrayList<R3t> teile1 = new ArrayList<>();
		R3p oben = new R3p(0.5, 0.5, maxh);
		R3p unten = new R3p(0.5, 0.5, minh);
		R3p last = dreh(dreh, scale, (minh + maxh) / 2);
		for(int i = 1; i <= edg; i++)
		{
			R3p neu = dreh(dreh + i / (double) edg, scale, (minh + maxh) / 2);
			teile1.add(new R3t(false, i == 1 ? w1 : w, oben, last, neu));
			teile1.add(new R3t(false, w, unten, last, neu));
			last = neu;
		}
		return new SpielerRender(minh, maxh, teile1);
	}

	private static R3p dreh(double dreh, double scale, double mh)
	{
		return new R3p(Math.cos(dreh * Math.PI * 2) / 2 * scale + 0.5, Math.sin(dreh * Math.PI * 2) / 2 * scale + 0.5, mh);
	}
}