package block;

import area.*;
import tex.*;

public class SRD
{
	public double richtung;
	private double richtung2;
	public double x, y, z;
	public boolean gelandet;
	public double deep;

	public SRD(BlockLab blockLab)
	{
		reset(blockLab);
	}

	public void reset(BlockLab blockLab)
	{
		richtung2 = 0.75;
		richtung = richtung2;
		x = blockLab.xp;
		y = blockLab.yp;
		z = blockLab.hoeheA + 20;
		deep = 0;
		gelandet = false;
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

	public void tick(BlockLab blockLab)
	{
		if(richtung < richtung2)
		{
			richtung += 0.05;
			if(richtung >= richtung2)
				richtung = richtung2;
		}
		else
		{
			richtung -= 0.05;
			if(richtung <= richtung2)
				richtung = richtung2;
		}
		rr();
		if(x < blockLab.xp)
		{
			x += 0.2;
			if(x >= blockLab.xp)
				x = blockLab.xp;
		}
		if(x > blockLab.xp)
		{
			x -= 0.2;
			if(x <= blockLab.xp)
				x = blockLab.xp;
		}
		if(y < blockLab.yp)
		{
			y += 0.2;
			if(y >= blockLab.yp)
				y = blockLab.yp;
		}
		if(y > blockLab.yp)
		{
			y -= 0.2;
			if(y <= blockLab.yp)
				y = blockLab.yp;
		}
		if(z < blockLab.hoeheA)
		{
			z += 0.2;
			if(z >= blockLab.hoeheA)
				z = blockLab.hoeheA;
		}
		if(z > blockLab.hoeheA)
		{
			z -= 0.2;
			if(z <= blockLab.hoeheA)
				z = blockLab.hoeheA;
		}
		if(!gelandet)
		{
			z -= 0.8;
			if(z <= blockLab.hoeheA)
			{
				z = blockLab.hoeheA;
				gelandet = true;
			}
		}
	}
}