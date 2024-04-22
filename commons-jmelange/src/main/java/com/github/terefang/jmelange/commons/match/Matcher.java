package com.github.terefang.jmelange.commons.match;

import com.github.terefang.jmelange.commons.match.basic.*;

import java.util.Map;

public class Matcher
{
	
	public static final boolean match(Map obj, Filter filter)
	{
		return filter.match(obj);
	}

	public static final Filter parse(String filter)
	{
		return SimpleFilter.parse(filter);
	}

	public static final Filter parseQL(String filter)
	{
		return QLFilter.parse(filter);
	}

	public static final Filter keyValueFilter(IVariable k, IVariable v)
	{
		return new KeyValueFilter(k,v);
	}

	public static Filter likeFilter(IVariable k, IVariable v) { return LikeFilter.from(k,v); }

	public static Filter matchFilter(IVariable k, IVariable v) { return MatchFilter.from(k,v); }

	public static Filter ilikeFilter(IVariable k, IVariable v) { return LikeFilter.from(k,v,true); }

	public static Filter imatchFilter(IVariable k, IVariable v) { return MatchFilter.from(k,v,true); }

	public static Filter startswithFilter(IVariable k, IVariable v) { return StartsWithFilter.from(k,v); }
	public static Filter istartswithFilter(IVariable k, IVariable v) { return StartsWithFilter.from(k,v, true); }

	public static Filter endswithFilter(IVariable k, IVariable v) { return EndsWithFilter.from(k,v); }
	public static Filter iendswithFilter(IVariable k, IVariable v) { return EndsWithFilter.from(k,v, true); }

	public static Filter containsFilter(IVariable k, IVariable v) { return ContainsFilter.from(k,v); }
	public static Filter icontainsFilter(IVariable k, IVariable v) { return ContainsFilter.from(k,v, true); }
}
