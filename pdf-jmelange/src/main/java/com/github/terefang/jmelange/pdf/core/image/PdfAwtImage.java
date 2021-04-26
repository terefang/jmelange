/*
 * Copyright (c) 2019. terefang@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.terefang.jmelange.pdf.core.image;

import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.values.PdfArray;
import com.github.terefang.jmelange.pdf.core.values.PdfDictObjectWithStream;
import com.github.terefang.jmelange.pdf.core.values.PdfName;
import com.github.terefang.jmelange.pdf.core.values.PdfNum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.*;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public class PdfAwtImage extends PdfImage
{
	public static PdfAwtImage of(PdfDocument doc, BufferedImage i, String _compression, boolean _transparency, boolean _alpha, float _av, int _rot)
	{
		return new PdfAwtImage(doc, i, _compression, _transparency, _alpha, _av, _rot);
	}

	int w,h;
	PdfDictObjectWithStream _stream;

	public PdfAwtImage(PdfDocument doc, BufferedImage img, String _compression, boolean _transparency, boolean _alpha, float _av, int _rot)
	{
		super(doc);
		this.set("BitsPerComponent", PdfNum.of(8));
		this.set("ColorSpace", PdfName.of("DeviceRGB"));

		img = rotateImageByDegrees(img, _rot);

		this.set("Width", PdfNum.of(img.getWidth()));
		this.set("Height", PdfNum.of(img.getHeight()));

		try
		{
			if("rgb8".equalsIgnoreCase(_compression))
			{
				if(!_alpha && _transparency)
				{
					makeTmask(img);
				}

				makeRgb8(img, _transparency, _alpha);
			}
			else
			if("indexed".equalsIgnoreCase(_compression)
					|| "index".equalsIgnoreCase(_compression))
			{
				if(!_alpha && _transparency)
				{
					makeTmask(img);
				}

				makeIndexed(img, _transparency, _alpha);
			}
			else
			if("jpeg".equalsIgnoreCase(_compression)
					|| "jpg".equalsIgnoreCase(_compression))
			{
				if(!_alpha && _transparency)
				{
					makeTmask(img);
				}

				makeJpeg(img, _transparency, _alpha);
			}
			else
			if("grey".equalsIgnoreCase(_compression)
					|| "gray".equalsIgnoreCase(_compression))
			{
				if(!_alpha && _transparency)
				{
					makeTmask(img);
				}

				makeGray(img, false);
			}
			else
			if("dct-grey".equalsIgnoreCase(_compression)
					|| "dct-gray".equalsIgnoreCase(_compression))
			{
				if(!_alpha && _transparency)
				{
					makeTmask(img);
				}

				makeGray(img, true);
			}
			else
			if("mono".equalsIgnoreCase(_compression))
			{
				if(!_alpha && _transparency)
				{
					makeTmask(img);
				}

				makeMono(img);
			}
			else
			{
				makeRgb(img, _transparency, _alpha);
			}

			if(_alpha)
			{
				makeSmask(img, _av);
			}
		}
		catch(Exception xe)
		{
			//IGNORE
		}
		this.w=img.getWidth();
		this.h=img.getHeight();
	}

	public BufferedImage rotateImageByDegrees(BufferedImage _img, int _rot)
	{
		//return _img;

		if(_rot==0)
		{
			return _img;
		}

		BufferedImage rotated = new BufferedImage(_img.getHeight(), _img.getWidth(), _img.getType());

		for(int _x=0; _x<_img.getWidth(); _x++)
		{
			for(int _y=0; _y<_img.getHeight(); _y++)
			{
				if(_rot<0)
				{
					rotated.setRGB(_y,_x,_img.getRGB(_x,_y));
				}
				else
				{
					rotated.setRGB(_y,_img.getWidth()-_x-1,_img.getRGB(_x,_y));
				}
			}
		}

		return rotated;
	}

	@SneakyThrows
	private void makeRgb(BufferedImage img, boolean _transparency, boolean _alpha)
	{
		int[] rgb = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());

		this.setFlateFilter();

		if(!_alpha && _transparency)
		{
			this.set("Mask", PdfArray.from(0xfe,0xfe,0xfe,0xfe,0xfe,0xfe));
		}

		OutputStream out = this.getOutputStream();
		for(int c : rgb)
		{
			int _a = ((c>>>24) & 0xff);
			if(!_alpha && _transparency && ((c & 0xffffff)==0xfefefe)) c = 0xffffff;
			if(!_alpha && _transparency && (_a < 192)) c = 0xfefefe;
			out.write((byte)((c>>>16) & 0xff));
			out.write((byte)((c>>>8) & 0xff));
			out.write((byte)(c & 0xff));
		}
		out.flush();
	}

	@SneakyThrows
	private void makeJpeg(BufferedImage img, boolean _transparency, boolean _alpha)
	{
		img = toBufferedImage(img, null, BufferedImage.TYPE_3BYTE_BGR);
		this.setFilter(null);
		this.set("Filter", PdfName.of("DCTDecode"));

		try
		{
			final ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
			final MemoryCacheImageOutputStream _bout = new MemoryCacheImageOutputStream(this.getOutputStream());
			writer.setOutput(_bout);
			if(this.getDoc().getJpegCompression() < 0.8f)
			{
				JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
				jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				jpegParams.setCompressionQuality(this.getDoc().getJpegCompression());
				jpegParams.setDestinationType(ImageTypeSpecifier.createFromRenderedImage(img));

				writer.write(null, new IIOImage(img, null, null), jpegParams);
			}
			else
			{
				writer.write(img);
			}
			writer.dispose();
			_bout.flush();
			_bout.close();
		}
		catch(Exception _xe)
		{
			log.warn(_xe.getMessage(), _xe);
		}
	}

	@SneakyThrows
	private void makeIndexed(BufferedImage _img, boolean _transparency, boolean _alpha)
	{
		_img = toBufferedImage(_img, null, BufferedImage.TYPE_BYTE_INDEXED);

		IndexColorModel _cm = (IndexColorModel) _img.getColorModel();
		int _ms = _cm.getMapSize();

		_stream = PdfDictObjectWithStream.create(this.getDoc());
		_stream.setLzwFilter();
		this.set("ColorSpace", PdfArray.from(PdfName.of("Indexed"), PdfName.of("DeviceRGB"), PdfNum.of(_ms), _stream));
		OutputStream _cos = _stream.getOutputStream();
		for(int _i = 0; _i<_ms; _i++)
		{
			_cos.write((byte) (_cm.getRed(_i) & 0xff));
			_cos.write((byte) (_cm.getGreen(_i) & 0xff));
			_cos.write((byte) (_cm.getBlue(_i) & 0xff));
		}
		_cos.flush();

		int[] sid = _img.getData().getSamples(0,0, _img.getWidth(), _img.getHeight(), 0, new int[_img.getWidth()*_img.getHeight()]);

		this.setFlateFilter();
		OutputStream out = this.getOutputStream();
		for(int c : sid)
		{
			out.write((byte) (c & 0xff));
		}
		out.flush();
	}

	@SneakyThrows
	private void makeRgb8(BufferedImage _img, boolean _transparency, boolean _alpha)
	{
		_img = toBufferedImage(_img, null, BufferedImage.TYPE_INT_RGB);
		int[] _rgb = _img.getRGB(0, 0, _img.getWidth(), _img.getHeight(), null, 0, _img.getWidth());

		int _ms = 256;
		int _cm[] = new int[_ms];
		for(int _i : _rgb)
		{
			int _r = (_i>>>16) & 0xff;
			int _g = (_i>>>8) & 0xff;
			int _b = (_i) & 0xff;
			int _idx = toRgb8(_r, _g, _b);
			_cm[_idx] = _i;
		}

		_stream = PdfDictObjectWithStream.create(this.getDoc());
		_stream.setLzwFilter();
		this.set("ColorSpace", PdfArray.from(PdfName.of("Indexed"), PdfName.of("DeviceRGB"), PdfNum.of(_ms), _stream));
		OutputStream _cos = _stream.getOutputStream();
		for(int _i = 0; _i<_ms; _i++)
		{
			_cos.write((byte) ((_cm[_i]>>>16) & 0xff));
			_cos.write((byte) ((_cm[_i]>>>8) & 0xff));
			_cos.write((byte) (_cm[_i] & 0xff));
		}
		_cos.flush();

		this.setFlateFilter();
		OutputStream out = this.getOutputStream();
		for(int _i : _rgb)
		{
			int _r = (_i>>>16) & 0xff;
			int _g = (_i>>>8) & 0xff;
			int _b = (_i) & 0xff;
			int _idx = toRgb8(_r, _g, _b);
			out.write((byte) (_idx & 0xff));
		}
		out.flush();
	}

	int toRgb8(int r, int g, int b)
	{
		r = (r>>>5)&0x7;
		g = (g>>>6)&0x3;
		b = (b>>>5)&0x7;

		return (((r<<5) | (g<<3) | (b)) & 0xff);
	}

	int fromRgb8(int i)
	{
		int r = ((i>>>5)&0x7)<<5;
		r |= (r>>3);
		r |= (r>>6);
		int g = (i>>>3)&0x3;
		g |= (g<<2);
		g |= (g<<4);
		int b = (i&0x7)<<5;
		b |= (b>>3);
		b |= (b>>6);

		return ((((r&0xff)<<16) | ((g&0xff)<<8) | (b&0xff)) & 0xffffff);
	}

	@SneakyThrows
	private void makeMono(BufferedImage _img)
	{
		_img = toBufferedImage(_img, null, BufferedImage.TYPE_BYTE_BINARY);

		this.set("ColorSpace", PdfName.of("DeviceGray"));
		this.set("BitsPerComponent", PdfNum.of(1));
		int[] sid = _img.getData().getSamples(0,0, _img.getWidth(), _img.getHeight(), 0, new int[_img.getWidth()*_img.getHeight()]);

		this.setRleFilter();
		MemoryCacheImageOutputStream out = new MemoryCacheImageOutputStream(this.getOutputStream());
		for(int c : sid)
		{
			out.writeBit(c==0 ? 0 : 1);
		}
		out.flush();
	}

	@SneakyThrows
	private void makeGray(BufferedImage _img, boolean _dct)
	{
		_img = toBufferedImage(_img, null, BufferedImage.TYPE_BYTE_GRAY);

		this.set("ColorSpace", PdfName.of("DeviceGray"));
		if(_dct)
		{
			this.setFilter(null);
			this.set("Filter", PdfName.of("DCTDecode"));

			try
			{
				final ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
				final MemoryCacheImageOutputStream _bout = new MemoryCacheImageOutputStream(this.getOutputStream());
				writer.setOutput(_bout);
				if(this.getDoc().getJpegCompression() < 0.8f)
				{
					JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
					jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
					jpegParams.setCompressionQuality(this.getDoc().getJpegCompression());
					jpegParams.setDestinationType(ImageTypeSpecifier.createFromRenderedImage(_img));

					writer.write(null, new IIOImage(_img, null, null), jpegParams);
				}
				else
				{
					writer.write(_img);
				}
				writer.dispose();
				_bout.flush();
				_bout.close();
			}
			catch(Exception _xe)
			{
				log.warn(_xe.getMessage(), _xe);
			}
		}
		else
		{
			int[] sid = _img.getData().getSamples(0,0, _img.getWidth(), _img.getHeight(), 0, new int[_img.getWidth()*_img.getHeight()]);

			//this.setLzwFilter();
			this.setFlateFilter();
			OutputStream out = this.getOutputStream();
			for(int c : sid)
			{
				out.write((byte)(c & 0xff));
			}
			out.flush();
		}
	}

	@SneakyThrows
	private void makeSmask(BufferedImage img, float _av)
	{
		int[] rgb = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());

		PdfDictObjectWithStream _smask = PdfDictObjectWithStream.create(this.getDoc(), true);
		_smask.setType("XObject");
		_smask.setSubtype("Image");
		_smask.set("Height", PdfNum.of(img.getHeight()));
		_smask.set("Width", PdfNum.of(img.getWidth()));
		_smask.set("ColorSpace", PdfName.of("DeviceGray"));
		_smask.set("BitsPerComponent", PdfNum.of(8));
		OutputStream out = _smask.getOutputStream();
		for(int c : rgb)
		{
			float _af = (float) ((c>>>24) & 0xff);
			if(_av<0f)
			{
				_af /= (-_av/100f);
			}
			else
			{
				_af *= (_av/100f);
			}
			int _a = (int) _af;
			if(_af>255f) _a = 255;
			out.write((byte)_a);
		}
		out.flush();
		this.set("SMask", _smask);
	}

	@SneakyThrows
	private void makeTmask(BufferedImage img)
	{
		int[] rgb = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());

		PdfDictObjectWithStream _smask = PdfDictObjectWithStream.create(this.getDoc(), true);
		_smask.setType("XObject");
		_smask.setSubtype("Image");
		_smask.set("Height", PdfNum.of(img.getHeight()));
		_smask.set("Width", PdfNum.of(img.getWidth()));
		_smask.set("ColorSpace", PdfName.of("DeviceGray"));
		_smask.set("BitsPerComponent", PdfNum.of(8));
		OutputStream out = _smask.getOutputStream();
		for(int c : rgb)
		{
			int _a = ((c>>>24) & 0xff);
			if(_a < 192)
			{
				out.write((byte)0);
			}
			else
			{
				out.write((byte)255);
			}
		}
		out.flush();
		this.set("SMask", _smask);
	}

	@Override
	public double getWidth()
	{
		return this.w;
	}
	
	@Override
	public double getHeight()
	{
		return this.h;
	}

	@Override
	public void streamOut() throws IOException
	{
		if(_stream!=null)
		{
			_stream.streamOut();
		}
		super.streamOut();
	}
}
