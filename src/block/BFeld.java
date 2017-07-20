package block;

import area.*;
import javax.swing.*;
import laderLC.*;

public class BFeld extends Feld
{
	BlockLab blockLab;
	boolean ziel;
	char blockFarbe = 'n';
	int sonstH = -1;
	char schalter = 'n';
	int pfeil = -1;
	int einhauwand = -1;
	boolean dia = false;
	int diaTuer = 0;
	boolean eis;
	boolean loescher;
	int enterstange = -1;
	Item item;

	boolean benutzt = false;

	public BFeld(){}

	public BFeld copy(BlockLab blockLab)
	{
		BFeld copy = new BFeld();
		copy.ziel = ziel;
		copy.blockLab = blockLab;
		copy.hoehe = hoehe;
		copy.blockFarbe = blockFarbe;
		copy.sonstH = sonstH;
		copy.schalter = schalter;
		copy.pfeil = pfeil;
		copy.einhauwand = einhauwand;
		copy.dia = dia;
		copy.diaTuer = diaTuer;
		copy.eis = eis;
		copy.loescher = loescher;
		copy.enterstange = enterstange;
		copy.item = item;
		return copy;
	}

	public int getH()
	{
		if(blockFarbe != 'n' && blockFarbe != blockLab.farbeAktuell)
			return sonstH >= 0 ? sonstH : -1;
		return hoehe;
	}

	public Integer getH(int side, boolean enter)
	{
		if(enter)
		{
			if(einhauwand >= 0 && !benutzt && einhauwand != side)
				return null;
			if(pfeil >= 0 && pfeil == side)
				return null;
			if(diaTuer > blockLab.dias)
				return null;
			if(eis && !benutzt && !blockLab.items.contains(new Feuer()))
				return null;
		}
		else
		{
			if(pfeil >= 0 && pfeil != side)
				return null;
		}
		if(blockFarbe != 'n' && blockFarbe != blockLab.farbeAktuell)
			return sonstH >= 0 ? sonstH : null;
		return hoehe;
	}

	public int getAH()
	{
		int h1 = hoehe;
		if(blockFarbe != 'n' && blockFarbe != blockLab.farbeAktuell)
			h1 = sonstH >= 0 ? sonstH : 0;
		if((einhauwand >= 0 && !benutzt) || diaTuer > 0 || (eis && !benutzt))
			return h1 + 1;
		return h1;
	}

	public void gehen()
	{
		if(loescher)
			blockLab.items.forEach(Item::loescher);
		Item itemA = blockLab.items.get(blockLab.akItem);
		blockLab.items.removeIf(Item::weg);
		if(item != null)
		{
			blockLab.items.remove(item);
			blockLab.items.add(item.kopie(blockLab));
		}
		int nA = blockLab.items.indexOf(itemA);
		blockLab.akItem = nA >= 0 ? nA : 0;
		if(schalter != 'n')
			blockLab.farbeAktuell = schalter;
		if(dia && !benutzt)
		{
			benutzt = true;
			blockLab.dias++;
		}
		if(einhauwand >= 0)
			benutzt = true;
		if(eis)
			benutzt = true;
		if(ziel)
			blockLab.gewonnen = true;
	}

	@Override
	public void addToRender(Area area, boolean darauf)
	{
		if(hoehe <= 0)
			return;
		StringBuilder sb = new StringBuilder();
		sb.append("Höhe").append(hoehe);
		if(blockFarbe != 'n')
		{
			if(((BlockLab) area).farbeAktuell != blockFarbe)
			{
				sb.append('I');
				if(sonstH >= 0)
					area.addw("Höhe" + sonstH);
			}
			sb.append(blockFarbe);
		}
		area.addw(sb.toString());
		if(ziel)
			area.addw("Ziel");
		if(schalter != 'n')
			area.addw("Schalter" + (blockLab.farbeAktuell == schalter ? "1" : "") + schalter);
		if(pfeil >= 0)
			area.addw("Pfeil" + pfeil);
		if(einhauwand >= 0)
			if(benutzt)
				area.addw("EinhauwandB" + einhauwand);
			else
				area.addw("Einhauwand" + einhauwand);
		if(dia)
			if(benutzt)
				area.addw("DiaB");
			else
				//area.addw("Dia");
				area.add3(new DiaRender());
		if(diaTuer > 0)
			if(diaTuer > blockLab.dias)
				area.addw("DiaTür", "  " + diaTuer);
			else
				area.addw("DiaTürOffen");
		if(eis)
			if(benutzt)
				area.addw("EisB");
			else
				area.addw("Eis");
		if(loescher)
			area.addw("Löscher");
		if(enterstange >= 0)
			area.addm("Stange" + (darauf ? "B" : ""), enterstange);
		if(item != null && !darauf)
			area.addw(item.bildname());
	}

	public void liesDirekt(String build)
	{
		ErrorVial vial = new ErrorVial();
		build = vial.prep(build);
		lies(build, 0, vial.end(), vial);
		if(!vial.worked())
			JOptionPane.showMessageDialog(null, vial.toString(), null, JOptionPane.ERROR_MESSAGE);
	}

	private static final KXS IKL = new KXS(true, false, true, true, false);

	public void lies(String build, int errStart, int errEnd, ErrorVial vial)
	{
		build = LC2.removeKlammernVllt(build);
		if(build.contains(";"))
			LC2.superwaguh(build, errStart, vial, IKL, this, "lies2");
		else try
		{
			hoehe = Integer.parseInt(build);
		}
		catch(NumberFormatException e)
		{
			vial.add(new CError("Keine ; bzw. keine Zahl", errStart, errEnd));
		}
	}

