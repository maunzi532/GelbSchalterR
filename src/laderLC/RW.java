package laderLC;

import java.util.*;

public class RW
{
	private String name;
	private double zahl;

	RW(String text)
	{
		try
		{
			zahl = Double.parseDouble(text);
		}
		catch(NumberFormatException e)
		{
			name = text;
		}
	}

	RW(double zahl)
	{
		this.zahl = zahl;
	}

	public double gib(HashMap<String, Double> map)
	{
		if(name != null)
			if(map.containsKey(name))
				return map.get(name);
			else
				return 1;
		return zahl;
	}
}