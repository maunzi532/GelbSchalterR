package tex;

import area.*;
import java.awt.*;

public class Shift
{
	private static final int delpix = 4;
	public static int xd2;
	public static int yd2;
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
		gd.drawImage(tr.img, xf * tile - tr.shift - tshX - th * curH + xd2, yf * tile - tr.shift - tshY - th * curH + yd2, null);
	}

	public static boolean checkObDarauf(int mx, int my, int ix, int iy, int hoch)
	{
		return mx > ix * tile - tshX - hoch * th &&
				mx < (ix + 1) * tile - tshX - hoch * th &&
				my > iy * tile - tshY - hoch * th &&
				my < (iy + 1) * tile - tshY - hoch * th;
	}

	public static void selectTarget(int newX1, int newY1, int newH1, int newT1)
	{
		newX = newX1;
		newY = newY1;
		newH = newH1;
		if(newH < 0)
			newH = 0;
		newT = newT1;
		if(newT <= 0)
			newT = 1;
	}

	public static void instant()
	{
		curX = newX;
		curY = newY;
		curH = newH;
		curT = newT;
	}

	public static void moveTarget(boolean keys, int newT1)
	{
		newT = newT1;
		if(keys)
		{
			int mlr = TA.take[39] - TA.take[37];
			int mou = TA.take[40] - TA.take[38];
			if(mlr == 0 && mou == 0)
				return;
			newX += mlr;
			newY += mou;
		}
	}

	public static void moveToTarget(double realX, double realY, boolean usepix)
	{
		if(usepix)
			acpix = delpix;
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
}