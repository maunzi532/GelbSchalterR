package shift;

import area.*;
import java.awt.*;
import java.util.*;
import tex.*;

public class Shift
{
	private static final int delpix = 4;

	public static int xd2;
	public static int yd2;
	private static int mapview;
	private static int kamZoom = 8;
	private static int mapKamZoom = 16;
	private static int basetile, baseth;
	private static int newX, newY, newH, newT;
	private static int curX, curY, curH, curT;
	public static int tile;
	public static int th;
	private static int tshX;
	private static int tshY;
	private static int pixshX;
	private static int pixshY;
	private static int acpix;

	public static void resize(int fw1, int fh)
	{
		xd2 = fw1 / 2;
		yd2 = fh / 2;
		basetile = fh / 2;
		baseth = basetile / 3;
	}

	public static void place4(Graphics2D gd, TexturR tr, int xf, int yf)
	{
		gd.drawImage(tr.img, xf * tile - tr.shift - tshX - th * curH + xd2, yf * tile - tr.shift - tshY - th * curH + yd2, null);
	}

	public static Ziel zeiger(int mex, int mey, ArrayList<Ziel> eintrag)
	{
		int mx = mex - xd2 + tile / 2;
		int my = mey - yd2 + tile / 2;
		for(Ziel z : eintrag)
			if(checkObDarauf(mx, my, z))
				return z;
		for(int iy = SIN.area.yw - 1; iy >= 0; iy--)
			for(int ix = SIN.area.xw - 1; ix >= 0; ix--)
			{
				Ziel z = new Ziel(ix, iy, SIN.area.feld(iy, ix).markH());
				if(checkObDarauf(mx, my, z))
					return z;
			}
		return null;
	}

	private static boolean checkObDarauf(int mx, int my, D3C hier)
	{
		return mx > hier.x * tile - tshX - hier.h * th &&
				mx < (hier.x + 1) * tile - tshX - hier.h * th &&
				my > hier.y * tile - tshY - hier.h * th &&
				my < (hier.y + 1) * tile - tshY - hier.h * th;
	}

	public static void localReset(D3C hier)
	{
		mapview = 0;
		selectTarget(hier);
		curX = newX;
		curY = newY;
		curH = newH;
		newT = mapview == 1 ? mapKamZoom : kamZoom;
		curT = newT;
	}

	public static void selectTarget(D3C new1)
	{
		newX = new1.x;
		newY = new1.y;
		newH = new1.h;
		if(newH < 0)
			newH = 0;
		if(newT <= 0)
			newT = 1;
	}

	public static void moveToTarget(double realX, double realY)
	{
		if(mapview == 0)
		{
			if(acpix < delpix)
				acpix++;
		}
		else if(acpix > 0)
			acpix--;
		if(curX < newX)
			curX++;
		if(curX > newX)
			curX--;
		if(curY < newY)
			curY++;
		if(curY > newY)
			curY--;
		if(curH < newH)
			curH++;
		if(curH > newH)
			curH--;
		newT = mapview == 1 ? mapKamZoom : kamZoom;
		if(curT < newT)
			curT++;
		if(curT > newT)
			curT--;
		pixshX = (int)(realX * tile - curX * tile) * acpix / delpix;
		pixshY = (int)(realY * tile - curY * tile) * acpix / delpix;
		tile = basetile / curT;
		tshX = curX * tile + pixshX;
		tshY = curY * tile + pixshY;
		th = tile * baseth / basetile;
	}

	public static boolean blockMovement()
	{
		return mapview == 1;
	}

	public static boolean control(D3C d3c)
	{
		if(TA.take[75] == 2 && mapview < 2)
		{
			mapview = 1 - mapview;
			selectTarget(d3c);
			return false;
		}
		boolean sc1 = TA.take[77] == 2 || TA.take[210] == 2;
		boolean sc2 = TA.take[78] == 2 || TA.take[211] == 2;
		if(sc1 != sc2)
		{
			if(mapview == 1)
			{
				mapKamZoom += (sc1 ? 1 : -1);
				if(mapKamZoom < 1)
					mapKamZoom = 1;
			}
			else
			{
				kamZoom += (sc1 ? 1 : -1);
				if(kamZoom < 4)
					kamZoom = 4;
			}
		}
		if(mapview > 0)
		{
			int mlr = TA.take[39] - TA.take[37];
			int mou = TA.take[40] - TA.take[38];
			if(mlr != 0 || mou != 0)
			{
				newX += mlr;
				newY += mou;
			}
		}
		return !blockMovement();
	}

	public static void specialView(D3C d3c, boolean spv)
	{
		if(spv && mapview != 2)
			mapview = 2;
		if(!spv && mapview == 2)
			mapview = 0;
		selectTarget(d3c);
	}
}