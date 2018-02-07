package shift;

import area.*;
import java.util.*;
import tex.*;

public class SRD
{
	private double richtung;
	private double richtung2;
	public double x, y, h;
	private boolean gelandet;
	public double deep;
	public double dspeed = 0.05;
	public double mspeed = 0.2;

	public SRD(Area area)
	{
		reset(area);
	}

	public void reset(Area area)
	{
		richtung2 = 0.75;
		richtung = richtung2;
		x = area.xp;
		y = area.yp;
		h = area.hp + 20;
		deep = 0;
		gelandet = false;
	}

	public void reset2(Area area)
	{
		richtung2 = 0.75;
		richtung = richtung2;
		x = area.xp;
		y = area.yp;
		h = area.hp;
		deep = 0;
		gelandet = true;
	}

	public void addSpieler(ArrayList<Render>[][] renders2)
	{
		for(int iy = (int) Math.ceil(y); iy >= Math.floor(y); iy--)
			for(int ix = (int) Math.ceil(x); ix >= Math.floor(x); ix--)
				renders2[iy][ix].add(SpielerRender.gib(richtung, h, x - ix, y - iy, deep));
	}


	public void setRichtung(int r2)
	{
		setRichtung(r2 / 4d);
	}

	private void setRichtung(double r2)
	{
		richtung2 = r2;
		if(richtung < richtung2)
		{
			if(richtung - richtung2 + 1 < richtung2 - richtung)
				richtung2 -= 1;
		}
		else if(richtung < richtung2)
		{
			if(richtung2 - richtung + 1 < richtung - richtung2)
				richtung2 += 1;
		}
		if(richtung2 - richtung > 0.4 || richtung - richtung2 > 0.4)
		{
			richtung = richtung2;
			rr();
		}
	}

	private void rr()
	{
		if(richtung >= 1)
		{
			richtung -= 1;
			richtung2 -= 1;
		}
		if(richtung < 0)
		{
			richtung += 1;
			richtung2 += 1;
		}
	}

	public void tick(Area area)
	{
		if(richtung < richtung2)
		{
			richtung += dspeed;
			if(richtung >= richtung2)
				richtung = richtung2;
		}
		else
		{
			richtung -= dspeed;
			if(richtung <= richtung2)
				richtung = richtung2;
		}
		rr();
		if(x < area.xp)
		{
			x += mspeed;
			if(x >= area.xp)
				x = area.xp;
		}
		if(x > area.xp)
		{
			x -= mspeed;
			if(x <= area.xp)
				x = area.xp;
		}
		if(y < area.yp)
		{
			y += mspeed;
			if(y >= area.yp)
				y = area.yp;
		}
		if(y > area.yp)
		{
			y -= mspeed;
			if(y <= area.yp)
				y = area.yp;
		}
		if(h < area.hp)
		{
			h += mspeed;
			if(h >= area.hp)
				h = area.hp;
		}
		if(h > area.hp)
		{
			h -= mspeed;
			if(h <= area.hp)
				h = area.hp;
		}
		if(!gelandet)
		{
			h -= 0.8;
			if(h <= area.hp)
			{
				h = area.hp;
				gelandet = true;
			}
		}
	}
}