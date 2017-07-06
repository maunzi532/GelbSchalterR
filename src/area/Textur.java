package area;

import java.awt.image.*;

public class Textur
{
	public int h_up;
	public int h_down;
	public BufferedImage[] look;

	public Textur(int h_up, int h_down, BufferedImage[] look)
	{
		this.h_up = h_up;
		this.h_down = h_down;
		this.look = look;
	}
}