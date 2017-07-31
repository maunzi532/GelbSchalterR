package tex;

import java.awt.*;
import java.util.*;

public class R3t
{
	private final R3p[] p;
	private final boolean flach;
	private final Color farbe;

	public R3t(boolean flach, Color farbe, R3p... p)
	{
		this.flach = flach;
		this.farbe = farbe;
		this.p = p;
	}

	public void d(Graphics2D gd, int hi, int th, int sh, int tw)
	{
		if(flach)
		{
			if((int)(p[0].n[2] * th) == hi)
			{
				int[] xp = new int[p.length];
				int[] yp = new int[p.length];
				for(int i = 0; i < p.length; i++)
				{
					xp[i] = (int)(p[i].n[0] * tw) + sh;
					yp[i] = (int)(p[i].n[1] * tw) + sh;
				}
				double shd = 4;
				gd.setColor(new Color(shd(farbe.getRed(), shd), shd(farbe.getGreen(), shd), shd(farbe.getBlue(), shd), farbe.getAlpha()));
				gd.fillPolygon(xp, yp, p.length);
			}
			return;
		}
		ArrayList<R3p> unten = new ArrayList<>();
		ArrayList<R3p> oben = new ArrayList<>();
		int end = 0;
		for(int j = 0; j < 3; j++)
		{
			if(p[j].n[2] * th >= hi)
				oben.add(p[j]);
			else
				unten.add(p[j]);
			if(p[j].n[2] * th >= hi + 1)
				end++;
		}
		if(unten.size() == 0 || oben.size() == 0 || end == 0)
			return;
		R3p m;
		R3p[] a = new R3p[2];
		if(unten.size() == 2)
		{
			m = oben.get(0);
			a[0] = unten.get(0);
			a[1] = unten.get(1);
		}
		else
		{
			m = unten.get(0);
			a[0] = oben.get(0);
			a[1] = oben.get(1);
		}
		if(m.n[2] - a[0].n[2] == 0 || m.n[2] - a[1].n[2] == 0)
			return;
		double[] h = new double[]{hi / (double) th, (hi + 1) / (double) th};
		//x1, y1, x2, y2, x1a, y1a, x2a, y2a
		double[] fl = new double[8];
		for(int i = 0; i < 8; i++)
			fl[i] = ((m.n[2] - h[i / 4]) * a[(i / 2) % 2].n[i % 2] + (h[i / 4] - a[(i / 2) % 2].n[2]) * m.n[i % 2]) / (m.n[2] - a[(i / 2) % 2].n[2]) * tw + sh;
		double shdx = fl[0] - fl[4] + fl[2] - fl[6] + 2;
		double shdy = fl[1] - fl[5] + fl[3] - fl[7] - 2;
		double shd = Math.sqrt(shdx * shdx + shdy * shdy);
		gd.setColor(new Color(shd(farbe.getRed(), shd), shd(farbe.getGreen(), shd), shd(farbe.getBlue(), shd), farbe.getAlpha()));
		int[] xk = new int[]{inf(fl, 0), inf(fl, 2), inf(fl, 6) - 1, inf(fl, 4) - 1};
		int[] yk = new int[]{inf(fl, 1), inf(fl, 3), inf(fl, 7) - 1, inf(fl, 5) - 1};
		gd.fillPolygon(xk, yk, 4);
	}

	private int inf(double[] fl, int n)
	{
		return (int) fl[n];
	}

	private int shd(int vor, double shd)
	{
		int v2 = vor + (int)(shd * 10);
		if(v2 > 255)
			return 255;
		if(v2 < 0)
			return 0;
		return v2;
	}
}