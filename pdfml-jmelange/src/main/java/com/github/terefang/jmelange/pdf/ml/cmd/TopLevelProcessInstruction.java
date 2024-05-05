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
public enum TopLevelProcessInstruction
{
	info() {
		@Override
		public boolean execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			Properties _attributes = PmlParserUtil.toProperties(_xpp);
			if(_attributes.containsKey("author"))
			{
				_p._pdf.setAuthor(_attributes.getProperty("author"));
			}
			if(_attributes.containsKey("creator"))
			{
				_p._pdf.setCreator(_attributes.getProperty("creator"));
			}
			if(_attributes.containsKey("producer"))
			{
				_p._pdf.setProducer(_attributes.getProperty("producer"));
			}
			if(_attributes.containsKey("title"))
			{
				_p._pdf.setTitle(_attributes.getProperty("title"));
			}
			if(_attributes.containsKey("subject"))
			{
				_p._pdf.setSubject(_attributes.getProperty("subject"));
			}
			if(_attributes.containsKey("keywords"))
			{
				_p._pdf.setKeywords(_attributes.getProperty("keywords"));
			}
			return false;
		}
	},

	include() {
		@Override
		@SneakyThrows
		public boolean execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			String _src = _xpp.getAttributeValue(null, "src");
			_p._queue.push(_xpc);
			_p.pushFile(_xpc.getBasedir(), PmlUtil.sourceToFile(_src, _xpc.getBasedir(), _xpc.getFile().getParentFile()));
			return true;
		}
	},

	define() {
		@Override
		@SneakyThrows
		public boolean execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			Properties _attr = PmlParserUtil.toProperties(_xpp);
			_p.ATTRIBUTE_DEFAULTS.putAll(_attr);
			return false;
		}
	},

	defaults() {
		@Override
		@SneakyThrows
		public boolean execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			Properties _attr = PmlParserUtil.toProperties(_xpp);
			ResourceLoader _rl = PmlUtil.sourceToLoader(_attr.getProperty("src"), _xpc.getBasedir(), _xpc.getFile().getParentFile(), _p.ZIP_MOUNTS, _p.DIR_MOUNTS);
			_p.ATTRIBUTE_DEFAULTS.load(_rl.getInputStream());
			return false;
		}
	},

	outline(){
		@Override
		@SneakyThrows
		public boolean execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return PmlParserUtil.handleOutlines(_p, _xpc, _xpp);
		}
	},

	reference(){
		@Override
		@SneakyThrows
		public boolean execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return PmlParserUtil.handleOutlines(_p, _xpc, _xpp);
		}
	},

	part(){
		@Override
		@SneakyThrows
		public boolean execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return PmlParserUtil.handleOutlines(_p, _xpc, _xpp);
		}
	},

	chapter(){
		@Override
		@SneakyThrows
		public boolean execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return PmlParserUtil.handleOutlines(_p, _xpc, _xpp);
		}
	},

	section() {
		@Override
		@SneakyThrows
		public boolean execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return PmlParserUtil.handleOutlines(_p, _xpc, _xpp);
		}
	},

	subsection() {
		@Override
		@SneakyThrows
		public boolean execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return PmlParserUtil.handleOutlines(_p, _xpc, _xpp);
		}
	},

	subsubsection() {
		@Override
		@SneakyThrows
		public boolean execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return PmlParserUtil.handleOutlines(_p, _xpc, _xpp);
		}
	},

	mount() {
		@Override
		@SneakyThrows
		public boolean execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return PmlParserUtil.handleMountPi(_p, _xpc, _xpp);
		}
	},

	classdef() {
		@Override
		@SneakyThrows
		public boolean execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return PmlParserUtil.handleClassPi(_p, _xpc, _xpp);
		}
	},

	entity() {
		@Override
		@SneakyThrows
		public boolean execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return PmlParserUtil.handleEntityOrIconPi(_p, _xpc, _xpp, false);
		}
	},

	icon() {
		@Override
		@SneakyThrows
		public boolean execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			return PmlParserUtil.handleEntityOrIconPi(_p, _xpc, _xpp, true);
		}
	},

	font() {
		@Override
		@SneakyThrows
		public boolean execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			_p.executeProcessInstructionFont(_xpp, _xpc);
			return false;
		}
	},

	image() {
		@Override
		@SneakyThrows
		public boolean execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			_p.executeProcessInstructionSvgOrImage(_xpp, _xpc);
			return false;
		}
	},

	svg() {
		@Override
		@SneakyThrows
		public boolean execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			_p.executeProcessInstructionSvgOrImage(_xpp, _xpc);
			return false;
		}
	},

	option() {
		@Override
		@SneakyThrows
		public boolean execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
		{
			_p.executeProcessInstructionOption(_xpp, _xpc);
			return false;
		}
	},

	;

	public abstract boolean execute(String _instr, PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp);
}
