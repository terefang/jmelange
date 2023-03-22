package com.github.terefang.jmelange.planetj.util;

import com.github.terefang.jmelange.planetj.PlanetJ;
import com.github.terefang.jmelange.randfractal.lite.FastNoiseLite;

public class PlanetHelper
{
	public static double planetaryTemperature(double _x, double _y, double _z, int _lvl, double _alt, double _tAdj)
	{
		double temp;

		/* calculate temperature based on altitude and latitude */
		/* scale: -0.1 to 0.1 corresponds to -30 to +30 degrees Celsius */
		double sun = Math.sqrt(1.0-_y*_y); /* approximate amount of sunlight at
			     					latitude ranged from 0.1 to 1.1 */
		if (_alt < 0)
		{
			temp = (sun / 8.0) - (_alt * 0.3); /* deep water colder */
		}
		else {
			temp = (sun / 8.0) - (_alt * 1.2); /* high altitudes colder */
		}

		temp += _tAdj;

		return temp;
	}

	public static double calcTempAdjustmentCell(double x, double y, double z)
	{
		return calcTempAdjustmentCell(x,y,z,0x13371337,56., .001, -.0);
	}

	public static double calcTempAdjustmentCell(double x, double y, double z, int _seed)
	{
		return calcTempAdjustmentCell(x,y,z,_seed,56., .001, -.0);
	}

	public static double calcTempAdjustmentCell(double x, double y, double z, int _seed, double _temperatureVariationFrequency, double _temperatureVariationFactor, double _temperatureBase)
	{
		double _tAdj = FastNoiseLite.fractalByType(FastNoiseLite.FractalType.F_MULTI,
				FastNoiseLite.NoiseType.CELLULAR_NATURAL_CELL_VALUE,
				_seed, (x*_temperatureVariationFrequency), (z*_temperatureVariationFrequency), (y*_temperatureVariationFrequency),
				FastNoiseLite.BASE_OFFSET, FastNoiseLite.BASE_H, 4, true);

		_tAdj = (_temperatureVariationFactor * _tAdj) + _temperatureBase;

		return _tAdj;
	}

	public static double planetaryRain(double _x, double _y, double _z, int _lvl, double _alt, double _temp)
	{
		double _rAdj = calcRainAdjustmentCell(_x,_y,_z);

		return planetaryRain(_x, _y, _z, _lvl, _alt, _temp, _rAdj);
	}

	public static double planetaryRain(double _x, double _y, double _z, int _lvl, double _alt, double _temp, int _seed)
	{
		double _rAdj = calcRainAdjustmentCell(_x,_y,_z, _seed);

		return planetaryRain(_x, _y, _z, _lvl, _alt, _temp, _rAdj);
	}

	public static double planetaryRain(double _x, double _y, double _z, int _lvl, double _alt, double _temp, int _seed, double _rainfallVariationFrequency, double _rainShadow, double _rainfallBase, double _rainfallVariationFactor)
	{
		double _rAdj = calcRainAdjustmentCell(_x,_y,_z, _seed, _rainfallVariationFrequency, _rainShadow, _rainfallBase, _rainfallVariationFactor);

		return planetaryRain(_x, _y, _z, _lvl, _alt, _temp, _rAdj);
	}

	public static double planetaryRain(double _x, double _y, double _z, int _lvl, double _alt, double _temp, double _rAdj)
	{
		/* calculate rainfall based on temperature and latitude */
  		/* rainfall approximately proportional to temperature but reduced
     		near horse latitudes (+/- 30 degrees, y=0.5) and reduced for
     		rain shadow */

		double y2 = Math.abs(_y)-0.5;
		double rain = _temp*0.65 + 0.1 - 0.011/(y2*y2+0.1);

		rain += _rAdj;

		if (rain<0.0) rain = 0.0;

		return rain;
	}

	public static double calcRainAdjustmentCell(double x, double y, double z)
	{
		return calcRainAdjustmentCell(x,y,z,0x13371337, 123.456, 0, -.0, .001);
	}

	public static double calcRainAdjustmentCell(double x, double y, double z, int _seed)
	{
		return calcRainAdjustmentCell(x,y,z,_seed, 123.456, 0, -.0, .001);
	}

	public static double calcRainAdjustmentCell(double x, double y, double z, int _seed, double _rainfallVariationFrequency, double _rainShadow, double _rainfallBase, double _rainfallVariationFactor)
	{
		double _rAdj = FastNoiseLite.fractalByType(FastNoiseLite.FractalType.F_MULTI,
				FastNoiseLite.NoiseType.CELLULAR2EDGE_NATURAL_DISTANCE_2,
				_seed, (x * _rainfallVariationFrequency), (z * _rainfallVariationFrequency), (y * _rainfallVariationFrequency),
				FastNoiseLite.BASE_OFFSET, FastNoiseLite.BASE_H, 4, true);
		_rAdj = (0.03*_rainShadow) + _rainfallBase + (_rainfallVariationFactor * _rAdj);
		return _rAdj;
	}

