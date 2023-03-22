package com.github.terefang.jmelange.starsys;

import com.github.terefang.jmelange.randfractal.lite.FastNoiseLite;
import com.github.terefang.jmelange.starsys.model.SystemContext;
import lombok.Data;

public class SimpleStarCube extends AbstractStarCube<SimpleStarCube.SimpleStar>
{
	@Override
	public SimpleStarCube.SimpleStar calculate(int _ix, int _iy, int _iz)
	{
		SimpleStarCube.SimpleStar _ret = new SimpleStarCube.SimpleStar();

		_ret.setDX(FastNoiseLite.singleNoiseByType(this.getTertiaryNoiseType(), ((_ix << 8) ^ (_iy << 16) ^ _iz), ((double)_ix)*this.getTertiaryFrequency()/(double)this.getSize(), ((double)_iy)*this.getTertiaryFrequency()/(double)this.getSize()));
		_ret.setDY(FastNoiseLite.singleNoiseByType(this.getTertiaryNoiseType(), ((_iy << 8) ^ (_iz << 16) ^ _ix), ((double)_iz)*this.getTertiaryFrequency()/(double)this.getSize(), ((double)_iy)*this.getTertiaryFrequency()/(double)this.getSize()));
		_ret.setDZ(FastNoiseLite.singleNoiseByType(this.getTertiaryNoiseType(), ((_iz << 8) ^ (_ix << 16) ^ _iy), ((double)_iz)*this.getTertiaryFrequency()/(double)this.getSize(), ((double)_ix)*this.getTertiaryFrequency()/(double)this.getSize()));

		_ret.setLocalX(50. + ((((double)_ix)+_ret.getDX()) - (this.getSize() / 2.)) * 100.);
		_ret.setLocalY(50. + ((((double)_iy)+_ret.getDY()) - (this.getSize() / 2.)) * 100.);
		_ret.setLocalZ(50. + ((((double)_iz)+_ret.getDZ()) - (this.getSize() / 2.)) * 100.);

		_ret.setNoiseX(FastNoiseLite.singleByNoiseAndTransform(this.getNoiseType(), FastNoiseLite.TransformType.T_ABS, (int)this.getSeed(), ((double)_ix)*this.getFrequency()/(double)this.getSize(), ((double)_iy)*this.getFrequency()/(double)this.getSize(), ((double)_iz)*this.getFrequency()/(double)this.getSize()));
		_ret.setNoiseY(FastNoiseLite.singleByNoiseAndTransform(this.getNoiseType(), FastNoiseLite.TransformType.T_ABS, (int)this.getSeed(), ((double)_iy)*this.getFrequency()/(double)this.getSize(), ((double)_iz)*this.getFrequency()/(double)this.getSize(), ((double)_ix)*this.getFrequency()/(double)this.getSize()));
		_ret.setNoiseZ(FastNoiseLite.singleByNoiseAndTransform(this.getNoiseType(), FastNoiseLite.TransformType.T_ABS, (int)this.getSeed(), ((double)_iz)*this.getFrequency()/(double)this.getSize(), ((double)_ix)*this.getFrequency()/(double)this.getSize(), ((double)_iy)*this.getFrequency()/(double)this.getSize()));
		_ret.setNoise(_ret.getNoiseX()+_ret.getNoiseY()+_ret.getNoiseZ());

		_ret.setNoiseU(FastNoiseLite.f_ridged_multi(this.getNoiseType(), (double)_ix/(double)this.getSize(), (double)_iy/(double)this.getSize(), (double)_iz/(double)this.getSize(), (int)this.getSeed(), this.getOffset(), this.getH(), this.getOctaves(),this.getFrequency(),this.getLacunarity(),this.getGain(), this.getHarshness(), this.getMutation(), this.getSharpness(), this.isVariableSeed()));
		_ret.setNoiseV(FastNoiseLite.singleByNoiseAndTransform(this.getSecondaryNoiseType(), FastNoiseLite.TransformType.T_ABS, (int)this.getSeed(), ((double)_iy)*this.getSecondaryFrequency()/(double)this.getSize(), ((double)_iz)*this.getSecondaryFrequency()/(double)this.getSize(), ((double)_ix)*this.getSecondaryFrequency()/(double)this.getSize()));
		_ret.setNoiseW(FastNoiseLite.singleByNoiseAndTransform(this.getSecondaryNoiseType(), FastNoiseLite.TransformType.T_ABS, (int)this.getSeed(), ((double)_iz)*this.getSecondaryFrequency()/(double)this.getSize(), ((double)_ix)*this.getSecondaryFrequency()/(double)this.getSize(), ((double)_iy)*this.getSecondaryFrequency()/(double)this.getSize()));

		_ret.setId((_iz & this.getMask()) | ((_iy & this.getMask())<<this.getBits()) | ((_ix & this.getMask())<<(this.getBits()*2)));
		return _ret;
	}

	@Override
	public Class<SimpleStarCube.SimpleStar> clazz() {
		return SimpleStar.class;
	}

	@Data
	public static class SimpleStar
	{
		long id;
		double dX;
		double dY;
		double dZ;
		double localX;
		double localY;
		double localZ;

		double noiseX;
		double noiseY;
		double noiseZ;
		double noise;

		double noiseU;
		double noiseV;
		double noiseW;

		SystemContext context;

		public void init(int _seed)
		{
			this.context = new SystemContext();
			this.context.setId(id);
			this.context.setSeed(_seed);
			this.context.init();
		}
	}
}
