package com.github.terefang.jmelange.gfx.impl;

import com.github.terefang.jmelange.gfx.BgInterface;

import java.io.File;
import java.util.Vector;

public class BgInterfaceWrapper implements BgInterface
{
	AbstractGfxInterface _current;

	// BGI - Simple Borland Grafix Interface Emulation

	int bgiCurrentX = 0;
	int bgiCurrentY = 0;

	int bgiStartX = 0;
	int bgiStartY = 0;

	long bgiColor = 0;
	long bgiBkColor = -1L;

	int bgiFont = -1;

	java.util.List<BmpFont> bgiFonts = new Vector<BmpFont>();

	int bgiFontDirection = 0;
	int bgiFontSize = 8;

	public static BgInterface from(AbstractGfxInterface _ace)
	{
		BgInterfaceWrapper _bgi = new BgInterfaceWrapper();
		_bgi._current = _ace;
		return _bgi;
	}

	@Override
	public void end()
	{
		this._current.endGroup();
		this._current = null;
	}

	@Override
	public void arc(int x, int y, int stangle, int endangle, int radius)
	{
		this._current.gArc(x, y, radius, radius, stangle, endangle, this.bgiColor);
		this.bgiStartX = this.bgiCurrentX = x;
		this.bgiStartY = this.bgiCurrentY = y;
	}

	@Override
	public void bar(int left, int top, int right, int bottom) {

	}

	@Override
	public void bar3d(int left, int top, int right, int bottom, int depth, int topflag) {

	}

	@Override
	public void circle(int x, int y, int radius)
	{
		this._current.gCircle(x, y, radius, this.bgiColor);
		this.bgiStartX = this.bgiCurrentX = x;
		this.bgiStartY = this.bgiCurrentY = y;
	}

	@Override
	public void clear() {

	}

	@Override
	public void drawpoly(int... polypoints) {

	}

	@Override
	public void ellipse(int x, int y, int stangle, int endangle, int xradius, int yradius) {

	}

	@Override
	public void fillellipse(int x, int y, int xradius, int yradius) {

	}

	@Override
	public void fillpoly(int... polypoints) {

	}

	@Override
	public void floodfill(int x, int y, int border) {

	}

	@Override
	public int getmaxx() {
		if(this._current instanceof PixelImage)
		{
			return ((PixelImage)this._current).getHeight();
		}
		return 0;
	}

	@Override
	public int getmaxy()
	{
		if(this._current instanceof PixelImage)
		{
			return ((PixelImage)this._current).getWidth();
		}
		return 0;
	}

	@Override
	public int getpixel(int x, int y) {
		return this._current.gGet(x,y);
	}

	@Override
	public int getx() {
		return this.bgiCurrentX;
	}

	@Override
	public int gety() {
		return this.bgiCurrentY;
	}

	@Override
	public void graphdefaults()
	{
		this.bgiCurrentX = 0;
		this.bgiCurrentY = 0;

		this.bgiStartX = 0;
		this.bgiStartY = 0;

		this.bgiColor = 0;
		this.bgiBkColor = -1L;

		this.bgiFont = -1;
		this.bgiFontDirection = 0;
		this.bgiFontSize = 8;
	}


	@Override
	public int installuserfont(String _name) {
		if(_name == null)
		{
			this.bgiFonts.add((BmpFont) BmpFont.defaultInstance());
		}
		else if(_name.contains(".gdf"))
		{
			this.bgiFonts.add((BmpFont) BmpFont.load(new File(_name)));
		}
		else if(FONT_6x11.equalsIgnoreCase(_name))
		{
			this.bgiFonts.add((BmpFont) BmpFont.default6x11());
		}
		else if(FONT_6x12.equalsIgnoreCase(_name))
		{
			this.bgiFonts.add((BmpFont) BmpFont.default6x12());
		}
		else if(FONT_8x8.equalsIgnoreCase(_name))
		{
			this.bgiFonts.add((BmpFont) BmpFont.default8x8());
		}
		else if(FONT_8x14.equalsIgnoreCase(_name))
		{
			this.bgiFonts.add((BmpFont) BmpFont.default8x14());
		}
		else if(FONT_8x16.equalsIgnoreCase(_name))
		{
			this.bgiFonts.add((BmpFont) BmpFont.default8x16());
		}
		else if(FONT_7x8.equalsIgnoreCase(_name))
		{
			this.bgiFonts.add((BmpFont) BmpFont.epson_7x8());
		}
		else if(FONT_7x12.equalsIgnoreCase(_name))
		{
			this.bgiFonts.add((BmpFont) BmpFont.mc6847_7x12());
		}
		else
		{
			throw new IllegalArgumentException(_name);
		}
		return this.bgiFonts.size()-1;
	}