	public static char planetaryBiome(double _x, double _y, double _z, double _alt, double _temp, double _rain)
	{
		char _biome = '*';

		/* make biome colours */
		int tt = PlanetJ.min(44,PlanetJ.max(0,(int)(_rain*300.0-9)));
		int rr = PlanetJ.min(44,PlanetJ.max(0,(int)(_temp*300.0+10)));

		_biome = biomes[tt].charAt(rr);

		if ((_alt < 0.0) && (_biome != 'I')) {
			_biome = '*';
		}

		return  _biome;
	}

	/* Whittaker diagram */
	public static final String[] biomes = {
			"IIITTTTTGGGGGGGGDDDDDDDDDDDDDDDDDDDDDDDDDDDDD",
			"IIITTTTTGGGGGGGGDDDDGGDSDDSDDDDDDDDDDDDDDDDDD",
			"IITTTTTTTTTBGGGGGGGGGGGSSSSSSDDDDDDDDDDDDDDDD",
			"IITTTTTTTTBBBBBBGGGGGGGSSSSSSSSSWWWWWWWDDDDDD",
			"IITTTTTTTTBBBBBBGGGGGGGSSSSSSSSSSWWWWWWWWWWDD",
			"IIITTTTTTTBBBBBBFGGGGGGSSSSSSSSSSSWWWWWWWWWWW",
			"IIIITTTTTTBBBBBBFFGGGGGSSSSSSSSSSSWWWWWWWWWWW",
			"IIIIITTTTTBBBBBBFFFFGGGSSSSSSSSSSSWWWWWWWWWWW",
			"IIIIITTTTTBBBBBBBFFFFGGGSSSSSSSSSSSWWWWWWWWWW",
			"IIIIIITTTTBBBBBBBFFFFFFGGGSSSSSSSSWWWWWWWWWWW",
			"IIIIIIITTTBBBBBBBFFFFFFFFGGGSSSSSSWWWWWWWWWWW",
			"IIIIIIIITTBBBBBBBFFFFFFFFFFGGSSSSSWWWWWWWWWWW",
			"IIIIIIIIITBBBBBBBFFFFFFFFFFFFFSSSSWWWWWWWWWWW",
			"IIIIIIIIIITBBBBBBFFFFFFFFFFFFFFFSSEEEWWWWWWWW",
			"IIIIIIIIIITBBBBBBFFFFFFFFFFFFFFFFFFEEEEEEWWWW",
			"IIIIIIIIIIIBBBBBBFFFFFFFFFFFFFFFFFFEEEEEEEEWW",
			"IIIIIIIIIIIBBBBBBRFFFFFFFFFFFFFFFFFEEEEEEEEEE",
			"IIIIIIIIIIIIBBBBBBRFFFFFFFFFFFFFFFFEEEEEEEEEE",
			"IIIIIIIIIIIIIBBBBBRRRFFFFFFFFFFFFFFEEEEEEEEEE",
			"IIIIIIIIIIIIIIIBBBRRRRRFFFFFFFFFFFFEEEEEEEEEE",
			"IIIIIIIIIIIIIIIIIBRRRRRRRFFFFFFFFFFEEEEEEEEEE",
			"IIIIIIIIIIIIIIIIIRRRRRRRRRRFFFFFFFFEEEEEEEEEE",
			"IIIIIIIIIIIIIIIIIIRRRRRRRRRRRRFFFFFEEEEEEEEEE",
			"IIIIIIIIIIIIIIIIIIIRRRRRRRRRRRRRFRREEEEEEEEEE",
			"IIIIIIIIIIIIIIIIIIIIIRRRRRRRRRRRRRRRREEEEEEEE",
			"IIIIIIIIIIIIIIIIIIIIIIIRRRRRRRRRRRRRROOEEEEEE",
			"IIIIIIIIIIIIIIIIIIIIIIIIRRRRRRRRRRRROOOOOEEEE",
			"IIIIIIIIIIIIIIIIIIIIIIIIIIRRRRRRRRRROOOOOOEEE",
			"IIIIIIIIIIIIIIIIIIIIIIIIIIIRRRRRRRRROOOOOOOEE",
			"IIIIIIIIIIIIIIIIIIIIIIIIIIIIRRRRRRRROOOOOOOEE",
			"IIIIIIIIIIIIIIIIIIIIIIIIIIIIIRRRRRRROOOOOOOOE",
			"IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIRRRRROOOOOOOOOO",
			"IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIRROOOOOOOOOOO",
			"IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIROOOOOOOOOOO",
			"IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIROOOOOOOOOOO",
			"IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIOOOOOOOOOOO",
			"IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIOOOOOOOOOO",
			"IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIOOOOOOOOO",
			"IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIOOOOOOOOO",
			"IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIOOOOOOOO",
			"IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIOOOOOOOO",
			"IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIOOOOOOOO",
			"IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIOOOOOOOO",
			"IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIOOOOOOO",
			"IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIOOOOOOO"
	};
}
