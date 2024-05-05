package com.github.terefang.jmelange.pdf.ml.cmd;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.loader.ResourceLoader;
import com.github.terefang.jmelange.pdf.ml.PmlParser;
import com.github.terefang.jmelange.pdf.ml.PmlParserContext;
import com.github.terefang.jmelange.pdf.ml.PmlParserUtil;
import com.github.terefang.jmelange.pdf.ml.PmlUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.xml.pull.XmlPullParser;

import java.util.Properties;

@Slf4j
public enum TopLevelDirective
{
	document() {
		@Override
		@SneakyThrows
		public boolean executeStart(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			// IGNORE
			return false;
		}

		@Override @SneakyThrows
		public boolean executeEnd(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return false;
		}
	},

	lua() {
		@Override
		@SneakyThrows
		public boolean executeStart(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return PmlParserUtil.handleLua(_p, _xpc, _xpp);
		}

		@Override @SneakyThrows
		public boolean executeEnd(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return false;
		}
	},

	page() {
		@Override
		@SneakyThrows
		public boolean executeStart(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return PmlParserUtil.handlePage(_p, _xpc, _xpp);
		}

		@Override @SneakyThrows
		public boolean executeEnd(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			_p._page.streamOut();
			_p._page = null;
			return false;
		}
	},

	outline(){
		@Override
		@SneakyThrows
		public boolean executeStart(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return PmlParserUtil.handleOutlines(_p, _xpc, _xpp);
		}

		@Override @SneakyThrows
		public boolean executeEnd(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return false;
		}
	},

	reference(){
		@Override
		@SneakyThrows
		public boolean executeStart(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return PmlParserUtil.handleOutlines(_p, _xpc, _xpp);
		}

		@Override @SneakyThrows
		public boolean executeEnd(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return false;
		}
	},

	part(){
		@Override
		@SneakyThrows
		public boolean executeStart(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return PmlParserUtil.handleOutlines(_p, _xpc, _xpp);
		}

		@Override @SneakyThrows
		public boolean executeEnd(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return false;
		}
	},

	chapter(){
		@Override
		@SneakyThrows
		public boolean executeStart(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return PmlParserUtil.handleOutlines(_p, _xpc, _xpp);
		}

		@Override @SneakyThrows
		public boolean executeEnd(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return false;
		}
	},

	section() {
		@Override
		@SneakyThrows
		public boolean executeStart(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return PmlParserUtil.handleOutlines(_p, _xpc, _xpp);
		}

		@Override @SneakyThrows
		public boolean executeEnd(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return false;
		}
	},

	subsection() {
		@Override
		@SneakyThrows
		public boolean executeStart(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return PmlParserUtil.handleOutlines(_p, _xpc, _xpp);
		}

		@Override @SneakyThrows
		public boolean executeEnd(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return false;
		}
	},

	subsubsection() {
		@Override
		@SneakyThrows
		public boolean executeStart(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return PmlParserUtil.handleOutlines(_p, _xpc, _xpp);
		}

		@Override @SneakyThrows
		public boolean executeEnd(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return false;
		}
	}
	;

	public abstract boolean executeStart(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp);
	public abstract boolean executeEnd(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp);
}
