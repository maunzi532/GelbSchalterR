package block.state;

import area.*;
import block.*;
import java.util.*;
import tex.*;

public class ASIN
{
	static ArrayList<BState> states;
	static int caret1;
	static boolean nca;
	static int caret2;
	static ArrayList<ArrayList<Integer>> pointers;
	static BlockLab blockLab;

	static int[] ws;
	static ArrayList<Integer> newws;
	static ArrayList<Integer> newws2;

	public static void run()
	{
		states = new ArrayList<>();
		caret1 = 0;
		nca = true;
		caret2 = 0;
		pointers = new ArrayList<>();
		blockLab = (BlockLab) SIN.area;

		states.add(new BState(blockLab));
		pointers.add(new ArrayList<>());
		while(caret1 < states.size())
		{
			//System.out.println(caret1 + " " + states.size());
			if(states.get(caret1).gewonnen)
			{
				nca = true;
				caret1++;
				caret2 = 0;
				pointers.add(new ArrayList<>());
				continue;
			}
			states.get(caret1).charge(blockLab);
			blockLab.srd.reset2(blockLab);
			Shift.selectTarget(blockLab.xp, blockLab.yp, blockLab.hoeheA, SIN.kamZoom, 1);

			SIN.area.checkFields();
			if(nca)
			{
				nca = false;
				if(SIN.testmode == 2)
				{
					Shift.moveToTarget();
					SIN.drawX();
				}
				else
					System.out.println(states.size());
			}
			if(blockLab.moveR(caret2))
			{
				caret2++;
				BState nst = new BState(blockLab);
				int ind = states.indexOf(nst);
				if(ind >= 0)
					pointers.get(caret1).add(ind);
				else
				{
					pointers.get(caret1).add(states.size());
					states.add(nst);
					//System.out.println(nst);
				}
			}
			else
			{
				nca = true;
				caret1++;
				caret2 = 0;
				pointers.add(new ArrayList<>());
			}
		}
		System.out.println(states.size());
		ws();
	}

	public static void ws()
	{
		ws = new int[states.size()];
		int tcl = ws.length;
		newws = new ArrayList<>();

		int weg = 0;
		for(int i = 0; i < ws.length; i++)
			if(states.get(i).gewonnen)
			{
				newws.add(i);
				tcl--;
			}
			else
				ws[i] = -1;
		while(tcl > 0 && !newws.isEmpty())
		{
			newws2 = new ArrayList<>();
			weg++;
			for(int i = 0; i < ws.length; i++)
				if(ws[i] < 0)
				{
					for(Integer p : pointers.get(i))
						if(newws.contains(p))
						{
							ws[i] = weg;
							newws2.add(i);
							tcl--;
							break;
						}
				}
			newws = newws2;
		}
		if(newws.isEmpty())
			System.out.println("rip " + tcl);
		else
			System.out.println("yay");
		System.out.println(ws[0]);
		if(newws.isEmpty())
			showDeadEnds();
		if(ws[0] >= 0)
			showFastPath();
	}

	public static void showDeadEnds()
	{
		for(int i = 0; i < ws.length; i++)
			if(ws[i] < 0)
			{
				states.get(i).charge(blockLab);
				blockLab.srd.reset2(blockLab);
				Shift.selectTarget(blockLab.xp, blockLab.yp, blockLab.hoeheA, SIN.kamZoom, 1);
				Shift.moveToTarget();
				SIN.drawX();
				System.out.println(i);
				TA.bereit();
				while(TA.take[16] <= 0)
				{
					U.warte(20);
					TA.bereit();
				}
			}
	}

	public static void showFastPath()
	{
		caret1 = 0;
		states.get(caret1).charge(blockLab);
		blockLab.srd.reset(blockLab);
		Shift.selectTarget(blockLab.xp, blockLab.yp, blockLab.hoeheA, SIN.kamZoom, 1);
		for(int i = 0; i < 5; i++)
		{
			Shift.moveToTarget();
			SIN.drawX();
			U.warte(20);
		}
		blockLab.srd.dspeed = 0.25;
		blockLab.srd.mspeed = 2;
		int noch = ws[0];
		while(!states.get(caret1).gewonnen)
		{
			Shift.moveToTarget();
			SIN.drawX();
			U.warte(20);
			blockLab.checkFields();
			int c2 = 0;
			for(int i = 0; i < pointers.get(caret1).size(); i++)
				if(ws[pointers.get(caret1).get(i)] < noch)
				{
					c2 = i;
					break;
				}
			caret1 = pointers.get(caret1).get(c2);
			noch = ws[caret1];
			blockLab.moveR(c2);
			Shift.selectTarget(blockLab.xp, blockLab.yp, blockLab.hoeheA, SIN.kamZoom, 1);
		}
		for(int i = 0; i < 20; i++)
		{
			if(blockLab.gewonnen)
				blockLab.victoryTick();
			Shift.moveToTarget();
			SIN.drawX();
			U.warte(20);
		}
	}
}