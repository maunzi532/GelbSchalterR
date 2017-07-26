package area;

import block.*;
import gelb.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import javax.swing.*;

class M
{
	private static boolean g = false;
	private static boolean changesize = false;
	private static final String texOrdnerName = "Texturen2";
	private static File selected;
	public static boolean reload = true;

	public static void main(String[] args)
	{
		boolean cheatmode = false;
		for(int i = 0; i < args.length; i++)
		{
			switch(args[i])
			{
				case "cheatmode":
					cheatmode = true;
					break;
				case "gelb":
					g = true;
					break;
				case "changesize":
					changesize = true;
					break;
				default:
					selected = new File(args[i]);
			}
		}
		while(reload)
		{
			reload = false;
			if(selected == null)
			{
				JFileChooser fc = new JFileChooser(new File("saves"));
				if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
					selected = fc.getSelectedFile();
			}
			if(selected != null)
				start(selected, cheatmode);
			selected = null;
		}
		System.exit(0);
	}

	private static void start(File lv, boolean cheatmode)
	{
		String input = null;
		try
		{
			input = new String(Files.readAllBytes(lv.toPath()), Charset.forName("UTF-8"));
		}
		catch(Exception e)
		{
			System.out.println("Nicht gefunden");
			System.exit(1);
		}
		Area area;
		if(g)
			area = new Gelb();
		else
			area = new BlockLab();
		area.start(input, texOrdnerName, changesize, cheatmode);
	}
}