package block;

import area.*;
import block.item.*;
import java.awt.*;
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

	public void reset()
	{
		schalterR.updateItem(new CheatMovement(this).kopie(schalterR), true);
	}

	public void move()
	{
		int xp = schalterR.xp;
		int yp = schalterR.yp;
		D3C fa = SIN.feldAuswahl;
		if(TA.take[112] == 2)
		{
			String alt = schalterR.feld[yp][xp].speichern(false);
			Object neu = JOptionPane.showInputDialog(SIN.fr, "x   " + xp + "\ny   " + yp, null, JOptionPane.QUESTION_MESSAGE, null, null, alt);
			TA.fix();
			if(neu instanceof String)
			{
				BFeld nf = new BFeld();
				nf.liesDirekt((String) neu);
				sl.feld[yp][xp] = nf;
				schalterR.feld[yp][xp] = BFeld.copy(nf, schalterR);
			}
		}
		int hc = 0;
		if(TA.take[113] == 2)
			hc--;
		if(TA.take[114] == 2)
			hc++;
		if(hc != 0)
		{
			if(TA.take[16] > 0)
			{
				if(fa != null)
				{
					LFeld f1 = sl.feld[fa.y][fa.x];
					f1.hoehe += hc;
					if(f1.hoehe < 0)
						f1.hoehe = 0;
					schalterR.feld[fa.y][fa.x] = BFeld.copy(f1, schalterR);
				}
			}
			else
			{
				schalterR.hp += hc;
				if(schalterR.hp < 0)
					schalterR.hp = 0;
				if(pfadmodus)
					schalterR.angleichen();
			}
		}
		if(TA.take[115] == 2)
		{
			pfadmodus = !pfadmodus;
			if(pfadmodus)
				schalterR.angleichen();
		}
		if(TA.take[116] == 2)
			label68:
			{
				if(fa != null)
				{
					LFeld f1 = sl.feld[fa.y][fa.x];
					if(f1.schalter != 'n')
					{
						f1.schalter = plusfarbe(f1.schalter);
						schalterR.feld[fa.y][fa.x] = BFeld.copy(f1, schalterR);
						break label68;
					}
					if(f1.wspender != 'n')
					{
						f1.wspender = plusfarbe(f1.wspender);
						schalterR.feld[fa.y][fa.x] = BFeld.copy(f1, schalterR);
						break label68;
					}
					if(f1.blockFarbe != 'n')
					{
						f1.blockFarbe = plusfarbe(f1.blockFarbe);
						schalterR.feld[fa.y][fa.x] = BFeld.copy(f1, schalterR);
						break label68;
					}
				}
				schalterR.farbeAktuell = plusfarbe(schalterR.farbeAktuell);
			}
		int ec = 0;
		if(TA.take[117] == 2)
			ec--;
		if(TA.take[118] == 2)
			ec++;
		if(ec != 0)
		{
			if(TA.take[16] > 0)
				schalterR.dias += ec;
			else
			{
				enhkey += ec;
				if(enhkey < 0)
					enhkey = LFeld.maxenh;
				if(enhkey > LFeld.maxenh)
					enhkey = 0;
			}
		}
		if(TA.take[119] == 2)
			enhance(xp, yp, false);
		if(TA.take[203] == 2 && fa != null)
			enhance(fa.x, fa.y, true);
		if(TA.take[127] == 2 && fa != null)
		{
			LFeld f1 = new BFeld(0);
			sl.feld[fa.y][fa.x] = f1;
			schalterR.feld[fa.y][fa.x] = BFeld.copy(f1, schalterR);
		}
	}

	private void enhance(int x, int y, boolean autoh)
	{
		LFeld f1 = sl.feld[y][x];
		f1.enhance(schalterR, enhkey, autoh);
		schalterR.feld[y][x] = BFeld.copy(f1, schalterR);
	}

	private static char plusfarbe(char farbe)
	{
		if(farbe == 'F')
			return 'A';
		return (char)(farbe + 1);
	}

	void rahmen(Graphics2D gd, Texturen tex, int w1, int ht)
	{
		if(Shift.tile > 0)
		{
			RenderCreater rc = new RenderCreater();
			int th = schalterR.hp;
			if(SIN.feldAuswahl != null)
			{
				BFeld fn = BFeld.copy(sl.feld[SIN.feldAuswahl.y][SIN.feldAuswahl.x], schalterR);
				fn.enhance(schalterR, enhkey, true);
				th = fn.markH();
				fn.addToRender(rc, false, true);
			}
			rc.addt("  " + enhkey, th);
			gd.drawImage(tex.placeThese(rc.renders).img, w1 + ht, ht * 2 * 4, ht * 2, ht * 2, null);
		}
	}
}