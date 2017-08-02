package shift;

import area.*;
import tex.*;

public class SRD
{
	private double richtung;
	private double richtung2;
	public double x, y, z;
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
		z = area.hp + 20;
		deep = 0;
		gelandet = false;
	}

	public void reset2(Area area)
	{
		richtung2 = 0.75;
		richtung = richtung2;
		x = area.xp;
		y = area.yp;
		z = area.hp;
		deep = 0;
		gelandet = true;
	}

	public void addSpieler(Area area, int xcp, int ycp)
	{
		if(xcp >= 0 && (xcp == Math.floor(x) || xcp == Math.ceil(x)) && (ycp == Math.floor(y) || ycp == Math.ceil(y)))
			area.add3(SpielerRender.gib(richtung, z, x - xcp, y - ycp, deep));
	}


	public void setRichtung(int r2)
	{
		setRichtung(r2 / 4d);
	}

	public void setRichtung(double r2)
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
		if(z < area.hp)
		{
			z += mspeed;
			if(z >= area.hp)
				z = area.hp;
		}
		if(z > area.hp)
		{
			z -= mspeed;
			if(z <= area.hp)
				z = area.hp;
		}
		if(!gelandet)
		{
			z -= 0.8;
			if(z <= area.hp)
			{
				z = area.hp;
				gelandet = true;
			}
		}
	}
}