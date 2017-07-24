package area;

import java.awt.*;
import java.util.*;
import tex.*;

public class SpielerRender extends Render3
{
	public static Color w = Color.BLACK;
	public static Color w1 = Color.RED;

	public SpielerRender(int height, double minh, double maxh, ArrayList<R3t> teile)
	{
		super(height, true);
		this.minh = minh;
		this.maxh = maxh;
		this.teile = teile;
	}

	public static SpielerRender gib(double dreh, int hoeheA)
	{
		ArrayList<R3t> teile1 = new ArrayList<>();
		R3p[] punkte = new R3p[46];
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
		for(int i = 0; i < 4; i++)
			punkte[i] = R3p.dreh(dreh + i / 4d, new R3p(-kw1, kw1, maxh));
		for(int i = 0; i < 4; i++)
			punkte[i + 4] = R3p.dreh(dreh + i / 4d, new R3p(-kw1, kw1, h1));

		for(int i = 0; i < 4; i++)
			punkte[i + 8] = R3p.dreh(dreh + i / 4d, new R3p(-nb1, nw1, h2), i % 2);
		for(int i = 0; i < 4; i++)
			punkte[i + 12] = R3p.dreh(dreh + i / 4d, new R3p(-nb1, nw1, h3), i % 2);

		for(int i = 0; i < 2; i++)
			punkte[i + 16] = R3p.dreh(dreh + i / 2d, new R3p(0, nw1, h3));
		for(int i = 0; i < 4; i++)
			punkte[i + 18] = R3p.dreh(dreh + i / 4d, new R3p(-nb1 - fa1, nw1, 0), i % 2);
		for(int i = 0; i < 4; i++)
			punkte[i + 22] = R3p.dreh(dreh + i / 4d, new R3p(-fa1, nw1, 0), i % 2);

		for(int i = 0; i < 4; i++)
			punkte[i + 26] = R3p.dreh(dreh + i / 4d, new R3p(-hb1, nw1, h3), i % 2);
		for(int i = 0; i < 4; i++)
			punkte[i + 30] = R3p.dreh(dreh + i / 4d, new R3p(-hb2, nw1, h3), i % 2);
		for(int i = 0; i < 4; i++)
			punkte[i + 34] = R3p.dreh(dreh + i / 4d, new R3p(-hb3, nw1, h2), i % 2);

		for(int i = 0; i < 8; i++)
			punkte[38 + i] = R3p.dreh(dreh, new R3p((i % 4 > 1 ? 0.3 : 0.15) * (i / 4 * 2 - 1), -kw1, maxh - (i % 2 == 0 ? 0.2 : 0.1)));

		r4(teile1, w, punkte[16], punkte[17], punkte[12], punkte[13], punkte[22], punkte[23], punkte[18], punkte[19]);
		r4(teile1, w, punkte[17], punkte[16], punkte[14], punkte[15], punkte[24], punkte[25], punkte[20], punkte[21]);

		for(int i = 0; i < 4; i++)
			q(teile1, w, punkte[i + 8], punkte[(i + 1) % 4 + 8], punkte[i + 12], punkte[(i + 1) % 4 + 12]);

		r4(teile1, w, punkte[8], punkte[9], punkte[34], punkte[35], punkte[26], punkte[27], punkte[30], punkte[31]);
		r4(teile1, w, punkte[10], punkte[11], punkte[36], punkte[37], punkte[28], punkte[29], punkte[32], punkte[33]);
		teile1.add(new R3t(true, w, Arrays.copyOfRange(punkte, 34, 38)));

		for(int i = 0; i < 4; i++)
			q(teile1, i == 2 ? w : w, punkte[i], punkte[(i + 1) % 4], punkte[i + 4], punkte[(i + 1) % 4 + 4]);

		q(teile1, w1, punkte[38], punkte[39], punkte[40], punkte[41]);
		q(teile1, w1, punkte[42], punkte[43], punkte[44], punkte[45]);

		teile1.add(new R3t(true, w, Arrays.copyOfRange(punkte, 0, 4)));

		return new SpielerRender(hoeheA, 0, maxh, teile1);
	}

	private static void q(ArrayList<R3t> teile1, Color f, R3p... ecken)
	{
		teile1.add(new R3t(false, f, ecken[0], ecken[1], ecken[2]));
		teile1.add(new R3t(false, f, ecken[1], ecken[2], ecken[3]));
	}

	private static int[] rf = new int[]{0, 1, 3, 2, 0};
	private static void r4(ArrayList<R3t> teile1, Color f, R3p... ecken)
	{
		for(int i = 0; i < 4; i++)
			q(teile1, f, ecken[rf[i]], ecken[rf[i + 1]], ecken[rf[i] + 4], ecken[rf[i + 1] + 4]);
	}
}