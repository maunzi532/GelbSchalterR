package area;

import block.*;
import gelb.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import javax.swing.*;

class M
{
	private static boolean cheatmode;
	private static boolean changesize;
	private static int testmode;
	private static final String texOrdnerName = "Texturen2";
	private static File selected;
	public static boolean reload = true;

	public static void main(String[] args)
	{
		for(int i = 0; i < args.length; i++)
		{
			switch(args[i])
			{
				case "cheatmode":
					cheatmode = true;
					break;
				case "changesize":
					changesize = true;
					break;
				case "testmode":
					testmode = 1;
					break;
				case "testmodevis":
					testmode = 2;
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
		switch(input.charAt(0))
		{
			case '{':
			case ' ':
				area = new BlockLab();
				break;
			default:
				if(input.contains(";;"))
					area = new Gelb();
				else
					area = new BlockLab();
		}
		area.start(input, texOrdnerName, cheatmode, changesize, testmode);
	}
}