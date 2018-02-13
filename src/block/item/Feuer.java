package block.item;

import block.*;
import block.state.*;

public class Feuer extends Item
{
	public Feuer()
	{
		id = 2;
	}

	public Feuer(int level, int priority)
	{
		super(level, priority);
		id = 2;
	}

	@Override
	public Item kopie(SchalterR schalterR)
	{
		Feuer i1 = new Feuer(level, priority);
		i1.schalterR = schalterR;
		return i1;
	}

	@Override
	public String edgeText(int edge)
	{
		if(edge == 0 && level >= 0)
			return String.valueOf(level);
		return super.edgeText(edge);
	}

	@Override
	public String bildname()
	{
		return "Feuer";
	}

	@Override
	public ItemD saveState()
	{
		return new ItemD(id, level, priority);
	}
}