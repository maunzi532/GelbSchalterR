package tex;

public class R3p
{
	final double[] n;

	public R3p(double x, double y, double h)
	{
		n = new double[]{x, y, h};
	}

	public static R3p dreh(double dreh, R3p original)
	{
		return dreh(dreh, original, 0);
	}

	public static R3p dreh(double dreh, R3p original, int inv)
	{
		double dreh2 = dreh * Math.PI * 2;
		return new R3p(Math.cos(dreh2) / 2 * original.n[1 - inv] - Math.sin(dreh2) / 2 * original.n[inv] + 0.5,
				Math.sin(dreh2) / 2 * original.n[1 - inv] + Math.cos(dreh2) / 2 * original.n[inv] + 0.5, original.n[2]);
	}
}