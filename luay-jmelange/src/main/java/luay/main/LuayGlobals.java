package luay.main;

import luay.vm.Globals;
import luay.vm.LuaTable;
import luay.vm.LuaValue;

public class LuayGlobals extends Globals
{
    public LuaTable cloneEnv()
    {
        return new LuaTable(this);
    }
}
