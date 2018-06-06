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
	public static int testmode;
	private static boolean ende;
	private static Texturen tex;
	public static JFrame fr;
	private static Dimension size;
	private static Dimension size2;
	private static BufferedImage img;
	private static Graphics2D gd;
	public static Area area;
	public static Ziel auswahl;
	public static D3C feldAuswahl;
	public static Ziel[] tasten;
	public static int mfokusX;
	public static int mfokusY;

	public static boolean start(Area area1, Texturen tex1, int tem)
	{
		ende = false;
		testmode = tem;
		area = area1;
		tex = tex1;
		if(fr == null)
		{
			fr = new JFrame();
			fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			TA.einbau(fr);
			fr.pack();
			int h1 = Toolkit.getDefaultToolkit().getScreenSize().height / 6 * 5;
			size2 = new Dimension(h1, h1);
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
				S.warte(100);
		}
		else
			resizeImg();
		fr.getGraphics().drawImage(img, 0, 0, null);
		S.warte(200);
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

	private static void run()
	{
		while(!ende)
		{
			S.warte(20);
			if(Shift.blockMovement())
				area.noMovement();
			else
				area.checkFields();
			updatePosition();
			Shift.moveToTarget(area.srd);
			if(area.gewonnen)
				ende = true;
			drawX();
		}
		if(area.gewonnen)
		{
			area.noMovement();
			for(int i = 0; i < 25; i++)
			{
				area.victoryTick();
				drawX();
				S.warte(20);
				Shift.moveToTarget(area.srd);
			}
		}
	}

	private static void updatePosition()
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
		if(mex >= 0 && mex < size.width && mey >= 0 && mey < size2.height)
		{
			ArrayList<Ziel> eintrag = area.anzielbar();
			tasten = new Ziel[5];
			for(int i = 0; i < tasten.length; i++)
				for(Ziel z : eintrag)
					if(z.taste == i)
					{
						tasten[i] = z;
						break;
					}
			if(mex >= size2.width)
			{
				mfokusX = (mex - size2.width) / (size2.height / 10);
				mfokusY = mey / (size2.height / 10);
				auswahl = null;
				feldAuswahl = null;
			}
			else
			{
				mfokusX = -1;
				mfokusY = -1;
				feldAuswahl = Shift.zeigerF(mex, mey);
				auswahl = Shift.zeiger(mex, mey, eintrag);
			}
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
		if(Shift.control(area.d3c()) && area.moveX())
			Shift.specialView(area.d3c(), area.mapview);
	}

	public static void drawX()
	{
		gd.setColor(Color.BLACK);
		gd.fillRect(0, 0, size.width, size.height);
		tex.placeAll2(gd, area.render(auswahl, feldAuswahl), area.xw, area.yw);
		area.rahmen(gd, tex, size2.width, size2.height);
		fr.getGraphics().drawImage(img, 0, 0, null);
	}
}