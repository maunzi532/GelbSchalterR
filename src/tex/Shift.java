package tex;

import area.*;
import java.awt.*;

public class Shift
{
	public static int xd2;
	public static int yd2;
	static int basetile, baseth;
	static int startX, startY, startTile;
	static int targetX, targetY, targetTile;
	static int tick, maxtick;
	public static int tile;
	public static int th;
	public static int shiftX;
	public static int shiftY;

	public static void resize(int fw1, int fh)
	{
		xd2 = fw1 / 2;
		yd2 = fh / 2;
		basetile = fh / 2;
		baseth = basetile / 3;
	}

	public static void place3Vor(Graphics2D gd, Image im, int sh, int h)
	{
		gd.drawImage(im, sh - h, sh - h, tile, tile, null);
	}

	public static void placeErsatzTextVor(Graphics2D gd, String s, int sh, int h)
	{
		gd.setFont(new Font("Consolas", Font.PLAIN, (int)(tile * 1.5f / s.length())));
		int fh2 = gd.getFontMetrics().getHeight() / 2;
		gd.setColor(Color.WHITE);
		gd.drawString(s, sh - h, sh - h + tile / 2 + fh2);
	}

	public static void place4(Graphics2D gd, TexturR tr, int xf, int yf)
	{
		gd.drawImage(tr.img, xf * tile - tr.shift - shiftX + xd2, yf * tile - tr.shift - shiftY + yd2, null);
	}

	public static boolean checkObDarauf(int mx, int my, int ix, int iy, int hoch)
	{
		return mx > ix * tile - shiftX - hoch * th &&
				mx < (ix + 1) * tile - shiftX - hoch * th &&
				my > iy * tile - shiftY - hoch * th &&
				my < (iy + 1) * tile - shiftY - hoch * th;
	}

	public static void selectTarget(int newX, int newY, int newH, int divisor, int ticks)
	{
		tick = 0;
		maxtick = ticks;
		startX = shiftX;
		startY = shiftY;
		startTile = tile;
		targetX = newX * basetile / divisor + basetile / 2 / divisor - newH * baseth / divisor;
		targetY = newY * basetile / divisor + basetile / 2 / divisor - newH * baseth / divisor;
		targetTile = basetile / divisor;
	}

	public static void moveTarget(int ticks)
	{
		int mlr = TA.take[39] - TA.take[37];
		int mou = TA.take[40] - TA.take[38];
		if(mlr == 0 && mou == 0)
			return;
		tick = 0;
		maxtick = ticks;
		startX = shiftX;
		startY = shiftY;
		startTile = tile;
		targetX = targetX + basetile / 5 * mlr;
		targetY = targetY + basetile / 5 * mou;
	}

	public static void moveToTarget()
	{
		if(tick >= maxtick)
			return;
		tick++;
		shiftX = (startX * (maxtick - tick) + targetX * tick) / maxtick;
		shiftY = (startY * (maxtick - tick) + targetY * tick) / maxtick;
		tile = basetile / (basetile / ((startTile * (maxtick - tick) + targetTile * tick) / maxtick));
		th = tile * baseth / basetile;
	}
}