package com.github.terefang.jmelange.pdf.ml.cmd;

import com.github.terefang.jmelange.pdf.ml.PmlParser;
import com.github.terefang.jmelange.pdf.ml.PmlParserContext;
import com.github.terefang.jmelange.pdf.ml.PmlParserUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.xml.pull.XmlPullParser;

@Slf4j
public enum PageLevelDirective
{
	lua() {
		@Override
		@SneakyThrows
		public void execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			PmlParserUtil.handleLua(_p, _xpc, _xpp);
		}
	},

	text() {
		@Override
		@SneakyThrows
		public void execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			_p.handlePageTextOrLabel(_xpp);
		}
	},

	label() {
		@Override
		@SneakyThrows
		public void execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			_p.handlePageTextOrLabel(_xpp);
		}
	},

	image() {
		@Override
		@SneakyThrows
		public void execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			_p.handlePageImage(_xpc, _xpp);
		}
	},

	svg() {
		@Override
		@SneakyThrows
		public void execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			_p.handlePageImage(_xpc, _xpp);
		}
	},

	h() {
		@Override
		@SneakyThrows
		public void execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			_p.handlePageHeading(_xpp);
		}
	},

	heading() {
		@Override
		@SneakyThrows
		public void execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			_p.handlePageHeading(_xpp);
		}
	},

	p() {
		@Override
		@SneakyThrows
		public void execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			_p.handlePageParagraph(_xpp);
		}
	},

	paragraph() {
		@Override
		@SneakyThrows
		public void execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			_p.handlePageParagraph(_xpp);
		}
	},

	table() {
		@Override
		@SneakyThrows
		public void execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			_p.handlePageTable(_xpc, _xpp);
		}
	},

	markdown() {
		@Override
		@SneakyThrows
		public void execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			_p.handlePageMarkdown(_xpc, _xpp);
		}
	},

	draw() {
		@Override
		@SneakyThrows
		public void execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			PmlParserUtil.handlePageDraw(_p,_xpc, _xpp);
		}
	},

	outline(){
		@Override
		@SneakyThrows
		public void execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			PmlParserUtil.handleOutlines(_p, _xpc, _xpp);
		}
	},

	reference(){
		@Override
		@SneakyThrows
		public void execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			PmlParserUtil.handleOutlines(_p, _xpc, _xpp);
		}
	},

	part(){
		@Override
		@SneakyThrows
		public void execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			PmlParserUtil.handleOutlines(_p, _xpc, _xpp);
		}
	},

	chapter(){
		@Override
		@SneakyThrows
		public void execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			PmlParserUtil.handleOutlines(_p, _xpc, _xpp);
		}
	},

	section() {
		@Override
		@SneakyThrows
		public void execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			PmlParserUtil.handleOutlines(_p, _xpc, _xpp);
		}
	},

	subsection() {
		@Override
		@SneakyThrows
		public void execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			PmlParserUtil.handleOutlines(_p, _xpc, _xpp);
		}
	},

	subsubsection() {
		@Override
		@SneakyThrows
		public void execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			PmlParserUtil.handleOutlines(_p, _xpc, _xpp);
		}
	}
	;

	public abstract void execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp);
}
