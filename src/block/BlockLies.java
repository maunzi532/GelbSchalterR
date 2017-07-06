package block;

import laderLC.*;
import java.util.*;

public class BlockLies
{
	BFeld[][] feld;
	int[][] se = new int[2][2];

	public ErrorVial lies(String build)
	{
		ErrorVial vial = new ErrorVial();
		ArrayList<Integer> ends = new ArrayList<>();
		ArrayList<String> buildSpl = LC2.klaSplit2(vial.prep(build), false, 0, ends);
		Object[] werte = LC2.verifyTypes(buildSpl, 0, vial.end(), vial, LC2.TFV.KLAMMERN, LC2.TFV.KLAMMERN);
		readInSettings((String) werte[0], ends.get(0), ends.get(1), vial);
		readInFeld((String) werte[1], ends.get(1), ends.get(2), vial);
		return vial;
	}

	private static final KXS forReadInSettings = new KXS(true, false, true, true, false);

	public void readInSettings(String build, int errStart, int errEnd, ErrorVial vial)
	{
		LC2.superwaguh(build, errStart, vial, forReadInSettings, new ArrayList(), this, "readInSettings2");
	}

	public void readInSettings2(String value, Integer errStart, Integer errEnd, ErrorVial vial, String textKey)
	{
		int setN = -1;
		switch(textKey)
		{
			case "w":
				setN = 0;
				break;
			case "s":
				setN = 1;
				break;
			default:
				vial.add(new CError("Setting " + textKey + " not recognized", errStart, errEnd));
				break;
		}
		if(setN >= 0)
		{
			ArrayList<Integer> ends = new ArrayList<>();
			ArrayList<String> buildSpl = LC2.klaSplit2(value, false, errStart, ends);
			Object[] werte1S = LC2.verifyTypes(buildSpl, ends.get(0), ends.get(ends.size() - 1), vial, LC2.TFV.UINT, LC2.TFV.UINT);
			for(int i = 0; i < 2; i++)
				se[setN][i] = (Integer) werte1S[i];
		}
	}

	private static final KXS forReadInFeld = new KXS(true, false);

	public void readInFeld(String build, int errStart, int errEnd, ErrorVial vial)
	{
		ArrayList<BFeld[]> feld1 = new ArrayList<>();
		LC2.superwaguh(build, errStart, vial, forReadInFeld, this, "readInFeld2", feld1);
		if(feld1.size() != se[0][1])
			vial.add(new CError("Unpassende Anzahl Reihen: " + feld1.size() + " != " + se[0][1], errStart, errEnd));
		feld = feld1.toArray(new BFeld[feld1.size()][]);
	}

	private static final KXS forReadInFeld2 = new KXS(false, false);

	public void readInFeld2(String value, Integer errStart, Integer errEnd, ErrorVial vial, ArrayList<BFeld[]> feld1)
	{
		ArrayList<BFeld> feld2 = new ArrayList<>();
		LC2.superwaguh(value, errStart, vial, forReadInFeld2, this, "readInFeld3", feld2);
		if(feld2.size() == se[0][0])
			feld1.add(feld2.toArray(new BFeld[feld2.size()]));
		else
			vial.add(new CError("Reihe mit unpassender Länge: " + feld2.size() + " != " + se[0][0], errStart, errEnd));
	}

	public void readInFeld3(String value, Integer errStart, Integer errEnd, ErrorVial vial, ArrayList<BFeld> feld2)
	{
		BFeld f = new BFeld();
		f.lies(value, errStart, errEnd, vial);
		feld2.add(f);
	}

	public String speichern()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		sb.append("\tw = ").append(se[0][0]).append(", ").append(se[0][1]).append(";\n");
		sb.append("\ts = ").append(se[1][0]).append(", ").append(se[1][1]).append(";\n");
		sb.append("},\n{");
		for(int iy = 0; iy < feld.length; iy++)
		{
			sb.append('\t');
			for(int ix = 0; ix < feld[iy].length; ix++)
			{
				if(ix > 0)
					sb.append(", ");
				sb.append(feld[iy][ix].speichern());
			}
			sb.append(";\n");
		}
		sb.append('}');
		return sb.toString();
	}

	public void liesA(String build)
	{
		String[] xs = build.split("\n");
		for(int xi = 0; xi < xs.length; xi++)
		{
			String[] ys = xs[xi].split(",");
			if(feld == null)
			{
				se[0][0] = xs.length;
				se[0][1] = ys.length;
				feld = new BFeld[ys.length][xs.length];
			}
			for(int yi = 0; yi < ys.length; yi++)
			{
				BFeld f = new BFeld();
				f.liesA(ys[yi]);
				feld[yi][xi] = f;
				if(ys[yi].equals("a"))
				{
					se[1][0] = xi;
					se[1][1] = yi;
				}
			}
		}
	}
}