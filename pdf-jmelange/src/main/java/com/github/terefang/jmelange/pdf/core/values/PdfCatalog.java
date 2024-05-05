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
package com.github.terefang.jmelange.pdf.core.values;

import com.github.terefang.jmelange.commons.util.StringUtil;
import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.PdfDocument;

import java.io.IOException;
import java.io.OutputStream;

public class PdfCatalog extends PdfDictObject
{
	public static PdfCatalog create(PdfDocument doc)
	{
		return new PdfCatalog(doc);
	}
	
	public PdfCatalog(PdfDocument doc)
	{
		super(doc);
		this.set("Type", PdfName.of("Catalog"));
	}
	
	@Override
	public void writeTo(OutputStream os) throws IOException
	{
		/*
		PdfDictObjectWithStream _meta = PdfDictObjectWithStream.create(this.getDoc(), false);
		this.set("Metadata", _meta);
		_meta.set("Type", PdfName.of("Metadata"));
		_meta.set("Subtype", PdfName.of("XML"));
		IOUtil.copy(ClasspathResourceLoader.of("com/github/terefang/pdfgen/xmp/pdfx.xmp").getInputStream(), _meta.getOutputStream());
		*/
		super.writeTo(os);
	}

    public void makePdfX()
	{
		if(this.getDoc().getPdfxConformance()!= PDF.PDFX_NONE)
		{
			/*
				3 0 obj
				<<
					/DestOutputProfile 7 0 R
					/Info (GRACoL2006_Coated1v2.icc)
					/OutputCondition ()
					/OutputConditionIdentifier (Custom)
					/RegistryName ()
					/S /GTS_PDFX
					/Type /OutputIntent
				>>
				endobj
			*/
			PdfDictObject outputIntend = PdfDictObject.create(this.getDoc());
			outputIntend.setType(PdfName.OUTPUTINTENT);
			outputIntend.setAsName(PdfName.S, PdfName.GTS_PDFX);

			if(this.getDoc().getIccProfileType()!=0)
			{
				PdfDictObjectWithStream outputProfile = PdfDictObjectWithStream.create(this.getDoc());
				outputProfile.setAsNum(PdfName.N, this.getDoc().getIccProfileType());
				outputProfile.putStream(this.getDoc().getIccProfileData());
				outputIntend.set(PdfName.DESTOUTPUTPROFILE, outputProfile);

				outputIntend.setAsString(PdfName.OUTPUTCONDITION, "");
				outputIntend.setAsString(PdfName.OUTPUTCONDITIONIDENTIFIER, "Custom");
				outputIntend.setAsString(PdfName.REGISTRYNAME, "");
				String _info = this.getDoc().getIccProfileData().getName();
				if(_info==null) _info="";
				_info = StringUtil.replaceChars(_info, '\\', '/');
				if(_info.contains("/"))
				{
					_info = _info.substring(_info.lastIndexOf('/')+1);
				}
				outputIntend.setAsString(PdfName.INFO, _info);
			}
			else
			{
				outputIntend.setAsString(PdfName.OUTPUTCONDITION, "SWOP CGATS TR 001-1995");
				outputIntend.setAsString(PdfName.OUTPUTCONDITIONIDENTIFIER, "CGATS TR 001");
				outputIntend.setAsString(PdfName.REGISTRYNAME, "http://www.color.org");
				outputIntend.setAsString(PdfName.INFO, "");
			}

			this.set(PdfName.OUTPUTINTENTS, PdfArray.from(outputIntend));

			switch (this.getDoc().getPdfxConformance())
			{
				case PDF.PDFX_4_2008:
				case PDF.PDFX_4_2010:
				default:
					this.set(PDF.PDFX_INFO_KEY, PdfString.of(PDF.PDFX_4_NAME));
			}
		}
    }
}
