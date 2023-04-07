package com.github.terefang.jmelange.commons.match;

import java.util.Iterator;

import com.github.terefang.jmelange.commons.match.basic.*;
import com.github.terefang.jmelange.commons.match.ql.*;

public class QLFilter extends AbstractFilter {

	private final Parser sel;
	private final ParseTreeNode tree;
	private final Filter filter;

	public QLFilter(String expr) throws Exception
	{
		this.sel = new Parser(new Lexer(expr));
		this.tree = this.sel.expr();

		this.filter = parseTree();
	}

	private Filter parseTree() {
		return parseNode(this.tree, false);
	}

	private Filter parseNode(ParseTreeNode _node, boolean _invert)
	{
		if(_node.getToken().getTokenType() == TokenType.KEYWORD_AND)
		{
			AndFilter _and = AndFilter.from();
			for (Iterator<ParseTreeNode> it = _node.children(); it.hasNext(); ) {
				ParseTreeNode _n = it.next();
				_and.add(parseNode(_n, false));
			}
			_and.setInvert(_invert);
			return _and;
		}
		else
		if(_node.getToken().getTokenType() == TokenType.KEYWORD_OR)
		{
			OrFilter _and = OrFilter.from();
			for (Iterator<ParseTreeNode> it = _node.children(); it.hasNext(); ) {
				ParseTreeNode _n = it.next();
				_and.add(parseNode(_n, false));
			}
			_and.setInvert(_invert);
			return _and;
		}
		else
		if(_node.getToken().getTokenType() == TokenType.KEYWORD_NOT)
		{
			if(_node.getChildCount() == 1)
			{
				return parseNode(_node.getChild(0), !_invert);
			}
			throw new IllegalArgumentException("not count "+_node.getChildCount());
		}
		else
		if(_node.getToken().getTokenType() == TokenType.LPAREN)
		{
			if(_node.getChildCount() == 1)
			{
				return parseNode(_node.getChild(0), _invert);
			}
			throw new IllegalArgumentException("lparen count "+_node.getChildCount());
		}
		else
		if(_node.getToken().getTokenType() == TokenType.EQUAL)
		{
			Filter _f = null;
			if(_node.getChildCount() != 2)
			{
				throw new IllegalArgumentException("eq count "+_node.getChildCount());
			}
			Token _tok = _node.getChild(1).getToken();
			if(_tok.getTokenType() == TokenType.STRING_LITERAL)
			{
				_f = Matcher.keyValueFilter(_node.getChild(0).getToken().getTokenValue(), _tok.getTokenValue().substring(1, _tok.getTokenValue().length()-1));
			}
			else
			if(_tok.getTokenType() == TokenType.LONG_LITERAL)
			{
				_f = new EqFilter(_node.getChild(0).getToken().getTokenValue(), _tok.getTokenValue());
			}
			else
			{
				_f = Matcher.keyValueFilter(_node.getChild(0).getToken().getTokenValue(), _tok.getTokenValue());
			}
			((AbstractFilter)_f).setInvert(_invert);
			return _f;
		}
		else
		if(_node.getToken().getTokenType() == TokenType.KEYWORD_LIKE)
		{
			Filter _f = null;
			if(_node.getChildCount() != 2)
			{
				throw new IllegalArgumentException("like count "+_node.getChildCount());
			}
			Token _tok = _node.getChild(1).getToken();
			if(_tok.getTokenType() == TokenType.STRING_LITERAL)
			{
				_f = Matcher.likeFilter(_node.getChild(0).getToken().getTokenValue(), _tok.getTokenValue().substring(1, _tok.getTokenValue().length()-1));
			}
			else
			{
				_f = Matcher.likeFilter(_node.getChild(0).getToken().getTokenValue(), _tok.getTokenValue());
			}
			((AbstractFilter)_f).setInvert(_invert);
			return _f;
		}
		else
		if(_node.getToken().getTokenType() == TokenType.KEYWORD_MATCH)
		{
			Filter _f = null;
			if(_node.getChildCount() != 2)
			{
				throw new IllegalArgumentException("match count "+_node.getChildCount());
			}
			Token _tok = _node.getChild(1).getToken();
			if(_tok.getTokenType() == TokenType.STRING_LITERAL)
			{
				_f = Matcher.matchFilter(_node.getChild(0).getToken().getTokenValue(), _tok.getTokenValue().substring(1, _tok.getTokenValue().length()-1));
			}
			else
			{
				_f = Matcher.matchFilter(_node.getChild(0).getToken().getTokenValue(), _tok.getTokenValue());
			}
			((AbstractFilter)_f).setInvert(_invert);
			return _f;
		}
		else
		if(_node.getToken().getTokenType() == TokenType.GT)
		{
			Filter _f = null;
			if(_node.getChildCount() != 2)
			{
				throw new IllegalArgumentException("gt count "+_node.getChildCount());
			}
			Token _tok = _node.getChild(1).getToken();
			if(_tok.getTokenType() == TokenType.LONG_LITERAL)
			{
				_f = new GtFilter(_node.getChild(0).getToken().getTokenValue(), _tok.getTokenValue());
			}
			else
			{
				throw new IllegalArgumentException("gt param "+_tok.getTokenValue());
			}
			((AbstractFilter)_f).setInvert(_invert);
			return _f;
		}
		else
		if(_node.getToken().getTokenType() == TokenType.GTE)
		{
			Filter _f = null;
			if(_node.getChildCount() != 2)
			{
				throw new IllegalArgumentException("gte count "+_node.getChildCount());
			}
			Token _tok = _node.getChild(1).getToken();
			if(_tok.getTokenType() == TokenType.LONG_LITERAL)
			{
				_f = new GteFilter(_node.getChild(0).getToken().getTokenValue(), _tok.getTokenValue());
			}
			else
			{
				throw new IllegalArgumentException("gte param "+_tok.getTokenValue());
			}
			((AbstractFilter)_f).setInvert(_invert);
			return _f;
		}
		else
		if(_node.getToken().getTokenType() == TokenType.LT)
		{
			Filter _f = null;
			if(_node.getChildCount() != 2)
			{
				throw new IllegalArgumentException("lt count "+_node.getChildCount());
			}
			Token _tok = _node.getChild(1).getToken();
			if(_tok.getTokenType() == TokenType.LONG_LITERAL)
			{
				_f = new LtFilter(_node.getChild(0).getToken().getTokenValue(), _tok.getTokenValue());
			}
			else
			{
				throw new IllegalArgumentException("lt param "+_tok.getTokenValue());
			}
			((AbstractFilter)_f).setInvert(_invert);
			return _f;
		}
		else
		if(_node.getToken().getTokenType() == TokenType.LTE)
		{
			Filter _f = null;
			if(_node.getChildCount() != 2)
			{
				throw new IllegalArgumentException("lte count "+_node.getChildCount());
			}
			Token _tok = _node.getChild(1).getToken();
			if(_tok.getTokenType() == TokenType.LONG_LITERAL)
			{
				_f = new LteFilter(_node.getChild(0).getToken().getTokenValue(), _tok.getTokenValue());
			}
			else
			{
				throw new IllegalArgumentException("lte param "+_tok.getTokenValue());
			}
			((AbstractFilter)_f).setInvert(_invert);
			return _f;
		}
		throw new IllegalArgumentException("unknown token "+_node.getToken().toString());
	}

	@Override
	public boolean doMatch(Object object)
	{
		return this.filter.match(object);
	}


	public static final QLFilter parse(String f)
	{
		try {
			return new QLFilter(f);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
