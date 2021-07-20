package com.github.terefang.jmelange.randfractal.utils;

import com.github.terefang.jmelange.randfractal.Noisefield;
import com.github.terefang.jmelange.randfractal.map.ColorRamp;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class NoiseFieldUtil
{
    static class SColor extends java.awt.Color
    {
        public SColor(float r, float g, float b)
        { super(r, g, b); }
        public SColor(float r, float g, float b, float a)
        { super(r, g, b, a); }
        public SColor(int rgb)
        { super(rgb); }
        public SColor(int rgba, boolean hasalpha)
        { super(rgba, hasalpha); }
        public SColor(int r, int g, int b)
        { super(r, g, b); }
        public SColor(int r, int g, int b, int a)
        { super(r, g, b, a); }
        public SColor interpolate(SColor other, double d)
        {
            double inv = 1.0 - d;
            int a = (int) (other.getAlpha()*d + getAlpha()*inv);
            int r = (int) (other.getRed()*d + getRed()*inv);
            int g = (int) (other.getGreen()*d + getGreen()*inv);
            int b = (int) (other.getBlue()*d + getBlue()*inv);
            return new SColor(r,g,b,a);
        }
    }

    protected static int C2U(int r, int g, int b)
    {
        return (0xff000000|(r<<16)|(g<<8)|(b));
    }

    protected static int C2U(int r, int g, int b, int a)
    {
        return ((a<<24)|(r<<16)|(g<<8)|(b));
    }

    protected static int I2U(int x)
    {
        return ((x<<24)|(x<<16)|(x<<8)|(x));
    }

    public static void saveBImage(Noisefield nf, String fileName, String iType, double seaMax, double landMax)
    {
        // Create a buffered image in which to draw
        BufferedImage bufferedImage = new BufferedImage(nf.getWidth(), nf.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for(int y=0 ; y<nf.getHeight() ; ++y)
        {
            for(int x=0 ; x<nf.getWidth() ; ++x)
            {
                if(nf.getPoint(x,y)>=landMax)
                {
                    bufferedImage.setRGB(x,y,C2U(255,255,255));
                }
                else
                if(seaMax>=0.0)
                {
                    if(nf.getPoint(x,y)<0.0)
                    {
                        bufferedImage.setRGB(x,y,C2U(0, 0, 0));
                    }
                    else
                    {
                        double c = (255.0*(nf.getPoint(x,y)/landMax));
                        bufferedImage.setRGB(x,y,C2U((int)c, (int)c, (int)c));
                    }
                }
                else
                {
                    if(nf.getPoint(x,y)<=seaMax)
                    {
                        bufferedImage.setRGB(x,y,C2U(0,0,0));
                    }
                    else
                    if(nf.getPoint(x,y)<0.0)
                    {
                        double c = 127.0*(-nf.getPoint(x,y)/seaMax)+127.0;
                        bufferedImage.setRGB(x,y,C2U((int)c, (int)c, (int)c));
                    }
                    else
                    {
                        double c = (127.0*(nf.getPoint(x,y)/landMax))+127.0;
                        bufferedImage.setRGB(x,y,C2U((int)c, (int)c, (int)c));
                    }
                }
            }
        }

        if("png".equalsIgnoreCase(iType)
                || "jpg".equalsIgnoreCase(iType)
                || "bmp".equalsIgnoreCase(iType))
        {
            File file = new File(fileName);
            try
            {
                ImageIO.write(bufferedImage, iType, file);
            }
            catch(Exception e)
            {
                System.err.println(e.toString());
            }
        }
    }

    public static void saveTImage(Noisefield nf, String fileName, String iType, double seaMax, double landMax)
    {
        int landColor[][]={
                { 84, 194, 108 },
                {125, 196,  67 },
                {168, 186,  53 },
                {200, 169,  68 },
                {217, 154, 106 },
                {219, 147, 154 },
                {209, 152, 197 },
                {199, 168, 225 },
                {197, 190, 235 },
                {209, 215, 235 },
                {255, 255, 255 } };

        int seaColor[][]={
                { 97,  65, 247 },
                { 71,  89, 254 },
                { 53, 116, 243 },
                { 45, 143, 217 },
                { 0,  0, 160 } };

        // Create a buffered image in which to draw
        BufferedImage bufferedImage = new BufferedImage(nf.getWidth(), nf.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for(int y=0 ; y<nf.getHeight() ; ++y)
        {
            for(int x=0 ; x<nf.getWidth() ; ++x)
            {
                if(nf.getPoint(x,y)>=0.0)
                {
                    int sI=(int) ((nf.getPoint(x,y)/landMax)*(landColor.length-1));
                    if(sI>=(landColor.length-1))
                    {
                        bufferedImage.setRGB(x,y,C2U(landColor[(landColor.length-1)][0],landColor[(landColor.length-1)][1],landColor[(landColor.length-1)][2]));
                    }
                    else
                    {
                        SColor c1 = new SColor(landColor[sI][0],landColor[sI][1],landColor[sI][2]);
                        SColor c2 = new SColor(landColor[sI+1][0],landColor[sI+1][1],landColor[sI+1][2]);
                        SColor c3 = c1.interpolate(c2,(Math.abs(nf.getPoint(x,y))-Math.abs(sI*landMax/(landColor.length-1)))/(landMax/(landColor.length-1)));
                        bufferedImage.setRGB(x,y,c3.getRGB());
                    }
                }
                else
                {
                    int sI=(int)((nf.getPoint(x,y)/seaMax)*(seaColor.length-1));
                    if(sI>=(seaColor.length-1))
                    {
                        bufferedImage.setRGB(x,y,C2U(seaColor[seaColor.length-1][0],seaColor[seaColor.length-1][1],seaColor[seaColor.length-1][2]));
                    }
                    else
                    {
                        SColor c1 = new SColor(seaColor[sI][0],seaColor[sI][1],seaColor[sI][2]);
                        SColor c2 = new SColor(seaColor[sI+1][0],seaColor[sI+1][1],seaColor[sI+1][2]);
                        SColor c3 = c1.interpolate(c2,(Math.abs(sI*seaMax/(seaColor.length-1))-Math.abs(nf.getPoint(x,y)))/(seaMax/(seaColor.length-1)));
                        bufferedImage.setRGB(x,y,c3.getRGB());
                    }
                }
            }
        }

        if("png".equalsIgnoreCase(iType)
                || "jpg".equalsIgnoreCase(iType)
                || "bmp".equalsIgnoreCase(iType))
        {
            File file = new File(fileName);
            try
            {
                ImageIO.write(bufferedImage, iType, file);
            }
            catch(Exception e)
            {
                System.err.println(e.toString());
            }
        }
    }

    /*
     * function CloudExpCurve(v)
     *
     *   c = v - CloudCover
     *   if c < 0 then c=0
     *
     *   CloudDensity = 255 - ((CloudSharpness c) * 255)
     *
     *   return CloudDensity
     * end function
     */
    public static void saveCloudMap(Noisefield nf, String fileName, double cloudCover, double cloudSharpness)
    {
        Noisefield nf1 = nf.clone();
        nf1.normalize(0, 1);

        int blockStat[] = new int[256];
        for(int j=0; j<nf.getHeight(); j++)
        {
            for(int i=0; i<nf.getWidth(); i++)
            {
                blockStat[(int)(nf1.getPoint(i, j) * 0xff)]++;
            }
        }
        int sN=0;
        for(int j=255; j>=0; j--)
        {
            sN+=blockStat[j];
            blockStat[j]=sN;
        }

        double cloudCut=1;
        for(int j=255; j>=0; j--)
        {
            if((double)blockStat[j]/(double)sN > cloudCover)
            {
                cloudCut=1-((double)j/255.0);
                j=-1;
            }
        }

        BufferedImage bufferedImage = new BufferedImage(nf.getWidth(), nf.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for(int j=0; j<nf.getHeight(); j++)
        {
            for(int i=0; i<nf.getWidth(); i++)
            {
                double gx=nf1.getPoint(i, j)+cloudCut-1;
                if(gx<=0)
                {
                    gx=0;
                }
                else
                {
                    gx=255f* Math.pow(gx,cloudSharpness);
                }
                bufferedImage.setRGB(i, j, (
                        (((int)gx & 0xff) << 24)
                                |(((int)gx & 0xff) << 16)
                                |(((int)gx & 0xff) << 8)
                                |((int)gx & 0xff)));
            }
        }

        if(fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".bmp"))
        {
            File file = new File(fileName);
            try
            {
                ImageIO.write(bufferedImage, fileName.substring(fileName.length()-3), file);
            }
            catch(Exception e)
            {
                System.err.println(e.toString());
            }
        }
    }

    public static void saveNormalMap(Noisefield nf, String fileName, double bumpNess)
    {
        Noisefield nf1 = nf.clone();
        Noisefield nf2 = nf.clone();
        double fx[] = { 1, 0,-1,
                2, 0,-2,
                1, 0,-1 };
        double fy[] = { 1, 2, 1,
                0, 0, 0,
                -1,-2,-1 };

        nf1.normalize(0, 1);
        nf1.filterKernel(3, fx, 1.0);
        nf1.normalize(0, 1);

        nf2.normalize(0, 1);
        nf2.filterKernel(3, fy, 1.0);
        nf1.normalize(0, 1);

        BufferedImage bufferedImage = new BufferedImage(nf.getWidth(), nf.getHeight(), BufferedImage.TYPE_INT_RGB);
        for(int j=0; j<nf.getHeight(); j++)
        {
            for(int i=0; i<nf.getWidth(); i++)
            {
                double gx=nf1.getPoint(i, j);
                double gx2=gx*gx;
                double gy=nf2.getPoint(i, j);
                double gy2=gy*gy;

                double gz = bumpNess*Math.sqrt(1.0 - gx2 - gy2);
                double g = Math.sqrt(gx2 + gy2 + gz*gz);
                gx=256*gx/g;
                gy=256*gy/g;
                gz=256*gz/g;
                bufferedImage.setRGB(i, j, (((int)gx & 0xff) << 16)|(((int)gy & 0xff) << 8)|((int)gz & 0xff));
            }
        }

        if(fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".bmp"))
        {
            File file = new File(fileName);
            try
            {
                ImageIO.write(bufferedImage, fileName.substring(fileName.length()-3), file);
            }
            catch(Exception e)
            {
                System.err.println(e.toString());
            }
        }
    }

    public static void saveTImage(Noisefield nf, String fileName, double seaMax, double landMax)
    {
        saveTImage(nf, fileName, "png", seaMax, landMax);
    }

    public static void saveHF(Noisefield nf, String fileName)
    {
        try
        {
            File file = new File(fileName);
            FileOutputStream fh = new FileOutputStream(file);
            LEDataOutputStream dh = new LEDataOutputStream(fh);

            // HEAD_SIZE 16
            dh.writeBytes("BSTF");
            dh.writeInt(nf.getWidth()); // u32 SizeX;
            dh.writeInt(nf.getHeight()); // u32 SizeY;
            dh.writeFloat(30.0f); // f32 Scale;

            for(int i=0 ; i<nf.getWidth()*nf.getHeight() ; i++)
            {
                dh.writeFloat((float)nf.getData()[i]);
            }
            dh.writeBytes("EOF ");
            dh.close();
            fh.close();
        }
        catch(Exception e)
        {
            System.err.println(e.toString());
        }
    }

    public static void saveBT(Noisefield nf, String fileName)
    {
        try
        {
            File file = new File(fileName);
            OutputStream fh = new FileOutputStream(file);
            OutputStream bh = new BufferedOutputStream(fh, 1024*1024);
            if(fileName.endsWith(".gz") || fileName.endsWith(".btz"))
            {
                GZIPOutputStream gh = new GZIPOutputStream(bh);
                bh=gh;
            }
            LEDataOutputStream dh = new LEDataOutputStream(bh);

            // HEAD_SIZE 16
            //	Byte(Offset)	Length		Contents
            //		0			10			"binterr1.3"
            dh.writeBytes("binterr1.3");
            //		10			4 (int)		Columns
            dh.writeInt(nf.getWidth());
            //		14			4 (int)		Rows
            dh.writeInt(nf.getHeight());
            //		18			2 (short)	Data size (2,4 bytes)
            dh.writeShort(4);
            //		20			2 (short)	Floating-point flag (1 = float)
            dh.writeShort(1);
            //		22			2 (short)	Horizontal units
            //								0: Degrees
            //								1: Meters
            //								2: Feet (international foot = .3048 meters)
            //								3: Feet (U.S. survey foot = 1200/3937 meters)
            dh.writeShort(0);
            //		24			2 (short)	UTM zone
            dh.writeShort(0);
            // 		26			2 (short)	Datum
            dh.writeShort(0);
            //		28			8 (double)	Left extent
            dh.writeDouble(-1);
            //		36			8 (double)	Right extent
            dh.writeDouble(1);
            //		44			8 (double)	Bottom extent
            dh.writeDouble(-1);
            //		52			8 (double)	Top extent
            dh.writeDouble(1);
            //		60			2 (short)	External Projection (1 = extern .prj file)
            dh.writeShort(0);
            //		62			4 (float)	Vertical Units (0.0 => 1.0 compat)
            dh.writeInt(0);
            //		66-255		190			0x00 PAD
            for(int i=190; i>0; i--)
            {
                dh.writeByte(0);
            }

            for(int i=0; i<nf.getWidth(); i++)
            {
                for(int j=0; j<nf.getHeight(); j++)
                {
                    double gx = nf.getPoint(i, nf.getHeight()-(j+1));
                    dh.writeFloat((float)gx);
                }
            }
            dh.flush();
            bh.flush();
            dh.close();
            fh.close();
        }
        catch(Exception e)
        {
            System.err.println(e.toString());
        }
    }

    public static Noisefield loadBT(String fileName)
    {
        try
        {
            Noisefield nf;
            File file = new File(fileName);
            InputStream fh = new FileInputStream(file);
            InputStream bh = new BufferedInputStream(fh, 1024*1024);
            if(fileName.endsWith(".gz") || fileName.endsWith(".btz"))
            {
                GZIPInputStream gh = new GZIPInputStream(bh);
                bh=gh;
            }
            LEDataInputStream dh = new LEDataInputStream(bh);

            // HEAD_SIZE 16
            //	Byte(Offset)	Length		Contents
            //		0			10			"binterr1.3"
            byte[] buf = new byte[10];
            dh.readFully(buf);
            if(!"binterr1.3".equalsIgnoreCase(new String(buf)))
            {
                return null;
            }
            //		10			4 (int)		Columns
            int fW = dh.readInt();
            //		14			4 (int)		Rows
            int fH = dh.readInt();
            nf = new Noisefield(fW, fH);
            //		18			2 (short)	Data size (2,4 bytes)
            int dS = dh.readShort();
            //		20			2 (short)	Floating-point flag (1 = float)
            int fF = dh.readShort();
            //		22			2 (short)	Horizontal units
            //								0: Degrees
            //								1: Meters
            //								2: Feet (international foot = .3048 meters)
            //								3: Feet (U.S. survey foot = 1200/3937 meters)
            dh.readShort();
            //		24			2 (short)	UTM zone
            dh.readShort();
            // 		26			2 (short)	Datum
            dh.readShort();
            //		28			8 (double)	Left extent
            dh.readLong();
            //		36			8 (double)	Right extent
            dh.readLong();
            //		44			8 (double)	Bottom extent
            dh.readLong();
            //		52			8 (double)	Top extent
            dh.readLong();
            //		60			2 (short)	External Projection (1 = extern .prj file)
            dh.readShort();
            //		62			4 (float)	Vertical Units (0.0 => 1.0 compat)
            dh.readInt();
            //		66-255		190			0x00 PAD
            for(int i=190; i>0; i--)
            {
                dh.readByte();
            }

            for(int i=0; i<nf.getWidth(); i++)
            {
                for(int j=0; j<nf.getHeight(); j++)
                {
                    if(fF==1)
                    {
                        nf.setPoint(i, nf.getHeight() - (j + 1), dh.readFloat());
                    }
                    else if(dS==4)
                    {
                        nf.setPoint(i, nf.getHeight() - (j + 1), (double)dh.readInt());
                    }
                    else if(dS==2)
                    {
                        nf.setPoint(i, nf.getHeight() - (j + 1), (double)dh.readShort());
                    }
                }
            }
            dh.close();
            fh.close();
            return nf;
        }
        catch(Exception e)
        {
            System.err.println(e.toString());
        }
        return null;
    }

    public static void saveCRImage(Noisefield nf, String pngFileName, ColorRamp cRamp, double seaMax, double landMax) throws IOException
    {
        BufferedImage bufferedImage = new BufferedImage(nf.getWidth(), nf.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for(int y=0 ; y<nf.getHeight() ; ++y)
        {
            for(int x=0 ; x<nf.getWidth() ; ++x)
            {
                Color col = cRamp.mapHeight(nf.getPoint(x,y), seaMax, landMax);
                bufferedImage.setRGB(x,y, col.getRGB());
            }
        }

        File file = new File(pngFileName);
        FileOutputStream fo = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fo, 1024*1024);
        ImageIO.write(bufferedImage, "png", bos);
        bos.close();
    }

    public static void saveHFImage(Noisefield nf, String pngFileName) throws IOException
    {
        saveHFImage(nf,1f,pngFileName);
    }
    public static void saveHFImage(Noisefield nf, float sscale, String pngFileName) throws IOException
    {
        BufferedImage bufferedImage = new BufferedImage(nf.getWidth(), nf.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for(int y=0 ; y<nf.getHeight() ; ++y)
        {
            for(int x=0 ; x<nf.getWidth() ; ++x)
            {
                float _h = (float) nf.getPoint(x,y);
                Color col = new Color(_h/sscale,_h/sscale,_h/sscale);
                bufferedImage.setRGB(x,y, col.getRGB());
            }
        }

        File file = new File(pngFileName);
        FileOutputStream fo = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fo, 1024*1024);
        ImageIO.write(bufferedImage, "png", bos);
        bos.close();
    }
}
