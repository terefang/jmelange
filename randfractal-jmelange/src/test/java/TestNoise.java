import com.github.terefang.jmelange.commons.CommonUtil;

import com.github.terefang.jmelange.randfractal.INoise;
import com.github.terefang.jmelange.randfractal.INoiseSampler;
import com.github.terefang.jmelange.randfractal.IRandom;
import com.github.terefang.jmelange.randfractal.Noisefield;
import com.github.terefang.jmelange.randfractal.map.ColorRamp;
import com.github.terefang.jmelange.randfractal.utils.NoiseFieldUtil;
import lombok.SneakyThrows;

import java.nio.charset.Charset;
import java.util.Map;

public class TestNoise
{
    public static void main(String[] args)
    {
        for(Map.Entry<String, Charset> _cs : Charset.availableCharsets().entrySet())
        {
            System.out.println(_cs.getKey());
        }
    }

    @SneakyThrows
    public static void main_(String[] args) {
        Noisefield _nf = null;
        ColorRamp _cr = ColorRamp.getAdvanced();
        INoiseSampler _ins = null;
        long seed = 0L;

        Map<String, INoiseSampler> _map = (Map)CommonUtil.toMap(
              "vn", INoiseSampler.valueNoiseSampler(seed),
        //      "gn", INoiseSampler.gradientNoiseSampler(seed),
              "in", INoiseSampler.impulseNoiseSampler(seed),
        //      "jpg-fn", INoiseSampler.pluginNoiseSampler(INoise.jgpFastNoise(seed)),
        //      "jpg-fvn", INoiseSampler.pluginNoiseSampler(INoise.jgpFastValueNoise(seed)),
              "jl-gcn", INoiseSampler.pluginNoiseSampler(INoise.jlGradientCoherentNoise(seed)),
        //      "jl-gn", INoiseSampler.pluginNoiseSampler(INoise.jlGradientNoise(seed)),
        //      "jl-vn", INoiseSampler.pluginNoiseSampler(INoise.jlValueNoise(seed)),
              "kp-in", INoiseSampler.pluginNoiseSampler(INoise.kpImprovedNoise(seed)),
                "kp-n", INoiseSampler.pluginNoiseSampler(INoise.kpNoise(seed)),
              //  "tm-gn", INoiseSampler.pluginNoiseSampler(INoise.tmGradientNoise(seed)),
                "tm-scn", INoiseSampler.pluginNoiseSampler(INoise.tmSparseConvolutionNoise(seed)),
                "kp-sn-br", INoiseSampler.pluginNoiseSampler(INoise.kpNoise(seed)),
             //   "kp-sn-mt", INoiseSampler.pluginNoiseSampler(INoise.kpNoise(IRandom.mtRandom(seed), seed)),
             //   "kp-sn-mtf", INoiseSampler.pluginNoiseSampler(INoise.kpNoise(IRandom.mtfRandom(seed), seed)) ,
                "tm-vn", INoiseSampler.pluginNoiseSampler(INoise.tmValueNoise(seed))
                );

        Map<String, Integer> _proc = (Map)CommonUtil.toMap(
        //    "fsum", Noisefield.NF_PROC_FSUM			,
        //    "finvsum", Noisefield.NF_PROC_FINVSUM			,
            "fbm", Noisefield.NF_PROC_FBM				,
            "fmulti", Noisefield.NF_PROC_FMULTI			,
            "frm", Noisefield.NF_PROC_FRIDGEDMULTI	//,
       //     "fsum-sin", Noisefield.NF_PROC_FSUM_SIN		,
       //     "finvsum-sin", Noisefield.NF_PROC_FINVSUM_SIN		,
       //     "fbm-sin", Noisefield.NF_PROC_FBM_SIN			,
       //     "fmulti-sin", Noisefield.NF_PROC_FMULTI_SIN		,
       //     "frm-sin", Noisefield.NF_PROC_FRIDGEDMULTI_SIN,
       //     "fsum-cos", Noisefield.NF_PROC_FSUM_COS		,
       //     "finvsum-cos", Noisefield.NF_PROC_FINVSUM_COS		,
       //     "fbm-cos", Noisefield.NF_PROC_FBM_COS			,
       //     "fmulti-cos", Noisefield.NF_PROC_FMULTI_COS		,
       //     "frm-cos", Noisefield.NF_PROC_FRIDGEDMULTI_COS
        );

        for(String _k : _map.keySet())
        {
            _ins = _map.get(_k);
            for(String _p : _proc.keySet())
            {
                _nf = new Noisefield(1024,512);
                _nf.setProjection(Noisefield.FP_EQUIRECTANGULAR);
                _nf.applyNoise(_ins, Noisefield.NF_OP_ADD, _proc.get(_p), 1000, 3, 1.0, 2.0, 6.0, 1);
                _nf.normalize( -1, 1);
                _nf.mathExponent(0, 2, 2);
                _nf.normalize( -5000, 5000);
                //_nf.morphologicalErode(10, 0.2);
                //_nf.talusErode(0,10,30,30,0.5);
                NoiseFieldUtil.saveCRImage(_nf,"./out/fract/eqrect_"+_p+"_"+_k+".png", _cr, -5000, 5000);
            }
        }
    }
}
