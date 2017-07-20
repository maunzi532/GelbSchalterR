package block;

import area.*;
import java.awt.*;
import java.util.*;
import tex.*;

public class DiaRender extends Render3
{
	public DiaRender()
	{
		super(0);
		minh = 0;
		maxh = 2;
		Color dia = new Color(0, 0, 200, 127);
		teile = new ArrayList<>();
		R3p oben = new R3p(0.5, 0.5, 1);
		R3p unten = new R3p(0.5, 0.5, 0);
		double dp = SIN.t * Math.PI * 2 / SIN.tm;
		int edg = 4;
		R3p last = dreh(dp);
		for(int i = 1; i <= edg; i++)
		{
			R3p neu = dreh(dp + i / (double) edg);
			teile.add(new R3t(false, dia, oben, last, neu));
			teile.add(new R3t(false, dia, unten, last, neu));
			last = neu;
		}
	}

	private R3p dreh(double dreh)
	{
		return new R3p(Math.sin(dreh * Math.PI * 2) / 2 + 0.5, Math.cos(dreh * Math.PI * 2) / 2 + 0.5, 0.5);
	}
}