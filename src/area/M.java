package area;

import block.*;
import gelb.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import javax.swing.*;
import tex.*;

class M
{
	private static boolean g = false;
	private static String texPack;
	private static String texOrdnerName = "Texturen2";

	public static void main(String[] args)
	{
		File selected = null;
		boolean ch = false;
		for(int i = 0; i < args.length; i++)
		{
			if(args[i].equals("cheatmode"))
				ch = true;
			else
				selected = new File(args[i]);
		}
		if(selected == null)
		{
			JFileChooser fc = new JFileChooser(new File("saves"));
			fc.showOpenDialog(null);
			selected = fc.getSelectedFile();
		}
		start(selected, ch);
		System.exit(0);
	}

	private static void start(File lv, boolean ch)
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
		if(g)
		{
			texPack = "Default";
			Area area = new Gelb();
			area.readFL(input);
			area.reset();
			Texturen tex = new GTex(texPack, texOrdnerName);
			SIN.start(area, tex, ch);
		}
		else
		{
			texPack = "BlockLab";
			Area area = new BlockLab();
			area.readFL(input);
			area.reset();
			Texturen tex = new FTex(texPack, texOrdnerName);
			SIN.start(area, tex, ch);
		}
	}
}