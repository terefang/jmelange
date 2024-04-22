package com.github.terefang.jmelange.commons.match;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.github.terefang.jmelange.commons.match.basic.*;
import com.github.terefang.jmelange.commons.match.ql.*;
import com.github.terefang.jmelange.commons.util.BooleanUtil;
import com.github.terefang.jmelange.commons.util.NumberUtil;

public class QLFilter extends AbstractFilter {

	private Parser sel;
	private ParseTreeNode tree;
	private final Filter filter;

	public QLFilter(String expr) throws Exception
	{
		this.sel = new Parser(new Lexer(expr));
		this.tree = this.sel.expr();

		this.filter = parseTree();

		this.sel = null;
		this.tree = null;
	}

	private Filter parseTree() {
		return parseNode(this.tree, false);
	}

	private Filter parseNode(ParseTreeNode _node, boolean _invert)
	{
		switch(_node.getToken().getTokenType())
		{
			case TokenType.KEYWORD_IN:
			{
				return parseNodeIN(_node, _invert);
			}
			case TokenType.KEYWORD_AND:
			{
				return parseNodeAND(_node, _invert);
			}
			case TokenType.KEYWORD_OR:
			{
				return parseNodeOR(_node, _invert);
			}
			case TokenType.KEYWORD_NOT:
			{
				return parseNodeNOT(_node, _invert);
			}
			case TokenType.LPAREN:
			{
				return parseNodeLPAREN(_node, _invert);
			}
			case TokenType.EQUAL:
			{
				return parseNodeEQUAL(_node, _invert);
			}
			case TokenType.KEYWORD_LIKE:
			{
				return parseNodeLIKE(_node, _invert, false);
			}
			case TokenType.KEYWORD_MATCH:
			{
				return parseNodeMATCH(_node, _invert, false);
			}
			case TokenType.KEYWORD_ILIKE:
			{
				return parseNodeLIKE(_node, _invert, true);
			}
			case TokenType.KEYWORD_IMATCH:
			{
				return parseNodeMATCH(_node, _invert, true);
			}
			case TokenType.KEYWORD_STARTSWITH:
			{
				return parseNodeSTARTSWITH(_node, _invert, false);
			}
			case TokenType.KEYWORD_ISTARTSWITH:
			{
				return parseNodeSTARTSWITH(_node, _invert, true);
			}
			case TokenType.KEYWORD_ENDSWITH:
			{
				return parseNodeENDSWITH(_node, _invert, false);
			}
			case TokenType.KEYWORD_IENDSWITH:
			{
				return parseNodeENDSWITH(_node, _invert, true);
			}
			case TokenType.KEYWORD_CONTAINS:
			{
				return parseNodeCONTAINS(_node, _invert, false);
			}
			case TokenType.KEYWORD_ICONTAINS:
			{
				return parseNodeCONTAINS(_node, _invert, true);
			}
			case TokenType.GT:
			{
				return parseNodeGT(_node,_invert);
			}
			case TokenType.GTE:
			{
				return parseNodeGTE(_node,_invert);
			}
			case TokenType.LT:
			{
				return parseNodeLT(_node,_invert);
			}
			case TokenType.LTE:
			{
				return parseNodeLTE(_node,_invert);
			}
			case TokenType.IDENTIFIER:
			{
				if(_node.getChildCount()==0)
				{
					//standalone variable to be eval in bool context
					return VariableFilter.from(tokenToVariable(_node));
				}
				else
				{
					//function call
					String _funcName = _node.getToken().getTokenValue();
					List<IVariable> _args = new Vector<>();
					ParseTreeNode _cnode = _node.getChild(0);
					for(int _i= 0; _i<_cnode.getChildCount(); _i++)
					{
						_args.add(tokenToVariable(_cnode.getChild(_i)));
					}

					return FunctionCallFilter.from(_funcName, _args);
				}
			}
			case TokenType.DOT:
			case TokenType.ARROW:
			{
				// ab->cd OR ab.cd
				return VariableFilter.from(tokenToVariable(_node));
			}
			default:
			{
				throw new IllegalArgumentException("unknown token "+_node.toStringEx());
			}
		}
	}

