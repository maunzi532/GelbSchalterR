package area;

import java.awt.*;
import java.util.*;
import sun.reflect.generics.reflectiveObjects.*;
import tex.*;

public abstract class Area
{
	public int[][] geht;
	public boolean gewonnen;
	public boolean mapview;
	public int xw;
	public int yw;
	public int xp;
	public int yp;

	public abstract Feld feld(int y, int x);

	public abstract void checkFields();

	public abstract boolean moveX(boolean nichtMap);

	public abstract void rahmen(Graphics2D gd, Texturen tex, int w1, int h);

	public void noMovement()
	{
		geht = new int[yw][xw];
	}

	public void readFL(String c1){}

	private int ycp, xcp;
	private ArrayList<Render> renders;
	protected ArrayList<Render>[][] renders2;

	public void render(int mouseFx, int mouseFy)
	{
		//noinspection unchecked
		renders2 = new ArrayList[yw][xw];
		for(ycp = 0; ycp < yw; ycp++)
			for(xcp = 0; xcp < xw; xcp++)
			{
				renders = new ArrayList<>();
				feld(ycp, xcp).addToRender(this, xcp == xp && ycp == yp);
				if(geht[ycp][xcp] != 0)
					addw("Möglich");
				if(mouseFx == xcp && mouseFy == ycp)
					addw("Auswahl");
				if(xp == xcp && yp == ycp)
					addw("Spieler");
				renders2[ycp][xcp] = renders;
			}
	}

	public abstract void reset();

	public void speichern()
	{
		throw new NotImplementedException();
	}

	public void addw(String name)
	{
		renders.add(new Render(name, feld(ycp, xcp).visualH()));
	}

	public void addw(String name, String text)
	{
		renders.add(new Render(name, text, feld(ycp, xcp).visualH()));
	}

	public void addm(String name, int h)
	{
		renders.add(new Render(name, h));
	}

	public void add3(Render3 r3)
	{
		r3.height = feld(ycp, xcp).visualH();
		renders.add(r3);
	}
}