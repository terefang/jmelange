/***********************************************************************************
 * 
 *	Copyright (c) 2006-2015, Alfred Reibenschuh
 *	All rights reserved.
 *
 *	Redistribution and use in source and binary forms, with or without 
 *	modification, are permitted provided that the following conditions 
 *	are met:
 *
 *	1. Redistributions of source code must retain the above copyright 
 *	notice, this list of conditions and the following disclaimer.
 *
 *	2. Redistributions in binary form must reproduce the above copyright 
 *	notice, this list of conditions and the following disclaimer in the 
 *	documentation and/or other materials provided with the distribution.
 *
 *	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 *	"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 *	LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR 
 *	A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT 
 *	HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 *	SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED 
 *	TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
 *	PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
 *	LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 *	NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 *	SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 ***********************************************************************************/

package com.github.terefang.jmelange.conflux;

import com.github.terefang.jmelange.conflux.util.ArcRand;
import gnu.getopt.Getopt;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class Main {

	public static void main(String[] args) throws Exception
	{
		Getopt g = new Getopt("conflux", args, "T:n:m:s:S:i:b:f:dr");
		
		if(args.length==0)
		{
			System.out.println("Usage: java -jar conflux.jar [options]:");
			System.out.println("\t-T\t<n>\ttable size (def=2)");
			System.out.println("\t-n\t<n>\tmin word size (def=4)");
			System.out.println("\t-m\t<n>\tmax word size (def=5)");
			System.out.println("\t-s\t<n>\tnumber of words (def=20)");
			System.out.println("\t-S\t<n>\trandom seed");
			System.out.println("\t-i\t<f>\tinput file");
			System.out.println("\t-d\t\tuse debug");
			System.out.println("\t-b\t<n>\tloop breaker (def=100)");
			System.out.println("\t-f\t<n>\tfudge chars (def=3)");
			System.out.println("\t-r\t\tuse restricted mode (faster)");
			System.out.println("\t-E\t\tallow extended chars (0x7e-0xffff)");
			System.out.println("\t-X\t\tallow special chars (', -, `)");
			System.exit(1);
		}
		
		int c;
		String arg;
		Conflux cf = new Conflux();
		ArcRand _rng = ArcRand.from(0x1337beef);
		int tableSize = 2;
		int minSize = 4;
		int maxSize = 5;
		int num = 20;
		while ((c = g.getopt()) != -1)
		{
			switch(c)
			{
			case 'T':
				arg = g.getOptarg();
				tableSize = Integer.parseInt(arg);
				break;
			case 'n':
				arg = g.getOptarg();
				minSize = Integer.parseInt(arg);
				break;
			case 'm':
				arg = g.getOptarg();
				maxSize = Integer.parseInt(arg);
				break;
			case 's':
				arg = g.getOptarg();
				num = Integer.parseInt(arg);
				break;
			case 'S':
				arg = g.getOptarg();
				_rng = ArcRand.from(Long.parseLong(arg));
				break;
			case 'i':
				arg = g.getOptarg();
				cf.load(new File(arg), tableSize);
				break;
			case 'd':
				cf.setDebug(true);
				break;
			case 'r':
				cf.setRestrictedMode(true);
				break;
			case 'E':
				cf.setAllowExtendedChars(true);
				break;
			case 'X':
				cf.setAllowSpecialChars(true);
				break;
			case 'b':
				arg = g.getOptarg();
				cf.setLoopBreaker(Integer.parseInt(arg));
				break;
			case 'f':
				arg = g.getOptarg();
				cf.setFudge(Integer.parseInt(arg));
				break;
			default:
				break;
			}
		}
		
		Vector<String> w = new Vector();
		for(int i=minSize; i<=maxSize; i++)
		{
			try 
			{
				cf.generateWords(_rng, w, i, w.size()+(num/(maxSize-minSize+1))+1);
			} 
			catch (Exception xe) 
			{
				System.err.println("I="+i+" W="+w.size()+": "+xe);
			}
		}
		Collections.sort(w, new Comparator<String>() 
		{ 
			public int compare(String s1, String s2)
			{
				return s1.compareTo(s2);
			} 
		});
		
		for(int i=0; i<w.size(); i++)
		{
			System.out.println(w.elementAt(i));
		}
	}

}
