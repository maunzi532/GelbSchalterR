package laderLC;

import java.util.*;

public class R2
{
	private static List<Character> rechen = Arrays.asList('+', '-', '*', '/', '%');

	private ArrayList<RW> zahlies = new ArrayList<>();
	private ArrayList<Integer> zeichens = new ArrayList<>();
	boolean deg2rad;

	public R2(double zahl)
	{
		zahlies.add(new RW(zahl));
	}

	public R2(String build, boolean deg2rad, int errStart, int errEnd, ErrorVial vial)
	{
		this.deg2rad = deg2rad;
		String[] tex2 = build.split("_");
		int len = tex2.length;
		assert len > 0;
		if(tex2.length % 2 != 1)
		{
			len--;
			vial.add(new CError("R2 part count % 2 != 1", errStart, errEnd));
		}
		for(int i = 0; i < len; i++)
			if(i % 2 == 0)
				zahlies.add(new RW(tex2[i]));
			else
			{
				if(tex2[i].length() != 1)
					vial.add(new CError("Part " + i + " must be single char", errStart, errEnd));
				int index = rechen.indexOf(tex2[i].charAt(0));
				if(index < 0 || index >= rechen.size())
				{
					vial.add(new CError("Part " + i + " must be one of +-*/%", errStart, errEnd));
					index = 0;
				}
				zeichens.add(index);
			}
	}

	public R2(R2 copy)
	{
		zahlies = new ArrayList<>(copy.zahlies);
		zeichens = new ArrayList<>(copy.zeichens);
		deg2rad = copy.deg2rad;
	}

	public R2 append(int operator, double num)
	{
		zeichens.add(operator);
		zahlies.add(new RW(num));
		return this;
	}

	public double wert(HashMap<String, Double> map)
	{
		double wert = zahlies.get(0).gib(map);
		for(int i = 0; i < zeichens.size(); i++)
		{
			double w2 = zahlies.get(i + 1).gib(map);
			switch(zeichens.get(i))
			{
				case 0:
					wert = wert + w2;
					break;
				case 1:
					wert = wert - w2;
					break;
				case 2:
					wert = wert * w2;
					break;
				case 3:
					wert = wert / w2;
					break;
				case 4:
					wert = wert % w2;
					break;
			}
		}
		if(deg2rad)
			wert *= Math.PI / 180;
		return wert;
	}
}