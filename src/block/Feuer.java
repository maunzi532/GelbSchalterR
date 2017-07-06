package block;

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
}