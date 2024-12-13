package com.github.terefang.jmelange.commons.gfx.bgi;


public interface BgInterface
{
	public static final String FONT_6x11 = "6x11";
	public static final String FONT_6x12 = "6x12";
	public static final String FONT_8x8 = "8x8";
	public static final String FONT_8x14 = "8x14";
	public static final String FONT_8x16 = "8x16";
	public static final String FONT_7x12 = "7x12";

	void arc (int x, int y, int stangle, int endangle, int radius);

	void bar (int left, int top, int right, int bottom);

	void bar3d (int left, int top, int right, int bottom, int depth, int topflag);

	void circle (int x, int y, int radius);

	void clear();

	void end();

	void drawpoly (int...polypoints);

	void ellipse (int x, int y, int stangle, int endangle, int xradius, int yradius);

	void fillellipse (int x, int y, int xradius, int yradius);

	void fillpoly (int...polypoints);

	void floodfill (int x, int y, int border);

	int getmaxx ();

	int getmaxy ();

	int getpixel (int x, int y);

	int getx ();

	int gety ();

	void graphdefaults ();

	int installuserfont (String _name);

	void line (int x1, int y1, int x2, int y2);

	void linerel (int dx, int dy);

	void lineto (int x, int y);

	void moverel (int dx, int dy);

	void moveto (int x, int y);


	void outtext (String _textstring);

	void outtextxy (int x, int y, String _textstring);


	void pieslice (int x, int y, int stangle, int endangle, int radius);

	//void putimage (int left, int top, void *bitmap, int op);

	void putpixel (int x, int y, int color);

	void rectangle (int left, int top, int right, int bottom);

	int registerfont (BmpFont _font);

	void sector (int x, int y, int stangle, int endangle, int xradius, int yradius);

	//void setaspectratio (int xasp, int yasp);

	void setbkcolor (long color);

	void setcolor (long color);

	void setfillpattern (String _upattern, int color);

	void setfillstyle (int pattern, int color);

	void setgraphmode (int mode);

	void setlinestyle (int linestyle, int upattern, int thickness);

	void settextjustify (int horiz, int vert);

	void settextstyle (int font, int direction, int charsize);

	void setusercharsize (int multx, int divx, int multy, int divy);

	void setviewport (int left, int top, int right, int bottom, int clip);

	void setwritemode (int mode);

	int textheight (String _textstring);

	int textwidth (String _textstring);

}
