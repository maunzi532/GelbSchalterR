package area;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import tex.*;

public class SIN
{
	public static boolean cheatmode;
	public static Texturen tex;
	static JFrame fr;
	static Dimension size;
	static Dimension size2;
	static BufferedImage img;
	static Graphics2D gd;
	static Area area;
	static int mapview;
	static int kamZoom = 8;
	static int mapKamZoom = 16;
	static int fokusX, fokusY;
	static int mfokusX, mfokusY;
	private static int inputDataT = -1;
	private static int inputData2 = -1;
	private static int inputData3 = -1;

	static void start(Area area1, Texturen tex1, boolean ch)
	{
		cheatmode = ch;
		area = area1;
		tex = tex1;
		fr = new JFrame();
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				if((e.getKeyCode() <= 40 && e.getKeyCode() > 36) || (e.getKeyCode() >= 75 && e.getKeyCode() <= 83))
				{
					inputDataT = -2;
					inputData2 = e.getKeyCode();
					inputData3 = 1;
				}
				else if(e.getKeyCode() == 69)
				{
					inputDataT = -5;
					inputData2 = fokusX;
					inputData3 = fokusY;
				}
			}
		});
		fr.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if(mfokusX >= 0)
				{
					inputDataT = -3;
					inputData2 = mfokusX;
					inputData3 = mfokusY;
				}
				else
				{
					inputDataT = -4;
					inputData2 = fokusX;
					inputData3 = fokusY;
				}
			}
		});
		fr.addMouseWheelListener(new MouseAdapter()
		{
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				inputDataT = -2;
				inputData2 = e.getUnitsToScroll() < 0 ? 77 : 78;
				inputData3 = 1;
			}
		});
		fr.pack();
		size2 = new Dimension(800, 800);
		size = new Dimension(size2.width + size2.height / 10 * 3, size2.height);
		fr.setSize(size);
		resizeImg();
		gd.setColor(Color.WHITE);
		gd.fillRect(0, 0, size.width, size.height);
		if(tex.bilder2D.containsKey("Logo"))
		{
			BufferedImage logo = tex.bilder2D.get("Logo");
			gd.drawImage(logo, (size.width - logo.getWidth()) / 2, (size.height - logo.getHeight()) / 2, Color.white, null);
		}
		gd.setColor(Color.BLACK);
		fr.setVisible(true);
		while(!fr.hasFocus())
			U.warte(100);
		fr.getGraphics().drawImage(img, 0, 0, null);
		U.warte(200);
		run();
	}

	private static void resizeImg()
	{
		size2 = new Dimension(size.width - size.height / 10 * 3, size.height);
		Shift.resize(size2.width, size2.height);
		img = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		gd = img.createGraphics();
		Shift.selectTarget(area.xp, area.yp, area.feld(area.yp, area.xp).visualH(), mapview == 1 ? mapKamZoom : kamZoom, 1);
	}

	public static void run()
	{
		while(!area.gewonnen)
		{
			if(mapview == 1)
				area.noMovement();
			else
				area.checkFields();
			drawX();
			U.warte(20);
			updatePosition();
			Shift.moveToTarget();
		}
		area.noMovement();
		for(int i = 0; i < 15; i++)
		{
			drawX();
			U.warte(20 + i * 3);
			Shift.moveToTarget();
		}
	}

	static void updatePosition()
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
				if(Shift.checkObDarauf(mx, my, ix, iy, area.feld(iy, ix).hoehe))
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
		if(inputDataT == -2 && inputData2 == 83)
		{
			area.speichern();
		}
		else if(inputDataT == -2 && inputData2 == 82)
		{
			area.reset();
			mapview = 0;
			Shift.selectTarget(area.xp, area.yp, area.feld(area.yp, area.xp).visualH(), kamZoom, 6);
		}
		else if(inputDataT == -2 && inputData2 == 75 && mapview < 2)
		{
			mapview = 1 - mapview;
			Shift.selectTarget(area.xp, area.yp, area.feld(area.yp, area.xp).visualH(), mapview == 1 ? mapKamZoom : kamZoom, 6);
		}
		else if(inputDataT == -2 && (inputData2 == 77 || inputData2 == 78))
		{
			if(mapview == 1)
			{
				mapKamZoom += inputData2 * 2 - 155;
				if(mapKamZoom < 1)
					mapKamZoom = 1;
			}
			else
			{
				kamZoom += inputData2 * 2 - 155;
				if(kamZoom < 4)
					kamZoom = 4;
			}
			Shift.selectTarget(area.xp, area.yp, area.feld(area.yp, area.xp).visualH(), mapview == 1 ? mapKamZoom : kamZoom, 1);
		}
		else if(mapview != 1 && area.moveX(inputDataT, inputData2, inputData3))
		{
			if(area.mapview && mapview != 2)
				mapview = 2;
			if(!area.mapview && mapview == 2)
				mapview = 0;
			Shift.selectTarget(area.xp, area.yp, area.feld(area.yp, area.xp).visualH(), kamZoom, 6);
		}
		else if(mapview > 0)
			Shift.moveTarget(inputDataT, inputData2, 6);
		inputDataT = -1;
		inputData2 = -1;
		inputData3 = -1;
	}

	static void drawX()
	{
		area.render(fokusX, fokusY);
		clear();
		hintergrund();
		tex.placeAll2(gd, area.renders2, area.xw, area.yw);
		rahmen();
		fr.getGraphics().drawImage(img, 0, 0, null);
	}

	private static void clear()
	{
		gd.setColor(Color.BLACK);
		gd.fillRect(0, 0, size.width, size.height);
	}

	public static int t;
	public static int tm;

	static void hintergrund()
	{
		if(tex.bilder2D.containsKey("Logo"))
		{
			BufferedImage hintergrund = tex.bilder2D.get("Hintergrund");
			int hw = hintergrund.getWidth();
			int hh = hintergrund.getHeight();
			//int aw = size.width - size.height / 10 * 3;
			//int ah = size.height;
			tm = hw;
			int shtx = Shift.tile * 20;
			int shty = Shift.tile * 20;
			int shx = Shift.shiftX / 2/* + shtx * t / tm*/;
			int shy = Shift.shiftY / 2/* + (int)(shty / 20 * Math.sin(2 * Math.PI * t / tm))*/;
			for(int i1 = 0; i1 < 3; i1++)
				for(int i2 = 0; i2 < 3; i2++)
					gd.drawImage(hintergrund, shtx * i1 - shx, shty * i2 - shy, shtx * (i1 + 1) - shx, shty * (i2 + 1) - shy, 0, 0, hw, hh, null);
			t++;
			if(t > tm)
				t = 0;
		}
	}

	private static void rahmen()
	{
		area.rahmen(gd, tex, size2.width, size.width, size.height);
	}
}