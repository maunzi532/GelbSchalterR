package tex;

import java.awt.image.*;

public class Textur
{
	public final int h_up;
	public final int h_down;
	public final BufferedImage[] look;

	public Textur(int h_up, int h_down, BufferedImage[] look)
	{
		this.h_up = h_up;
		this.h_down = h_down;
		this.look = look;
	}
}