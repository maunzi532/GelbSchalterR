package block;

public class SRD
{
	public static double richtung;
	private static double richtung2;
	public static double x, y, z;
	public static boolean gelandet;
	public static double deep;

	public static void reset(BlockLab blockLab)
	{
		richtung2 = 0.75;
		richtung = richtung2;
		x = blockLab.xp;
		y = blockLab.yp;
		z = blockLab.hoeheA + 20;
		gelandet = false;
	}

	public static void setRichtung(int r2)
	{
		setRichtung(r2 / 4d);
	}

	public static void setRichtung(double r2)
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

	private static void rr()
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

	public static void tick(BlockLab blockLab)
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