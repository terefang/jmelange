package com.github.terefang.jmelange.randfractal.xnoise;

public final class Point2 {
	public double x, y;
	
	public Point2() {
	}
	
	public Point2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Point2(Point2 p) {
		x = p.x;
		y = p.y;
	}
	
	public final Point2 set(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public final Point2 set(Point2 p) {
		x = p.x;
		y = p.y;
		return this;
	}
	
	@Override
	public final String toString() {
		return String.format("(%.2f, %.2f)", x, y);
	}
}