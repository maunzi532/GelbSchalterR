package area;

import block.state.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import shift.*;
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
	/*public static int fokusX;
	public static int fokusY;*/
	public static Ziel auswahl;
	public static int mfokusX;
	public static int mfokusY;

	public static boolean start(Area area1, Texturen tex1, boolean chm, int tem)
	{
		ende = false;
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
		{
			ASIN.run();
			return true;
		}
		else
		{
			run();
			return area.gewonnen;
		}
	}

	private static void resizeImg()
	{
		img = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		gd = img.createGraphics();
		size2 = new Dimension(size.width - size.height / 10 * 3, size.height);
		Shift.resize(size2.width, size2.height);
		Shift.localReset(area.d3c());
	}

	public static void run()
	{
		while(!ende)
		{
			if(Shift.blockMovement())
				area.noMovement();
			else
				area.checkFields();
			drawX();
			U.warte(20);
			updatePosition();
			Shift.moveToTarget(area.realX(), area.realY());
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
				Shift.moveToTarget(area.realX(), area.realY());
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
		int mex = m.x - f.x;
		int mey = m.y - f.y;
		auswahl = Shift.zeiger(mex, mey, area.anzielbar());

		/*int mx = m.x - f.x - Shift.xd2;
		int my = m.y - f.y - Shift.yd2;
		int fokusX1 = -1;
		int fokusY1 = -1;
		boolean fertig = false;
		for(int iy = area.yw - 1; iy >= 0 && !fertig; iy--)
			for(int ix = area.xw - 1; ix >= 0 && !fertig; ix--)
				if(Shift.checkObDarauf(mx, my, new D3C(ix, iy, area.feld(iy, ix).visualH())))
				{
					fokusX1 = ix;
					fokusY1 = iy;
					fertig = true;
				}
		fokusX = fokusX1;
		fokusY = fokusY1;*/
		if(mex >= size2.width && mex < size.width && mey >= 0 && mey < size2.height)
		{
			mfokusX = (mex - size2.width) / (size2.height / 10);
			mfokusY = mey / (size2.height / 10);
		}
		else
		{
			mfokusX = -1;
			mfokusY = -1;
		}
		TA.bereit();
		area.tick++;
		if(TA.take[83] == 2)
			area.speichern();
		if(TA.take[82] == 2)
		{
			if(TA.take[16] > 0)
				ende = true;
			else
			{
				area.reset();
				Shift.localReset(area.d3c());
			}
		}
		if(Shift.control(area.d3c()) && area.moveX(mfokusX > 0))
			Shift.specialView(area.d3c(), area.mapview);
	}

	public static void drawX()
	{
		area.render(auswahl);
		gd.setColor(Color.BLACK);
		gd.fillRect(0, 0, size.width, size.height);
		//hintergrund
		tex.placeAll2(gd, area.renders2, area.xw, area.yw);
		area.rahmen(gd, tex, size2.width, size2.height);
		fr.getGraphics().drawImage(img, 0, 0, null);
	}
}