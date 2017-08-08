package block.item;

import block.*;
import block.state.*;

public class Feuer extends Item
{
	public Feuer(){}

	public Feuer(int level, int priority)
	{
		super(level, priority);
	}

	@Override
	public Item kopie(SchalterR schalterR)
	{
		Feuer i1 = new Feuer(level, priority);
		i1.schalterR = schalterR;
		return i1;
	}

	@Override
	public String bildname()
	{
		return "Feuer";
	}

	@Override
	public ItemD saveState()
	{
		return new ItemD(1, level, priority);
	}
}