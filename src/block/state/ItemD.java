package block.state;

import block.item.*;
import java.util.*;
import shift.*;

public class ItemD
{
	final int type;
	private final int[] data;

	public ItemD(int type, int... data)
	{
		this.type = type;
		if(type == 0)
			System.out.println(Arrays.toString(data));
		this.data = data;
	}

	public Item toItem()
	{
		switch(type)
		{
			case 1:
				return new Movement();
			case 2:
				return new Feuer(data[0], data[1]);
			case 3:
				return new Schalterpistole(data[0], data[1], data[2]);
			case 4:
				return new Sprungfeder(data[0], data[1], data[2]);
			case 5:
				return new Fluegel(data[0], data[1], data[2]);
			case 6:
				return new AerialEnterhaken(data[0], data[1], data[2], data[3] == 2);
			case 7:
				return new FahrenderPfeil(data[0], new D3C(data[1], data[2], data[3]));
			case 8:
				return new FahrendeEbene(new D3C(data[0], data[1], data[2]), new D3C(data[3], data[4], data[5]));
			default:
				throw new RuntimeException();
		}
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