	private Filter parseNodeIN(ParseTreeNode _node, boolean _invert)
	{
		// _VAR IN LIST(v1,...,vN)
		Filter _f = null;
		if(_node.getChildCount() != 2)
		{
			throw new IllegalArgumentException("IN count "+_node.getChildCount());
		}
		IVariable _var = tokenToVariable(_node.getChild(0));
		ParseTreeNode _cnode = _node.getChild(1);
		if(_cnode.getToken().getTokenType() != TokenType.KEYWORD_LIST)
		{
			throw new IllegalArgumentException("IN missing LIST");
		}

		int _len = _cnode.getChildCount();
		List<IVariable> _p = new Vector<>();
		for(int _i = 0; _i<_len; _i++)
		{
			_p.add(tokenToVariable(_cnode.getChild(_i)));
		}
		return InFilter.from(_var, _p);
	}

	private IVariable tokenToVariable(ParseTreeNode _node)
	{
		Token _tok = _node.getToken();
		if(_tok.getTokenType() == TokenType.DOT
			|| _tok.getTokenType() == TokenType.ARROW)
		{
			return IVariable.makeResolved(_node.getChild(0).getToken().getTokenValue(), _node.getChild(1).getToken().getTokenValue());
		}
		else
		if(_tok.getTokenType() == TokenType.IDENTIFIER && _node.getChildCount()==0)
		{
			return IVariable.makeResolved(_tok.getTokenValue());
		}
		else
		if(_tok.getTokenType() == TokenType.IDENTIFIER && _node.getChildCount()>0)
		{
			int _len = _node.getChild(0).getChildCount();
			List<IVariable> _p = new Vector<>();
			for(int _i = 0; _i<_len; _i++)
			{
				_p.add(tokenToVariable(_node.getChild(0).getChild(_i)));
			}
			return IVariable.make(_tok.getTokenValue(), _p);
		}
		else
		if(_tok.getTokenType() == TokenType.STRING_LITERAL)
		{
			return IVariable.make(_tok.getTokenValue().substring(1, _tok.getTokenValue().length()-1));
		}
		else
		if(_tok.getTokenType() == TokenType.LONG_LITERAL)
		{
			return IVariable.make(NumberUtil.checkLong(_tok.getTokenValue()));
		}
		else
		if(_tok.getTokenType() == TokenType.DOUBLE_LITERAL)
		{
			return IVariable.make(NumberUtil.checkDouble(_tok.getTokenValue()));
		}
		else
		if(_tok.getTokenType() == TokenType.BOOLEAN_LITERAL)
		{
			return IVariable.make(BooleanUtil.checkBoolean(_tok.getTokenValue()));
		}
		return IVariable.make(_tok.getTokenValue());
	}

	private Filter parseNodeMATCH(ParseTreeNode _node, boolean _invert, boolean insensitive)
	{
		Filter _f = null;
		if(_node.getChildCount() != 2)
		{
			throw new IllegalArgumentException("match count "+_node.getChildCount());
		}
		if(insensitive)
		{
			_f = Matcher.imatchFilter(tokenToVariable(_node.getChild(0)), tokenToVariable(_node.getChild(1)));
		}
		else
		{
			_f = Matcher.imatchFilter(tokenToVariable(_node.getChild(0)), tokenToVariable(_node.getChild(1)));
		}
		((AbstractFilter)_f).setInvert(_invert);
		return _f;
	}

