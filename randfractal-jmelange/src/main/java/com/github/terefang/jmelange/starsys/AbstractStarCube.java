package com.github.terefang.jmelange.starsys;

import com.github.terefang.jmelange.randfractal.lite.FastNoiseLite;
import lombok.Data;

import java.lang.reflect.Array;

@Data
public abstract class AbstractStarCube<T>
{
	T [][][] data = null;

	FastNoiseLite.NoiseType noiseType = FastNoiseLite.NoiseType.SIMPLEX;
	FastNoiseLite.NoiseType secondaryNoiseType = FastNoiseLite.NoiseType.RIPPLE_HERMITE;
	FastNoiseLite.NoiseType tertiaryNoiseType = FastNoiseLite.NoiseType.MUTANT_HERMITE;

	int bits;
	int size;
	int mask;
	long seed;
	float offset = 0f;
	double H = FastNoiseLite.BASE_H;
	int octaves = FastNoiseLite.BASE_OCTAVES;
	double frequency = FastNoiseLite.BASE_FREQUENCY;
	double secondaryFrequency = 1.;
	double tertiaryFrequency = 1.;
	double lacunarity = FastNoiseLite.BASE_LACUNARITY;
	double gain = FastNoiseLite.BASE_GAIN;
	double harshness = FastNoiseLite.BASE_HARSHNESS;
	double mutation = FastNoiseLite.BASE_MUTATION;
	double sharpness = FastNoiseLite.BASE_SHARPNESS;
	boolean variableSeed = true;

	public void init()
	{
		this.data = (T[][][]) Array.newInstance(this.clazz(), this.size, this.size, this.size);

		for(int _ix = 0; _ix<this.size; _ix++)
		{
			for(int _iy = 0; _iy<this.size; _iy++)
			{
				for(int _iz = 0; _iz<this.size; _iz++)
				{
					data[_ix][_iy][_iz] = this.calculate(_ix, _iy, _iz);
				}
			}
		}
	}

	public abstract Class<T> clazz();
	public abstract T calculate(int _ix, int _iy, int _iz);
	/*
		FastNoiseLite.f_ridged_multi(
			this.noiseType,
			(double)_ix/(double)this.size,
			(double)_iy/(double)this.size,
			(double)_iz/(double)this.size,
			(int)this.seed,
			this.offset,
			this.H,
			this.octaves,
			this.frequency,
			this.lacunarity,
			this.gain,
			this.harshness,
			this.mutation,
			this.sharpness,
			this.variableSeed);
	*/
}