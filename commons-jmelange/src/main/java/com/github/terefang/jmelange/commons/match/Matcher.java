package com.github.terefang.jmelange.commons.match;

import com.github.terefang.jmelange.commons.match.basic.KeyValueFilter;
import com.github.terefang.jmelange.commons.match.basic.LikeFilter;
import com.github.terefang.jmelange.commons.match.basic.MatchFilter;

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

	public static final Filter keyValueFilter(String k, String v)
	{
		return new KeyValueFilter(k,v);
	}

	public static Filter likeFilter(String k, String v) { return LikeFilter.from(k,v); }

	public static Filter matchFilter(String k, String v) { return MatchFilter.from(k,v); }
}
