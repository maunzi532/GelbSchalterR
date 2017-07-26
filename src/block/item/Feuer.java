package block.item;

import block.*;
import block.state.*;

public class Feuer extends Item
{
	public Feuer(){}

	public Feuer(int level)
	{
		super(level);
	}

	@Override
	public Item kopie(BlockLab blockLab)
	{
		Feuer i1 = new Feuer(level);
		i1.blockLab = blockLab;
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
		return new ItemD(1, level);
	}
}