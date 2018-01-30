package block.item;

import block.*;
import block.state.*;

public class Wuerfel extends Item
{
	char farbe;

	public Wuerfel(char farbe)
	{
		id = 9;
		this.farbe = farbe;
	}

	@Override
	public Item kopie(SchalterR schalterR)
	{
		Wuerfel i1 = new Wuerfel(farbe);
		i1.schalterR = schalterR;
		return i1;
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof Wuerfel && farbe == ((Wuerfel) o).farbe;
	}

	@Override
	public String bildname()
	{
		return "Würfel";
	}

	@Override
	public ItemD saveState()
	{
		return new ItemD(id, farbe);
	}
}