package com.github.terefang.jmelange.planetj.project;

public interface IProjection<T>
{
	public void projectCall(T _context, int _i, int _j, IProjectionCallback<T> _cb);
}
