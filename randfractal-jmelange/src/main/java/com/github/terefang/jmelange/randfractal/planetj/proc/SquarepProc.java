package com.github.terefang.jmelange.randfractal.planetj.proc;

import com.github.terefang.jmelange.randfractal.planetj.PlanetJ;

public class SquarepProc implements Runnable
{
	PlanetJ main = null;
	
	double y,scale1,cos2,theta1;
	int i,j,k;
	
	public static SquarepProc create(PlanetJ that, int m_j, int m_k) { return new SquarepProc(that, m_j, m_k); }
	
	public SquarepProc(PlanetJ that, int m_j, int m_k)
	{
		main = that;
		j=m_j;
		k=m_k;
	}
	
	@Override
	public void run() {
		double y,scale1,theta1,cos2;
		
		y = (2.0*(j-k)-main.Height)/main.Width/main.scale*main.PI;
		if (Math.abs(y)>=0.5*main.PI)
		{
			for (int i = 0; i < main.Width ; i++)
			{
				main.col[i][j] = main.BACK;
				if(main.doshade) main.shades[i][j] = 255;
			}
		} else {
			cos2 = Math.cos(y);
			if (cos2>0.0) {
				scale1 = main.scale*main.Width/main.Height/cos2/main.PI;
				// ----- dynamic depth
				int sDepth = 3*((int)(main.log_2(scale1*main.Height)))+3;
				sDepth = (sDepth<main.Depth) ? sDepth : main.Depth;
				// -----
				for (int i = 0; i < main.Width ; i++) {
					theta1 = main.baseLongitude-0.5*main.PI+main.PI*(2.0*i-main.Width)/main.Width/main.scale;
					main.planet0(i,j,Math.cos(theta1)*cos2,Math.sin(y),-Math.sin(theta1)*cos2, sDepth);
				}
			}
		}
		main.tickH(j);
	}
	
}
