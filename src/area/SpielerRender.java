package area;

import java.awt.*;
import java.util.*;
import tex.*;

public class SpielerRender extends Render3
{
	public static Color w = Color.GRAY;
	public static Color w1 = Color.RED;

	public SpielerRender(int hoeheA, double minh, double maxh, ArrayList<R3t> teile)
	{
		height = hoeheA;
		this.minh = minh;
		this.maxh = maxh;
		this.teile = teile;
		rerender = true;
	}

	public static SpielerRender gib(double dreh, int hoeheA)
	{
		ArrayList<R3t> teile1 = new ArrayList<>();
		R3p[] punkte = new R3p[8];
		double maxh = 1.8;
		for(int i = 0; i < 4; i++)
			punkte[i] = dreh(dreh + (i + 0.5) / 4, 0.6, maxh);
		for(int i = 0; i < 4; i++)
			punkte[i + 4] = dreh(dreh + (i + 0.5) / 4, 0.6, maxh - 0.5);

		teile1.add(new R3t(true, w, punkte[0], punkte[1], punkte[2], punkte[3]));
		teile1.add(new R3t(false, w, punkte[0], punkte[1], punkte[5]));
		teile1.add(new R3t(false, w, punkte[0], punkte[4], punkte[5]));
		teile1.add(new R3t(false, w1, punkte[1], punkte[2], punkte[6]));
		teile1.add(new R3t(false, w1, punkte[1], punkte[5], punkte[6]));
		teile1.add(new R3t(false, w, punkte[2], punkte[3], punkte[7]));
		teile1.add(new R3t(false, w, punkte[2], punkte[6], punkte[7]));
		teile1.add(new R3t(false, w, punkte[3], punkte[0], punkte[4]));
		teile1.add(new R3t(false, w, punkte[3], punkte[7], punkte[4]));

		return new SpielerRender(hoeheA, 0, maxh, teile1);
	}

	private static R3p dreh(double dreh, double scale, double h)
	{
		return new R3p(Math.cos(dreh * Math.PI * 2) / 2 * scale + 0.5, Math.sin(dreh * Math.PI * 2) / 2 * scale + 0.5, h);
	}
}