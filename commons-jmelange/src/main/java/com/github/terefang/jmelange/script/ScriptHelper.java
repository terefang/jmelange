package com.github.terefang.jmelange.script;

import com.github.terefang.jmelange.commons.CommonUtil;

public class ScriptHelper extends CommonUtil
{
    private static ScriptHelper INSTANCE = new ScriptHelper();

    public static ScriptHelper create()
    {
        return ScriptHelper.INSTANCE;
    }
}