	@Override
	public void line(int x1, int y1, int x2, int y2)
	{
		this._current.gLine(x1, y1, x2, y2, this.bgiColor);
		this.bgiStartX = x1; this.bgiCurrentX = x2;
		this.bgiStartY = y1; this.bgiCurrentY = y2;
	}

	@Override
	public void linerel(int dx, int dy)
	{
		this._current.gLine(this.bgiCurrentX, this.bgiCurrentY, this.bgiCurrentX+dx, this.bgiCurrentY+dy, this.bgiColor);
		this.bgiCurrentX+=dx;
		this.bgiCurrentY+=dy;
	}

	@Override
	public void lineto(int x, int y) {
		this._current.gLine(this.bgiCurrentX, this.bgiCurrentY, x, y, this.bgiColor);
		this.bgiCurrentX=x;
		this.bgiCurrentY=y;
	}

	@Override
	public void moverel(int dx, int dy) {
		this.bgiCurrentX+=dx;
		this.bgiCurrentY+=dy;
	}

	@Override
	public void moveto(int x, int y) {
		this.bgiCurrentX=x;
		this.bgiCurrentY=y;
	}

	@Override
	public void outtext(String _textstring)
	{
		this.bgiFonts.get(this.bgiFont).drawString(this._current, this.bgiCurrentX, this.bgiCurrentY, _textstring, this.bgiColor);
		this.bgiCurrentX+= this.textwidth(_textstring);
	}

	@Override
	public void outtextxy(int x, int y, String _textstring) {
		if(this.bgiBkColor == -1L)
		{
			this.bgiFonts.get(this.bgiFont).drawString(this._current, x, y, _textstring, this.bgiColor);
		}
		else
		{
			this.bgiFonts.get(this.bgiFont).drawString(this._current, x, y, _textstring, this.bgiColor, this.bgiBkColor);
		}
		this.bgiCurrentX= x+this.textwidth(_textstring);
		this.bgiCurrentY= y;
	}

	@Override
	public void pieslice(int x, int y, int stangle, int endangle, int radius) {

	}

	@Override
	public void putpixel(int x, int y, int color) {
		this._current.gSet(x,y, color & 0xFFFFFFFL);
	}

	@Override
	public void rectangle(int left, int top, int right, int bottom)
	{
		this._current.gRectangle(left, top, right, bottom, this.bgiColor);
	}

	@Override
	public int registerfont(BmpFont _font)
	{
		this.bgiFonts.add(_font);
		return this.bgiFonts.size()-1;
	}

	@Override
	public void sector(int x, int y, int stangle, int endangle, int xradius, int yradius) {

	}

	@Override
	public void setbkcolor(long color)
	{
		this.bgiBkColor = color;
	}

	@Override
	public void setcolor(long color) {
		this.bgiColor = color;
	}

	@Override
	public void setfillpattern(String _upattern, int color) {

	}

	@Override
	public void setfillstyle(int pattern, int color) {

	}

	@Override
	public void setgraphmode(int mode) {

	}

	@Override
	public void setlinestyle(int linestyle, int upattern, int thickness) {

	}

	@Override
	public void settextjustify(int horiz, int vert) {

	}

	@Override
	public void settextstyle(int font, int direction, int charsize) {
		this.bgiFont = font;
		this.bgiFontDirection = direction;
		this.bgiFontSize = charsize;
	}

	@Override
	public void setusercharsize(int multx, int divx, int multy, int divy) {

	}

	@Override
	public void setviewport(int left, int top, int right, int bottom, int clip) {

	}

	@Override
	public void setwritemode(int mode) {

	}

	@Override
	public int textheight(String _textstring) {
		return this.bgiFonts.get(this.bgiFont).getFontHeight();
	}

	@Override
	public int textwidth(String _textstring) {
		return this.bgiFonts.get(this.bgiFont).stringWidth(_textstring);
	}

}
