package block;

import java.util.*;
import javax.swing.*;
import laderLC.*;

public class BlockLies
{
	LFeld[][] feld;
	final int[][] se = new int[2][2];
	int[] se2 = new int[]{0, 0};

	public ErrorVial lies(String build, boolean se2n)
	{
		ErrorVial vial = new ErrorVial();
		ArrayList<Integer> ends = new ArrayList<>();
		ArrayList<String> buildSpl = LC2.klaSplit2(vial.prep(build), false, 0, ends);
		Object[] werte = LC2.verifyTypes(buildSpl, 0, vial.end(), vial, LC2.TFV.KLAMMERN, LC2.TFV.KLAMMERN);
		readInSettings((String) werte[0], ends.get(0), vial);
		if(se2n)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("0 0 ").append(se[0][0]).append(' ').append(se[0][1]);
			Object neu = JOptionPane.showInputDialog(null, null, null, JOptionPane.QUESTION_MESSAGE, null, null, sb.toString());
			if(neu instanceof String)
			{
				String[] w = ((String) neu).split(" ");
				se2 = new int[]{0, 0, se[0][0], se[0][1]};
				for(int i = 0; i < 4; i++)
					try
					{
						se2[i] = Integer.parseInt(w[i]);
					}
					catch(Exception ignored){}
			}
		}
		if(se2.length > 2)
		{
			se[0][0] = se2[2] - se2[0];
			se[0][1] = se2[3] - se2[1];
		}
		se[1][0] = se[1][0] - se2[0];
		se[1][1] = se[1][1] - se2[1];
		feld = new LFeld[se[0][1]][];
		readInFeld((String) werte[1], ends.get(1), ends.get(2), vial);
		return vial;
	}

	private static final KXS forReadInSettings = new KXS(true, false, true, true, false);

	public void readInSettings(String build, int errStart, ErrorVial vial)
	{
		LC2.superwaguh(build, errStart, vial, forReadInSettings, new ArrayList(), this, "readInSettings2");
	}

	@SuppressWarnings("unused")
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
		ArrayList<LFeld[]> feld1 = new ArrayList<>();
		LC2.superwaguh(build, errStart, vial, forReadInFeld, this, "readInFeld2", feld1);
		if(se2.length <= 2 && feld1.size() != se[0][1])
			vial.add(new CError("Unpassende Anzahl Reihen: " + feld1.size() + " != " + se[0][1], errStart, errEnd));
		for(int i = 0; i < se[0][1]; i++)
			if(i + se2[1] >= 0 && i + se2[1] < feld1.size())
				feld[i] = feld1.get(i + se2[1]);
			else
				feld[i] = leerreihe(se[0][0]);
	}

	private static LFeld[] leerreihe(int len)
	{
		LFeld[] reihe = new LFeld[len];
		for(int i = 0; i < reihe.length; i++)
			reihe[i] = new LFeld();
		return reihe;
	}

	private static final KXS forReadInFeld2 = new KXS(false, false);

	@SuppressWarnings("unused")
	public void readInFeld2(String value, Integer errStart, Integer errEnd, ErrorVial vial, ArrayList<LFeld[]> feld1)
	{
		ArrayList<LFeld> feld2 = new ArrayList<>();
		LC2.superwaguh(value, errStart, vial, forReadInFeld2, this, "readInFeld3", feld2);
		if(se2.length <= 2 && feld2.size() != se[0][0])
			vial.add(new CError("Reihe mit unpassender Länge: " + feld2.size() + " != " + se[0][0], errStart, errEnd));
		LFeld[] reihe = new LFeld[se[0][0]];
		for(int i = 0; i < se[0][0]; i++)
			if(i + se2[0] >= 0 && i + se2[0] < feld2.size())
				reihe[i] = feld2.get(i + se2[0]);
			else
				reihe[i] = new LFeld();
		feld1.add(reihe);
	}

	@SuppressWarnings("unused")
	public void readInFeld3(String value, Integer errStart, Integer errEnd, ErrorVial vial, ArrayList<LFeld> feld2)
	{
		LFeld f = new LFeld();
		f.lies(value, errStart, errEnd, vial);
		feld2.add(f);
	}

	public String speichern(int xs, int ys)
	{
		se[1][0] = xs;
		se[1][1] = ys;
		return speichern();
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
				feld = new LFeld[ys.length][xs.length];
			}
			for(int yi = 0; yi < ys.length; yi++)
			{
				LFeld f = new LFeld();
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