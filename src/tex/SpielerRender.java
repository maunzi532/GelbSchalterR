package tex;

import java.awt.*;
import java.util.*;

public class SpielerRender extends Render3
{
	private static final Color w = Color.BLACK;
	private static final Color w1 = Color.RED;

	private SpielerRender(int height, double minh, double maxh, ArrayList<R3t> teile)
	{
		super(height, true);
		this.minh = minh;
		this.maxh = maxh;
		this.teile = teile;
	}

	public static SpielerRender gib(double dreh, double z, double xs, double ys, double deep)
	{
		double maxh = 1.8;
		double h1 = 1.4;
		double h2 = 1.3;
		double h3 = 0.8;
		double kw1 = 0.35;
		double nw1 = 0.2;
		double nb1 = 0.4;
		double fa1 = 0.2;
		double hb1 = 0.7;
		double hb2 = 0.9;
		double hb3 = 0.5;
		int cs = 46;
		int ct1 = 4;
		int cs2a = cs + ct1;
		int cs2b = cs + ct1 * 3 + 1;
		double cwb = 2.2;
		double ca = 0.8;

		R3p[] punkte = new R3p[cs2b + ct1 + 1];
		ArrayList<R3t> teile1 = new ArrayList<>();

		setzeQP(punkte, 0, kw1, kw1, maxh);
		setzeQP(punkte, 4, kw1, kw1, h1);

		setzeQP(punkte, 8, nb1, nw1, h2);
		setzeQP(punkte, 12, nb1, nw1, h3);

		punkte[16] = new R3p(0, nw1, h3);
		punkte[17] = new R3p(0, -nw1, h3);
		setzeQP(punkte, 18, nb1 + fa1, nw1, 0);
		setzeQP(punkte, 22, fa1, nw1, 0);

		setzeQP(punkte, 26, hb1, nw1, h3);
		setzeQP(punkte, 30, hb2, nw1, h3);
		setzeQP(punkte, 34, hb3, nw1, h2);

		//dreh(punkte, new R3p(-hb3, 0, h2), 0.02, 0.4, 26, 28, 30, 32);
		//dreh(punkte, new R3p(hb3, 0, h2), -0.02, 0.4, 27, 29, 31, 33);

		for(int i = 0; i < 8; i++)
			punkte[38 + i] = new R3p((i % 4 > 1 ? 0.3 : 0.15) * (i / 4 * 2 - 1), -kw1, maxh - (i % 2 == 0 ? 0.2 : 0.1));

		for(int i = -ct1; i <= ct1; i++)
			punkte[cs2a + i] = new R3p(i * hb3 / ct1, nw1, h2);
		for(int i = -ct1; i <= ct1; i++)
			punkte[cs2b + i] = new R3p(i * hb3 / ct1 * cwb, ca, 0);

		for(int i = 0; i < punkte.length; i++)
			punkte[i] = R3p.shift(R3p.endDreh(dreh, punkte[i]), xs, ys, z - (int) z - deep);

		r4(teile1, w, punkte[16], punkte[17], punkte[12], punkte[14], punkte[22], punkte[24], punkte[18], punkte[20]);
		r4(teile1, w, punkte[16], punkte[17], punkte[13], punkte[15], punkte[23], punkte[25], punkte[19], punkte[21]);

		r4(teile1, w, Arrays.copyOfRange(punkte, 8, 16));

		for(int i = -ct1; i < ct1; i++)
			if(i >= 0)
				q(teile1, w1, punkte[cs2a + i + 1], punkte[cs2a + i], punkte[cs2b + i + 1], punkte[cs2b + i]);
			else
				q(teile1, w1, punkte[cs2a + i], punkte[cs2a + i + 1], punkte[cs2b + i], punkte[cs2b + i + 1]);

		q(teile1, w, punkte[26], punkte[28], punkte[30], punkte[32]);
		q(teile1, w, punkte[27], punkte[29], punkte[31], punkte[33]);
		r4(teile1, w, punkte[8], punkte[10], punkte[34], punkte[36], punkte[26], punkte[28], punkte[30], punkte[32]);
		r4(teile1, w, punkte[9], punkte[11], punkte[35], punkte[37], punkte[27], punkte[29], punkte[31], punkte[33]);
		teile1.add(new R3t(true, w, punkte[34], punkte[35], punkte[37], punkte[36]));

		r4(teile1, w, Arrays.copyOfRange(punkte, 0, 8));

		q(teile1, w1, punkte[38], punkte[39], punkte[40], punkte[41]);
		q(teile1, w1, punkte[42], punkte[43], punkte[44], punkte[45]);

		teile1.add(new R3t(true, w, punkte[0], punkte[1], punkte[3], punkte[2]));

		return new SpielerRender((int) z, 0, maxh, teile1);
	}

	private static void q(ArrayList<R3t> teile1, Color f, R3p... ecken)
	{
		teile1.add(new R3t(false, f, ecken[0], ecken[1], ecken[2]));
		teile1.add(new R3t(false, f, ecken[1], ecken[2], ecken[3]));
	}

	private static final int[] rf = new int[]{0, 1, 3, 2, 0};
	private static void r4(ArrayList<R3t> teile1, Color f, R3p... ecken)
	{
		for(int i = 0; i < 4; i++)
			q(teile1, f, ecken[rf[i]], ecken[rf[i + 1]], ecken[rf[i] + 4], ecken[rf[i + 1] + 4]);
	}

	private static void setzeQP(R3p[] punkte, int k, double x, double y, double z)
	{
		punkte[k] = new R3p(x, y, z);
		punkte[k + 1] = new R3p(-x, y, z);
		punkte[k + 2] = new R3p(x, -y, z);
		punkte[k + 3] = new R3p(-x, -y, z);
	}

	private static void dreh(R3p[] punkte, R3p start, double wl, double wb, int... wo)
	{
		for(int i = 0; i < wo.length; i++)
			punkte[wo[i]] = R3p.dreh(start, punkte[wo[i]], wl, wb);
	}
}