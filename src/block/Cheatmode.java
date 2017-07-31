package block;

import area.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import shift.*;
import tex.*;

public class Cheatmode
{
	private final SchalterR schalterR;
	private final SchalterLies sl;
	public boolean pfadmodus;
	private int enhkey;

	public Cheatmode(SchalterR schalterR, SchalterLies sl)
	{
		this.schalterR = schalterR;
		this.sl = sl;
	}

	public void move()
	{
		int fokusY = 0;
		int fokusX = 0;
		if(SIN.auswahl != null)
		{
			fokusY = SIN.auswahl.y;
			fokusX = SIN.auswahl.x;
		}
		if(TA.take[112] == 2 && SIN.auswahl != null)
		{
			String alt = schalterR.feld[fokusY][fokusX].speichern();
			Object neu = JOptionPane.showInputDialog(null, null, null, JOptionPane.QUESTION_MESSAGE, null, null, alt);
			if(neu instanceof String)
			{
				BFeld nf = new BFeld();
				nf.liesDirekt((String) neu);
				sl.feld[fokusY][fokusX] = nf;
				schalterR.feld[fokusY][fokusX] = BFeld.copy(nf, schalterR);
			}
		}
		if(TA.take[113] == 2 || TA.take[114] == 2)
		{
			int p = 0;
			if(TA.take[113] == 2)
				p--;
			if(TA.take[114] == 2)
				p++;
			if(p != 0)
			{
				if(TA.take[16] > 0 || pfadmodus)
				{
					schalterR.hp += p;
					if(schalterR.hp < 0)
						schalterR.hp = 0;
					if(pfadmodus)
						schalterR.angleichen();
				}
				else if(fokusX >= 0)
				{
					LFeld f1 = sl.feld[fokusY][fokusX];
					f1.hoehe += p;
					if(f1.hoehe < 0)
						f1.hoehe = 0;
					schalterR.feld[fokusY][fokusX] = BFeld.copy(f1, schalterR);
				}
			}
		}
		if(TA.take[115] == 2)
		{
			pfadmodus = !pfadmodus;
			if(pfadmodus)
				schalterR.angleichen();
		}
		if(TA.take[116] == 2 && fokusX >= 0)
		{
			LFeld f1 = sl.feld[fokusY][fokusX];
			if(f1.schalter != 'n')
			{
				f1.schalter = plusfarbe(f1.schalter);
				schalterR.feld[fokusY][fokusX] = BFeld.copy(f1, schalterR);
			}
			else if(f1.blockFarbe != 'n')
			{
				f1.blockFarbe = plusfarbe(f1.blockFarbe);
				schalterR.feld[fokusY][fokusX] = BFeld.copy(f1, schalterR);
			}
			else
				schalterR.farbeAktuell = plusfarbe(schalterR.farbeAktuell);
		}
		if(TA.take[117] == 2)
		{
			if(TA.take[16] > 0)
				schalterR.dias--;
			else
			{
				enhkey--;
				if(enhkey < 0)
					enhkey = LFeld.maxenh;
			}
		}
		if(TA.take[118] == 2)
		{
			if(TA.take[16] > 0)
				schalterR.dias++;
			else
			{
				enhkey++;
				if(enhkey > LFeld.maxenh)
					enhkey = 0;
			}
		}
		if(TA.take[119] == 2 && fokusX >= 0)
		{
			LFeld f1 = sl.feld[fokusY][fokusX];
			if(enhkey == 0)
			{
				f1 = new BFeld(f1.hoehe);
				sl.feld[fokusY][fokusX] = f1;
			}
			f1.enhance(schalterR, enhkey);
			schalterR.feld[fokusY][fokusX] = BFeld.copy(f1, schalterR);
		}
	}

	private static char plusfarbe(char farbe)
	{
		if(farbe == 'F')
			return 'A';
		return (char)(farbe + 1);
	}

	public void rahmen(Graphics2D gd, Texturen tex, int w1, int ht)
	{
		if(Shift.tile > 0)
		{
			schalterR.renders = new ArrayList<>();
			if(enhkey == 0)
				schalterR.renders.add(new Render("Löscher", 1));
			else if(SIN.auswahl != null)
			{
				schalterR.xcp = SIN.auswahl.x;
				schalterR.ycp = SIN.auswahl.y;
				BFeld fn = BFeld.copy(sl.feld[schalterR.ycp][schalterR.xcp], schalterR);
				fn.enhance(schalterR, enhkey);
				fn.addToRender(schalterR, false, -1, -1);
			}
			gd.drawImage(tex.placeThese(schalterR.renders).img, w1 + ht, ht * 2 * 4, ht * 2, ht * 2, null);
		}
	}
}