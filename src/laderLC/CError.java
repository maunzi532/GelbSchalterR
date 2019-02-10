package laderLC;

public class CError
{
	public final String message;
	public final int areaStart;
	public final int areaEnd;

	public CError(String message, int areaStart, int areaEnd)
	{
		this.message = message;
		this.areaStart = areaStart;
		this.areaEnd = areaEnd;
	}

	@Override
	public String toString()
	{
		return '\'' + message + '\'' +
				", zwischen " + areaStart +
				" und " + areaEnd;
	}
}