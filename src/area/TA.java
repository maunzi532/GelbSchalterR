package area;

import java.awt.event.*;
import javax.swing.*;

public class TA
{
	private static final int KL = 300;
	private static final int MP = 200;
	private static final int MRP = 10;
	private static boolean[] keys;
	public static int[] take;

	public static void einbau(JFrame fenster)
	{
		keys = new boolean[KL];
		take = new int[keys.length];
		fenster.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent keyEvent)
			{
				x(true, false, keyEvent.getKeyCode());
			}

			@Override
			public void keyReleased(KeyEvent keyEvent)
			{
				x(false, false, keyEvent.getKeyCode());
			}
		});
		fenster.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				x(true, true, mouseEvent.getButton());
			}

			@Override
			public void mouseReleased(MouseEvent mouseEvent)
			{
				x(false, true, mouseEvent.getButton());
			}

			@Override
			public void mouseEntered(MouseEvent mouseEvent)
			{
				x(false, true, mouseEvent.getButton());
			}

			@Override
			public void mouseExited(MouseEvent mouseEvent)
			{
				x(false, true, mouseEvent.getButton());
			}
		});
		fenster.addMouseWheelListener(new MouseAdapter()
		{
			@Override
			public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent)
			{
				x(true, true, mouseWheelEvent.getWheelRotation() > 0 ? MRP : MRP + 1);
			}
		});
	}

	private static void x(boolean set, boolean m, int n)
	{
		if(m)
			n += MP;
		if(n >= 0 && n < keys.length)
			keys[n] = set;
	}

	public static void fix()
	{
		keys = new boolean[keys.length];
	}

	public static void bereit()
	{
		int[] t2 = new int[take.length];
		for(int i = 0; i < t2.length; i++)
		{
			if(take[i] > 0 == keys[i])
				t2[i] = keys[i] ? 1 : 0;
			else
				t2[i] = keys[i] ? 2 : -1;
		}
		take = t2;
		keys[MP + MRP] = false;
		keys[MP + MRP + 1] = false;
	}
}