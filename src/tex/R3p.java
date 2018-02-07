package tex;

public class R3p
{
	final double[] n;

	public R3p(double x, double y, double h)
	{
		n = new double[]{x, y, h};
	}

	public static R3p endDreh(double dreh, R3p original)
	{
		double dreh2 = dreh * Math.PI * 2;
		return new R3p(Math.cos(dreh2) / 2 * original.n[1] - Math.sin(dreh2) / 2 * original.n[0] + 0.5,
				Math.sin(dreh2) / 2 * original.n[1] + Math.cos(dreh2) / 2 * original.n[0] + 0.5, original.n[2]);
	}

	public static R3p shift(R3p original, double xs, double ys, double zs)
	{
		return new R3p(original.n[0] + xs, original.n[1] + ys, original.n[2] + zs);
	}

	public static R3p dreh(R3p start, R3p transf, double wl, double wb)
	{
		wl *= Math.PI;
		wb *= Math.PI;
		double[] t1 = new double[3];
		for(int i = 0; i < 3; i++)
			t1[i] = transf.n[i] - start.n[i];
		double[] t2 = new double[3];
		t2[0] = t1[0];
		t2[1] = Math.sin(wb) * t1[2] + Math.cos(wb) * t1[1];
		t2[2] = Math.cos(wb) * t1[2] - Math.sin(wb) * t1[1];
		double[] t3 = new double[3];
		t3[0] = Math.sin(wl) * t2[1] + Math.cos(wl) * t2[0];
		t3[1] = Math.cos(wl) * t2[1] - Math.sin(wl) * t2[0];
		t3[2] = t2[2];
		return new R3p(t3[0] + start.n[0], t3[1] + start.n[1], t3[2] + start.n[2]);
	}
}