package block.item;

import laderLC.*;

public abstract class LaengeItem extends Item
{
	int laenge;

	LaengeItem(){}

	LaengeItem(int level, int priority, int laenge)
	{
		super(level, priority);
		this.laenge = laenge;
	}

	@Override
	public boolean benutze(int num, boolean cl, boolean lvm)
	{
		return super.benutze(num, cl, true);
	}

	@Override
	public String edgeText(int edge)
	{
		if(edge == 2)
			return String.valueOf(laenge);
		return super.edgeText(edge);
	}

	@Override
	public void lies2(String value, Integer errStart, Integer errEnd, ErrorVial vial, String textKey)
	{
		try
		{
			if(textKey.toLowerCase().equals("level"))
				level = Integer.parseInt(value);
			else if(textKey.toLowerCase().equals("priorität"))
				priority = Integer.parseInt(value);
			else if(textKey.toLowerCase().equals("länge"))
				laenge = Integer.parseInt(value);
			else
				vial.add(new CError("Unbekannter Wert: " + textKey, errStart, errEnd));
		}catch(Exception e)
		{
			vial.add(new CError("Invalides Setzen eines Werts", errStart, errEnd));
		}
	}

	@Override
	public void speichern(StringBuilder sb)
	{
		super.speichern(sb);
		speichernZ(sb, "länge", String.valueOf(laenge));
	}
}