	private Filter parseNodeSTARTSWITH(ParseTreeNode _node, boolean _invert, boolean insensitive)
	{
		Filter _f = null;
		if(_node.getChildCount() != 2)
		{
			throw new IllegalArgumentException("match count "+_node.getChildCount());
		}
		if(insensitive)
		{
			_f = Matcher.istartswithFilter(tokenToVariable(_node.getChild(0)), tokenToVariable(_node.getChild(1)));
		}
		else
		{
			_f = Matcher.startswithFilter(tokenToVariable(_node.getChild(0)), tokenToVariable(_node.getChild(1)));
		}
		((AbstractFilter)_f).setInvert(_invert);
		return _f;
	}

	private Filter parseNodeENDSWITH(ParseTreeNode _node, boolean _invert, boolean insensitive)
	{
		Filter _f = null;
		if(_node.getChildCount() != 2)
		{
			throw new IllegalArgumentException("match count "+_node.getChildCount());
		}
		if(insensitive)
		{
			_f = Matcher.iendswithFilter(tokenToVariable(_node.getChild(0)), tokenToVariable(_node.getChild(1)));
		}
		else
		{
			_f = Matcher.endswithFilter(tokenToVariable(_node.getChild(0)), tokenToVariable(_node.getChild(1)));
		}
		((AbstractFilter)_f).setInvert(_invert);
		return _f;
	}

	private Filter parseNodeCONTAINS(ParseTreeNode _node, boolean _invert, boolean insensitive)
	{
		Filter _f = null;
		if(_node.getChildCount() != 2)
		{
			throw new IllegalArgumentException("match count "+_node.getChildCount());
		}
		if(insensitive)
		{
			_f = Matcher.icontainsFilter(tokenToVariable(_node.getChild(0)), tokenToVariable(_node.getChild(1)));
		}
		else
		{
			_f = Matcher.containsFilter(tokenToVariable(_node.getChild(0)), tokenToVariable(_node.getChild(1)));
		}
		((AbstractFilter)_f).setInvert(_invert);
		return _f;
	}

	private Filter parseNodeGT(ParseTreeNode _node, boolean _invert)
	{
		Filter _f = null;
		if(_node.getChildCount() != 2)
		{
			throw new IllegalArgumentException("gt count "+_node.getChildCount());
		}
		Token _tok = _node.getChild(1).getToken();
		if(_tok.getTokenType() == TokenType.LONG_LITERAL
				|| _tok.getTokenType() == TokenType.DOUBLE_LITERAL
				|| _tok.getTokenType() == TokenType.IDENTIFIER)
		{
			_f = new GtFilter(tokenToVariable(_node.getChild(0)), tokenToVariable(_node.getChild(1)));
		}
		else
		{
			throw new IllegalArgumentException("gt param "+_tok.getTokenValue());
		}
		((AbstractFilter)_f).setInvert(_invert);
		return _f;
	}

	private Filter parseNodeGTE(ParseTreeNode _node, boolean _invert)
	{
		Filter _f = null;
		if(_node.getChildCount() != 2)
		{
			throw new IllegalArgumentException("gte count "+_node.getChildCount());
		}
		Token _tok = _node.getChild(1).getToken();
		if(_tok.getTokenType() == TokenType.LONG_LITERAL
				|| _tok.getTokenType() == TokenType.DOUBLE_LITERAL
				|| _tok.getTokenType() == TokenType.IDENTIFIER)
		{
			_f = new GteFilter(tokenToVariable(_node.getChild(0)), tokenToVariable(_node.getChild(1)));
		}
		else
		{
			throw new IllegalArgumentException("gte param "+_tok.getTokenValue());
		}
		((AbstractFilter)_f).setInvert(_invert);
		return _f;
	}