	public void lies2(String value, Integer errStart, Integer errEnd, ErrorVial vial, String textKey)
	{
		try
		{
			switch(textKey.toLowerCase())
			{
				case "h":
				case "höhe":
				case "hoehe":
					hoehe = Integer.parseInt(value);
					break;
				case "ziel":
					ziel = true;
					break;
				case "farbe":
					blockFarbe = Character.toUpperCase(value.charAt(0));
					break;
				case "sonsth":
					sonstH = Integer.parseInt(value);
					break;
				case "schalter":
					schalter = Character.toUpperCase(value.charAt(0));
					break;
				case "pfeil":
					pfeil = Integer.parseInt(value);
					break;
				case "einhauwand":
					einhauwand = Integer.parseInt(value);
					break;
				case "dia":
					dia = true;
					break;
				case "diatür":
					diaTuer = Integer.parseInt(value);
					break;
				case "eis":
					eis = true;
					break;
				case "löscher":
					loescher = true;
					break;
				case "stange":
					enterstange = value == null ? hoehe : Integer.parseInt(value);
					break;
				case "itemfeuer":
					item = new Feuer();
					break;
				case "itementerhaken":
					item = new AerialEnterhaken();
					break;
				case "itemschalterpistole":
					item = new Schalterpistole();
					break;
				case "itemflügel":
					item = new Fluegel();
					break;
				case "itemsprungfeder":
					item = new Sprungfeder();
					break;
				default:
					vial.add(new CError("Unbekannter Wert: " + textKey, errStart, errEnd));
			}
			if(textKey.toLowerCase().startsWith("item"))
				item.lies(value, errStart, errEnd, vial);
		}catch(Exception e)
		{
			vial.add(new CError("Invalides Setzen eines Werts", errStart, errEnd));
		}
	}

	public String speichern()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("{h = ").append(hoehe).append(';');
		if(ziel)
			speichernZ(sb, "ziel", null);
		if(blockFarbe != 'n')
			speichernZ(sb, "farbe", String.valueOf(blockFarbe));
		if(sonstH >= 0)
			speichernZ(sb, "sonsth", String.valueOf(sonstH));
		if(schalter != 'n')
			speichernZ(sb, "schalter", String.valueOf(schalter));
		if(pfeil >= 0)
			speichernZ(sb, "pfeil", String.valueOf(pfeil));
		if(einhauwand >= 0)
			speichernZ(sb, "einhauwand", String.valueOf(einhauwand));
		if(dia)
			speichernZ(sb, "dia", null);
		if(diaTuer > 0)
			speichernZ(sb, "diatür", String.valueOf(diaTuer));
		if(eis)
			speichernZ(sb, "eis", null);
		if(loescher)
			speichernZ(sb, "löscher", null);
		if(enterstange >= 0)
			speichernZ(sb, "stange", enterstange == hoehe ? null : String.valueOf(enterstange));
		if(item != null)
		{
			sb.append(" item").append(item.speichername()).append(" = {");
			item.speichern(sb);
			sb.append("};");
		}
		if(sb.indexOf("; ") >= 0)
		{
			sb.append('}');
			return sb.toString();
		}
		return String.valueOf(hoehe);
	}

	public void speichernZ(StringBuilder sb, String key, String value)
	{
		sb.append(' ').append(key);
		if(value != null)
			sb.append(" = ").append(value);
		sb.append(';');
	}

	public void liesA(String build)
	{
		hoehe = 1;
		if(build.length() >= 5)
			switch(build.toLowerCase())
			{
				case "feuer":
					item = new Feuer(1);
					break;
				case "eiszapfen":
					eis = true;
					break;
				case "loschboden":
					loescher = true;
					break;
				case "stange":
					enterstange = 1;
					break;
				case "sprung":
					item = new Sprungfeder(1, 2);
					break;
				default:
					if(build.toLowerCase().startsWith("enterhaken"))
						item = new AerialEnterhaken(1, false, Integer.parseInt(build.substring(11, build.length() - 1)));
					if(build.toLowerCase().startsWith("schalterkanone"))
						item = new Schalterpistole(1, Integer.parseInt(build.substring(15, build.length() - 1)));
			}
		else
			switch(build.charAt(0))
			{
				case ' ':
					hoehe = 0;
					break;
				case '+':
					break;
				case 'z':
					ziel = true;
					break;
				case 'w':
					if(build.length() == 1)
						hoehe = 2;
					else switch(build.charAt(1))
					{
						case 'l':
							einhauwand = 2;
							break;
						case 'o':
							einhauwand = 3;
							break;
						case 'r':
							einhauwand = 0;
							break;
						case 'u':
							einhauwand = 1;
							break;
						default:
							if(build.charAt(1) >= 'A' && build.charAt(1) <= 'F')
							{
								blockFarbe = build.charAt(1);
								sonstH = 1;
							}
					}
					break;
				case 's':
					schalter = build.charAt(1);
					break;
				case 'd':
					dia = true;
					break;
				case 'l':
					pfeil = 0;
					break;
				case 'o':
					pfeil = 1;
					break;
				case 'r':
					pfeil = 2;
					break;
				case 'u':
					pfeil = 3;
					break;
				default:
					if(build.charAt(0) >= 'A' && build.charAt(0) <= 'F')
						blockFarbe = build.charAt(0);
					if(build.charAt(0) >= '1' && build.charAt(0) <= '9')
						diaTuer = build.charAt(0) - '0';
			}
	}
}