package area;

import block.state.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import tex.*;

public class SIN
{
	public static boolean cheatmode;
	public static int testmode;
	public static boolean ende;
	public static Texturen tex;
	static JFrame fr;
	static Dimension size;
	static Dimension size2;
	static BufferedImage img;
	static Graphics2D gd;
	public static Area area;
	public static int mapview;
	public static int kamZoom = 8;
	static int mapKamZoom = 16;
	public static int fokusX;
	public static int fokusY;
	public static int mfokusX;
	public static int mfokusY;

	public static void start(Area area1, Texturen tex1, boolean chm, int tem)
	{
		ende = false;
		mapview = 0;
		cheatmode = chm;
		testmode = tem;
		area = area1;
		tex = tex1;
		if(fr == null)
		{
			fr = new JFrame();
			fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			TA.einbau(fr);
			fr.pack();
			size2 = new Dimension(800, 800);
			size = new Dimension(size2.width + size2.height / 10 * 3, size2.height);
			fr.setSize(size);
			resizeImg();
			gd.setColor(Color.BLACK);
			gd.fillRect(0, 0, size.width, size.height);
			if(tex.bilder2D.containsKey("Logo"))
			{
				BufferedImage logo = tex.bilder2D.get("Logo");
				gd.drawImage(logo, (size.width - logo.getWidth()) / 2, (size.height - logo.getHeight()) / 2,
						Color.BLACK, null);
			}
			fr.setVisible(true);
			while(!fr.hasFocus())
				U.warte(100);
		}
		else
			resizeImg();
		fr.getGraphics().drawImage(img, 0, 0, null);
		U.warte(200);
		if(testmode > 0)
			ASIN.run();
		else
			run();
	}

	private static void resizeImg()
	{
		size2 = new Dimension(size.width - size.height / 10 * 3, size.height);
		Shift.resize(size2.width, size2.height);
		img = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		gd = img.createGraphics();
		Shift.selectTarget(area.xp, area.yp, area.spielerHoehe(), mapview == 1 ? mapKamZoom : kamZoom);
		Shift.instant();
	}

	public static void run()
	{
		while(!ende)
		{
			if(mapview == 1)
				area.noMovement();
			else
				area.checkFields();
			drawX();
			U.warte(20);
			updatePosition();
			Shift.moveToTarget(area.realX(), area.realY(), mapview == 0);
			if(area.gewonnen)
				ende = true;
		}
		if(area.gewonnen)
		{
			area.noMovement();
			for(int i = 0; i < 25; i++)
			{
				area.victoryTick();
				drawX();
				U.warte(20);
				Shift.moveToTarget(area.realX(), area.realY(), true);
			}
		}
	}

	public static void updatePosition()
	{
		if(!fr.getContentPane().getSize().equals(size))
		{
			size = new Dimension(fr.getContentPane().getSize());
			resizeImg();
		}
		Point m = MouseInfo.getPointerInfo().getLocation();
		Point f = fr.getContentPane().getLocationOnScreen();
		int mx = m.x - f.x - Shift.xd2;
		int my = m.y - f.y - Shift.yd2;
		int fokusX1 = -1;
		int fokusY1 = -1;
		boolean fertig = false;
		for(int iy = area.yw - 1; iy >= 0 && !fertig; iy--)
			for(int ix = area.xw - 1; ix >= 0 && !fertig; ix--)
				if(Shift.checkObDarauf(mx, my, ix, iy, area.feld(iy, ix).visualH()))
				{
					fokusX1 = ix;
					fokusY1 = iy;
					fertig = true;
				}
		fokusX = fokusX1;
		fokusY = fokusY1;
		int mx2 = m.x - f.x;
		int my2 = m.y - f.y;
		if(mx2 >= size2.width && mx2 < size.width && my2 >= 0 && my2 < size2.height)
		{
			mfokusX = (mx2 - size2.width) / (size2.height / 10);
			mfokusY = my2 / (size2.height / 10);
		}
		else
		{
			mfokusX = -1;
			mfokusY = -1;
		}
		area.tick++;
		TA.bereit();
		if(TA.take[83] == 2)
			area.speichern();
		else if(TA.take[82] == 2)
		{
			if(TA.take[16] > 0)
			{
				M.reload = true;
				ende = true;
			}
			else
			{
				area.reset();
				mapview = 0;
				Shift.selectTarget(area.xp, area.yp, area.spielerHoehe(), kamZoom);//TODO 6
			}
		}
		else if(TA.take[75] == 2 && mapview < 2)
		{
			mapview = 1 - mapview;
			Shift.selectTarget(area.xp, area.yp, area.spielerHoehe(), mapview == 1 ? mapKamZoom : kamZoom);//TODO 6
		}
		else
		{
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
				//Shift.selectTarget(area.xp, area.yp, area.spielerHoehe(), mapview == 1 ? mapKamZoom : kamZoom);
			}
			Shift.moveTarget(mapview > 0, mapview == 1 ? mapKamZoom : kamZoom);
			if(mapview != 1 && area.moveX(mfokusX > 0))
			{
				if(area.mapview && mapview != 2)
					mapview = 2;
				if(!area.mapview && mapview == 2)
					mapview = 0;
				Shift.selectTarget(area.xp, area.yp, area.spielerHoehe(), kamZoom);//TODO 6
			}
		}
	}

	public static void drawX()
	{
		area.render(fokusX, fokusY);
		clear();
		//hintergrund();
		tex.placeAll2(gd, area.renders2, area.xw, area.yw);
		rahmen();
		fr.getGraphics().drawImage(img, 0, 0, null);
	}

	private static void clear()
	{
		gd.setColor(Color.BLACK);
		gd.fillRect(0, 0, size.width, size.height);
	}

	/*static void hintergrund()
	{
		if(tex.bilder2D.containsKey("Hintergrund"))
		{
			BufferedImage hintergrund = tex.bilder2D.get("Hintergrund");
			int hw = hintergrund.getWidth();
			int hh = hintergrund.getHeight();
			//int aw = size.width - size.height / 10 * 3;
			//int ah = size.height;
			int shtx = Shift.tile * 20;
			int shty = Shift.tile * 20;
			int shx = Shift.shiftX / 2;
			int shy = Shift.shiftY / 2;
			for(int i1 = 0; i1 < 3; i1++)
				for(int i2 = 0; i2 < 3; i2++)
					gd.drawImage(hintergrund, shtx * i1 - shx, shty * i2 - shy, shtx * (i1 + 1) - shx, shty * (i2 + 1) - shy, 0, 0, hw, hh, null);
		}
	}*/

	private static void rahmen()
	{
		area.rahmen(gd, tex, size2.width, size2.height);
	}
}