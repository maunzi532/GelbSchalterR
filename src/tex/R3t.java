package tex;

import java.awt.*;
import java.util.*;

public class R3t
{
	R3p[] p;
	boolean flach;
	Color farbe;

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
			if(p[0].n[2] * th == hi)
			{
				int[] xp = new int[p.length];
				int[] yp = new int[p.length];
				for(int i = 0; i < p.length; i++)
				{
					xp[i] = (int)(p[i].n[0] * tw) + sh;
					yp[i] = (int)(p[i].n[1] * tw) + sh;
				}
				gd.setColor(farbe);
				gd.fillPolygon(xp, yp, p.length);
			}
			return;
		}
		ArrayList<R3p> unten = new ArrayList<>();
		ArrayList<R3p> oben = new ArrayList<>();
		for(int j = 0; j < 3; j++)
			if(p[j].n[2] * th >= hi)
				oben.add(p[j]);
			else
				unten.add(p[j]);
		if(unten.size() == 0 || oben.size() == 0)
			return;
		R3p m;
		R3p a1;
		R3p a2;
		if(unten.size() == 2)
		{
			m = oben.get(0);
			a1 = unten.get(0);
			a2 = unten.get(1);
		}
		else
		{
			m = unten.get(0);
			a1 = oben.get(0);
			a2 = oben.get(1);
		}
		if(m.n[2] - a1.n[2] == 0 || m.n[2] - a2.n[2] == 0)
			return;
		double h = hi / (double) th;
		int x1 = (int)(((m.n[2] - h) * a1.n[0] + (h - a1.n[2]) * m.n[0]) / (m.n[2] - a1.n[2]) * tw) + sh;
		int y1 = (int)(((m.n[2] - h) * a1.n[1] + (h - a1.n[2]) * m.n[1]) / (m.n[2] - a1.n[2]) * tw) + sh;
		int x2 = (int)(((m.n[2] - h) * a2.n[0] + (h - a2.n[2]) * m.n[0]) / (m.n[2] - a2.n[2]) * tw) + sh;
		int y2 = (int)(((m.n[2] - h) * a2.n[1] + (h - a2.n[2]) * m.n[1]) / (m.n[2] - a2.n[2]) * tw) + sh;
		double ha = (hi + 1) / (double) th;
		int x1a = (int)(((m.n[2] - ha) * a1.n[0] + (ha - a1.n[2]) * m.n[0]) / (m.n[2] - a1.n[2]) * tw) + sh;
		int y1a = (int)(((m.n[2] - ha) * a1.n[1] + (ha - a1.n[2]) * m.n[1]) / (m.n[2] - a1.n[2]) * tw) + sh;
		int x2a = (int)(((m.n[2] - ha) * a2.n[0] + (ha - a2.n[2]) * m.n[0]) / (m.n[2] - a2.n[2]) * tw) + sh;
		int y2a = (int)(((m.n[2] - ha) * a2.n[1] + (ha - a2.n[2]) * m.n[1]) / (m.n[2] - a2.n[2]) * tw) + sh;
		int[] xk = new int[]{x1, x2, x2a - 1, x1a - 1};
		int[] yk = new int[]{y1, y2, y2a - 1, y1a - 1};
		gd.setColor(farbe);
		gd.fillPolygon(xk, yk, 4);
	}
}