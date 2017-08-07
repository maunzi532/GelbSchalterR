package block;

import area.*;
import block.item.*;
import java.awt.*;
import java.util.*;
import tex.*;

public class BFeld extends LFeld implements Feld
{
	private SchalterR schalterR;

	public boolean benutzt = false;

	public BFeld(){}

	public BFeld(int hoehe)
	{
		this.hoehe = hoehe;
	}

	public static BFeld copy(LFeld von, SchalterR schalterR)
	{
		BFeld copy = new BFeld();
		copy.schalterR = schalterR;
		copy.ziel = von.ziel;
		copy.hoehe = von.hoehe;
		copy.blockFarbe = von.blockFarbe;
		copy.sonstH = von.sonstH;
		copy.schalter = von.schalter;
		copy.pfeil = von.pfeil;
		copy.einhauwand = von.einhauwand;
		copy.dia = von.dia;
		copy.diaTuer = von.diaTuer;
		copy.eis = von.eis;
		copy.loescher = von.loescher;
		copy.enterstange = von.enterstange;
		copy.lift = von.lift;
		copy.item = von.item;
		return copy;
	}

	public void reset()
	{
		benutzt = false;
	}

	public boolean betretengeht(int side)
	{
		if(!aufBoden())
			return false;
		if(einhauwand >= 0 && !benutzt && einhauwand != side)
			return false;
		if(pfeil >= 0 && pfeil == side)
			return false;
		if(diaTuer > schalterR.dias)
			return false;
		if(eis && !benutzt && !schalterR.items.contains(new Feuer()))
			return false;
		return true;
	}

	public boolean aufBoden()
	{
		return schalterR.hp == bodenH();
	}

	public boolean weggehengeht(int side)
	{
		return !(pfeil >= 0 && pfeil != side);
	}

	/*public int getBlockLaserH()
	{
		return (einhauwand >= 0 && !benutzt) || diaTuer > 0 || (eis && !benutzt) ? bodenH() + 1 : bodenH();
	}*/

	public int getBlockH()
	{
		return (einhauwand >= 0 && !benutzt) || diaTuer > schalterR.dias || (eis && !benutzt) ? bodenH() + 1 : bodenH();
	}

	public boolean farbeAktiv()
	{
		return blockFarbe == schalterR.farbeAktuell;
	}

	private int steinH()
	{
		return blockFarbe != 'n' && blockFarbe != schalterR.farbeAktuell ? sonstH : hoehe;
	}

	public boolean liftOben()
	{
		return lift && schalterR.hp > steinH();
	}

	public int bodenH()
	{
		return liftOben() ? steinH() + 1 : steinH();
	}

	@Override
	public int daraufH()
	{
		return hoehe;
	}

	@Override
	public int markH()
	{
		return bodenH();
	}

	public void gehen()
	{
		if(loescher)
			schalterR.items.forEach(Item::loescher);
		Item itemA = schalterR.items.get(schalterR.akItem);
		schalterR.items.removeIf(Item::weg);
		if(item != null)
		{
			schalterR.items.remove(item);
			schalterR.items.add(item.kopie(schalterR));
		}
		int nA = schalterR.items.indexOf(itemA);
		schalterR.akItem = nA >= 0 ? nA : 0;
		if(schalter != 'n')
			schalterR.farbeAktuell = schalter;
		if(dia && !benutzt)
		{
			benutzt = true;
			schalterR.dias++;
		}
		if(einhauwand >= 0)
			benutzt = true;
		if(eis)
			benutzt = true;
		if(ziel)
		{
			schalterR.setRichtung(3);
			schalterR.gewonnen = true;
		}
	}

	@Override
	public ArrayList<Render> addToRender(RenderCreater rc, boolean darauf, boolean preview)
	{
		rc.sh = bodenH();
		if(blockFarbe == 'n')
			rc.addgz("Höhe" + hoehe, hoehe);
		else if(schalterR.farbeAktuell == blockFarbe)
			rc.addgz("Höhe" + hoehe + blockFarbe, hoehe);
		else
		{
			rc.addgz("HöheI" + blockFarbe, hoehe);
			rc.addgz("Höhe" + sonstH, sonstH);
		}
		if(ziel)
		{
			rc.addw("Ziel2");
			rc.addw("Ziel1");
		}
		if(schalter != 'n')
			rc.addw("Schalter" + (schalterR.farbeAktuell == schalter ? "1" : "") + schalter);
		if(pfeil >= 0)
			rc.addw("Pfeil" + pfeil);
		if(einhauwand >= 0)
			if(benutzt)
				rc.addw("EinhauwandB" + einhauwand);
			else
				rc.addw("Einhauwand" + einhauwand);
		if(dia)
			rc.add(DiaRender.gib(0.1, 0.9, 4, (schalterR.tick % 100) / 100d, 0.8, new Color(0, 0, benutzt ? 0 : 200, 127), bodenH()));
		if(diaTuer > 0)
			if(diaTuer > schalterR.dias || preview)
			{
				rc.addw("DiaTür");
				rc.addt("  " + diaTuer, bodenH() + 1);
			}
			else
				rc.addw("DiaTürOffen");
		if(eis)
			rc.addw(benutzt ? "EisB" : "Eis");
		if(loescher)
			rc.addw("Löscher");
		if(enterstange >= 0)
			rc.addm("Stange" + (darauf ? "B" : ""), enterstange);
		if(lift)
			if(liftOben())
				rc.addm("LiftOben", steinH());
			else
				rc.addm("LiftUnten", steinH());
		if(item != null && !darauf)
			rc.addw(item.bildname());
		return rc.renders;
	}
}