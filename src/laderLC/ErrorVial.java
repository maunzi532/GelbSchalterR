package laderLC;

import java.util.*;

public class ErrorVial
{
	public ArrayList<CError> errors;
	ArrayList<Integer> areaEnds;
	String buildX;

	public ErrorVial()
	{
		errors = new ArrayList<>();
	}

	public String prep(String build)
	{
		buildX = build;
		build = LC2.decomment(build);
		areaEnds = LC2.areaEnds(build);
		return build;
	}

	public int end()
	{
		return areaEnds.size();
	}

	public void add(CError error)
	{
		errors.add(error);
	}

	public boolean errors()
	{
		return !errors.isEmpty();
	}

	public int[] mark(CError e, int xm)
	{
		int[] toR = new int[2];
		toR[0] = areaEnds.get(e.areaStart) + 1;
		if(e.areaEnd >= areaEnds.size())
			toR[1] = areaEnds.get(areaEnds.size() - 1);
		else
			toR[1] = areaEnds.get(e.areaEnd);
		if(toR[0] < 0)
			toR[0] = 0;
		if(toR[1] > xm)
			toR[1] = xm;
		return toR;
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for(CError err : errors)
		{
			sb.append(err.message).append('\n');
			int anf = err.areaStart > 5 ? err.areaStart - 5 : 0;
			int end = buildX.length() - err.areaEnd > 5 ? err.areaEnd + 5 : buildX.length();
			sb.append(" Ort: ").append(buildX.substring(anf, end).replace("\n", "\\n")).append('\n');
		}
		return sb.toString();
	}
}