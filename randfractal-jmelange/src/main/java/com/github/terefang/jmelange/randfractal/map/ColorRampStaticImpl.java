package com.github.terefang.jmelange.randfractal.map;

import com.github.terefang.jmelange.randfractal.utils.MathHelper;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

/**
 * Created by fredo on 21.11.15.
 */
public class ColorRampStaticImpl implements ColorRamp
{
    public Color[] SEA_COLOR = null;
    public Color[] LAND_COLOR = null;

    public boolean seaHardRamp = true;
    public boolean landHardRamp = true;

    @Override
    public boolean isNonlinear()
    {
        return this.landHardRamp || this.seaHardRamp;
    }
    
    public void load(Reader reader)
    {
        try
        {
            Properties properties = new Properties();
            properties.load(reader);

            this.landHardRamp=!Boolean.parseBoolean(properties.getProperty("softRamp", "false"));
            this.landHardRamp=!Boolean.parseBoolean(properties.getProperty("landSoftRamp", this.landHardRamp ? "false" : "true"));

            this.seaHardRamp=!Boolean.parseBoolean(properties.getProperty("softRamp", "false"));
            this.seaHardRamp=!Boolean.parseBoolean(properties.getProperty("landSoftRamp", this.seaHardRamp ? "false" : "true"));

            List<String> sea = new Vector();
            for(int i = 0; properties.containsKey("sea."+i); i++)
            {
                sea.add(properties.getProperty("sea."+i));
            }

            if(this.seaHardRamp)
            {
                SEA_COLOR = new Color[(sea.size()*2)-1];
                for(int i = 0; i < (sea.size()-1); i++)
                {
                    String[] rgb1 = sea.get(i).split("[\\s,]+");
                    String[] rgb2 = sea.get(i+1).split("[\\s,]+");
                    SEA_COLOR[i*2]=new Color(Integer.parseInt(rgb1[0]), Integer.parseInt(rgb1[1]), Integer.parseInt(rgb1[2]));
                    SEA_COLOR[(i+1)*2]=new Color(Integer.parseInt(rgb2[0]), Integer.parseInt(rgb2[1]), Integer.parseInt(rgb2[2]));
                    SEA_COLOR[(i*2)+1]= MathHelper.lerp(SEA_COLOR[i*2], SEA_COLOR[(i+1)*2], 0.5f);
                }
            }
            else
            {
                SEA_COLOR = new Color[sea.size()];
                for(int i = 0; i < SEA_COLOR.length; i++)
                {
                    String[] rgb = sea.get(i).split("[\\s,]+");
                    SEA_COLOR[i]=new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
                }
            }


            List<String> land = new Vector();
            for(int i = 0; properties.containsKey("land."+i); i++)
            {
                land.add(properties.getProperty("land."+i));
            }

            if(this.landHardRamp)
            {
                LAND_COLOR = new Color[(land.size()*2)-1];
                for(int i = 0; i < (land.size()-1); i++)
                {
                    String[] rgb1 = land.get(i).split("[\\s,]+");
                    String[] rgb2 = land.get(i+1).split("[\\s,]+");
                    LAND_COLOR[i*2]=new Color(Integer.parseInt(rgb1[0]), Integer.parseInt(rgb1[1]), Integer.parseInt(rgb1[2]));
                    LAND_COLOR[(i+1)*2]=new Color(Integer.parseInt(rgb2[0]), Integer.parseInt(rgb2[1]), Integer.parseInt(rgb2[2]));
                    LAND_COLOR[(i*2)+1]=MathHelper.lerp(LAND_COLOR[i*2], LAND_COLOR[(i+1)*2], 0.5f);
                }
            }
            else
            {
                LAND_COLOR = new Color[land.size()];
                for(int i = 0; i < LAND_COLOR.length; i++)
                {
                    String[] rgb = land.get(i).split("[\\s,]+");
                    LAND_COLOR[i]=new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
                }
            }

        }
        catch(Exception xe)
        {
            xe.printStackTrace();
        }
    }

    public void load(File file)
    {
        FileReader fh = null;
        try
        {
            fh = new FileReader(file);
            this.load(fh);
        }
        catch(Exception xe)
        {
            xe.printStackTrace();
        }
        finally
        {
            try { fh.close(); } catch(Exception xe) {}
        }
    }

    public Color mapHeight(double h, double seaMin, double landMax)
    {
        h = MathHelper.ClampValue(h, seaMin, landMax);
        if(h<=seaMin)
        {
            return SEA_COLOR[0];
        }
        else
        if(h>=landMax)
        {
            return LAND_COLOR[LAND_COLOR.length-1];
        }
        else
        if(h<=0.0)
        {
            double hm = h*((double)SEA_COLOR.length-1.0)/seaMin;
            int hi = (int)Math.floor(hm);
            double fh = hm-(double)hi;

            if(hi >= SEA_COLOR.length-1) return SEA_COLOR[0];

            if(this.seaHardRamp)
            {
                return SEA_COLOR[SEA_COLOR.length-hi-1];
            }
            return MathHelper.lerp(SEA_COLOR[SEA_COLOR.length-hi-1], SEA_COLOR[SEA_COLOR.length-hi-2], fh);
        }
        else
        {
            double hm = h*((double)LAND_COLOR.length-1.1)/landMax;
            int hi = (int)Math.floor(hm);

            if(hi >= LAND_COLOR.length-1) return LAND_COLOR[LAND_COLOR.length-1];

            if(this.landHardRamp)
            {
                return LAND_COLOR[hi];
            }

            double fh = hm-(double)hi;
            return MathHelper.lerp(LAND_COLOR[hi],LAND_COLOR[hi+1], fh);
        }
    }

    @Override
    public String toHtml(double seaMin, double landMax)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<div>");
        for(Color c : SEA_COLOR)
        {
            sb.append("<div>");
            sb.append("<i style='background-color:#"+String.format("%06X", c.getRGB()&0xffffff)+"'>&nbsp; &nbsp; &nbsp;</i>");
            sb.append("</div>");
        }
        for(Color c : LAND_COLOR)
        {
            sb.append("<div>");
            sb.append("<i style='background-color:#"+String.format("%06X", c.getRGB()&0xffffff)+"'>&nbsp; &nbsp; &nbsp;</i>");
            sb.append("</div>");
        }
        sb.append("</div>");
        return sb.toString();
    }

}
