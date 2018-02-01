package block.item;

import block.*;
import block.state.*;
import shift.*;

public class Wuerfel extends Item
{
	public char farbe;
	private D3C ort;

	public Wuerfel(char farbe)
	{
		id = 9 + (farbe - 'A');
		this.farbe = farbe;
	}

	public Wuerfel(char farbe, D3C ort)
	{
		id = 9 + (farbe - 'A');
		this.farbe = farbe;
		this.ort = ort;
	}

	@Override
	public Item kopie(SchalterR schalterR)
	{
		Wuerfel i1 = new Wuerfel(farbe, ort);
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