	private Filter parseNodeLT(ParseTreeNode _node, boolean _invert)
	{
		Filter _f = null;
		if(_node.getChildCount() != 2)
		{
			throw new IllegalArgumentException("lt count "+_node.getChildCount());
		}
		Token _tok = _node.getChild(1).getToken();
		if(_tok.getTokenType() == TokenType.LONG_LITERAL
				|| _tok.getTokenType() == TokenType.DOUBLE_LITERAL
				|| _tok.getTokenType() == TokenType.IDENTIFIER)
		{
			_f = new LtFilter(tokenToVariable(_node.getChild(0)), tokenToVariable(_node.getChild(1)));
		}
		else
		{
			throw new IllegalArgumentException("lt param "+_tok.getTokenValue());
		}
		((AbstractFilter)_f).setInvert(_invert);
		return _f;
	}

	private Filter parseNodeLTE(ParseTreeNode _node, boolean _invert)
	{
		Filter _f = null;
		if(_node.getChildCount() != 2)
		{
			throw new IllegalArgumentException("lte count "+_node.getChildCount());
		}
		Token _tok = _node.getChild(1).getToken();
		if(_tok.getTokenType() == TokenType.LONG_LITERAL
				|| _tok.getTokenType() == TokenType.DOUBLE_LITERAL
				|| _tok.getTokenType() == TokenType.IDENTIFIER)
		{
			_f = new LteFilter(tokenToVariable(_node.getChild(0)), tokenToVariable(_node.getChild(1)));
		}
		else
		{
			throw new IllegalArgumentException("lte param "+_tok.getTokenValue());
		}
		((AbstractFilter)_f).setInvert(_invert);
		return _f;
	}

	private Filter parseNodeAND(ParseTreeNode _node, boolean _invert)
	{
		AndFilter _and = AndFilter.from();
		for (Iterator<ParseTreeNode> it = _node.children(); it.hasNext(); ) {
			ParseTreeNode _n = it.next();
			_and.add(parseNode(_n, false));
		}
		_and.setInvert(_invert);
		return _and;
	}

	private Filter parseNodeOR(ParseTreeNode _node, boolean _invert)
	{
		OrFilter _and = OrFilter.from();
		for (Iterator<ParseTreeNode> it = _node.children(); it.hasNext(); ) {
			ParseTreeNode _n = it.next();
			_and.add(parseNode(_n, false));
		}
		_and.setInvert(_invert);
		return _and;
	}

	private Filter parseNodeNOT(ParseTreeNode _node, boolean _invert)
	{
		if(_node.getChildCount() == 1)
		{
			return parseNode(_node.getChild(0), !_invert);
		}
		throw new IllegalArgumentException("not count "+_node.getChildCount());
	}

	private Filter parseNodeLPAREN(ParseTreeNode _node, boolean _invert)
	{
		if(_node.getChildCount() == 1)
		{
			return parseNode(_node.getChild(0), _invert);
		}
		throw new IllegalArgumentException("lparen count "+_node.getChildCount());
	}

	private Filter parseNodeEQUAL(ParseTreeNode _node, boolean _invert)
	{
		Filter _f = null;
		if(_node.getChildCount() != 2)
		{
			throw new IllegalArgumentException("eq count "+_node.getChildCount());
		}
		_f = new EqFilter(tokenToVariable(_node.getChild(0)), tokenToVariable(_node.getChild(1)));
		((AbstractFilter)_f).setInvert(_invert);
		return _f;
	}

	private Filter parseNodeLIKE(ParseTreeNode _node, boolean _invert, boolean insensitive)
	{
		Filter _f = null;
		if(_node.getChildCount() != 2)
		{
			throw new IllegalArgumentException("like count "+_node.getChildCount());
		}
		if(insensitive)
		{
			_f = Matcher.ilikeFilter(tokenToVariable(_node.getChild(0)), tokenToVariable(_node.getChild(1)));
		}
		else
		{
			_f = Matcher.likeFilter(tokenToVariable(_node.getChild(0)), tokenToVariable(_node.getChild(1)));
		}
		((AbstractFilter)_f).setInvert(_invert);
		return _f;
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

	public interface QlFunction
	{
		boolean call(String _method, Object[] _args);
	}

}
