package luay.vm.lib;

import luay.vm.LuaValue;

class TableLibFunction extends LibFunction {
	@Override
	public LuaValue call() {
		return argerror(1, "table expected, got no value");
	}
}
