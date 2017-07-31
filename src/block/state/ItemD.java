package block.state;

import block.item.*;
import java.util.*;

public class ItemD implements Comparable<ItemD>
{
	private final int type;
	private final int[] data;

	public ItemD(int type, int... data)
	{
		this.type = type;
		this.data = data;
	}

	public Item toItem()
	{
		switch(type)
		{
			case 0:
				return new Movement();
			case 1:
				return new Feuer(data[0]);
			case 2:
				return new AerialEnterhaken(data[0], data[1], data[2] == 2);
			case 3:
				return new Schalterpistole(data[0], data[1]);
			case 4:
				return new Sprungfeder(data[0], data[1]);
			case 5:
				return new Fluegel(data[0], data[1]);
			default:
				throw new RuntimeException();
		}
	}

	@Override
	public int compareTo(ItemD itemD)
	{
		return type - itemD.type;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof ItemD)) return false;
		ItemD itemD = (ItemD) o;
		return type == itemD.type && Arrays.equals(data, itemD.data);
	}

	@Override
	public int hashCode()
	{
		int result = type;
		result = 31 * result + Arrays.hashCode(data);
		return result;
	}
}