package block;

import block.item.*;
import javax.swing.*;
import laderLC.*;

public class LFeld
{
	public static final int maxenh = 12;

	public int hoehe;
	boolean ziel;
	public char blockFarbe = 'n';
	int sonstH = -1;
	public char schalter = 'n';
	int pfeil = -1;
	int einhauwand = -1;
	boolean dia = false;
	int diaTuer = 0;
	boolean eis;
	boolean loescher;
	public int enterstange = -1;
	public boolean lift;
	Item item;

	public void enhance(SchalterR mit, int wie)
	{
		switch(wie)
		{
			case 1:
				blockFarbe = mit.farbeAktuell;
				break;
			case 2:
				schalter = mit.farbeAktuell;
				break;
			case 3:
				if(blockFarbe != 'n')
				{
					if(sonstH >= 0)
						sonstH = -1;
					else
						sonstH = mit.hp;
				}
				break;
			case 4:
				pfeil = mit.richtung;
				break;
			case 5:
				einhauwand = (mit.richtung + 2) % 4;
				break;
			case 6:
				dia = true;
				break;
			case 7:
				if(mit.dias > 0)
					diaTuer = mit.dias;
				break;
			case 8:
				eis = true;
				break;
			case 9:
				loescher = true;
				break;
			case 10:
				enterstange = mit.hp;
				break;
			case 11:
				ziel = true;
				break;
			case 12:
				lift = true;
				break;
		}
	}

	public void liesDirekt(String build)
	{
		ErrorVial vial = new ErrorVial();
		build = vial.prep(build);
		lies(build, 0, vial.end(), vial);
		if(vial.errors())
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

	@SuppressWarnings("unused")
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
				case "lift":
					lift = true;
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
				item.lies(value, errStart, vial);
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
		if(lift)
			speichernZ(sb, "lift", null);
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
						item = new AerialEnterhaken(1, Integer.parseInt(build.substring(11, build.length() - 1)), false);
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