import com.github.terefang.jmelange.commons.CommonUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class ReflectCommons {
	public static void main(String[] args) {
		for(Method _m : CommonUtil.class.getMethods())
		{
			System.err.print(_m.getName()+" ( ");
			boolean _first = true;
			for(Type _a : _m.getGenericParameterTypes())
			{
				System.err.print((_first ? "":" , ")+_a.getTypeName());
				_first=false;
			}
			System.err.println(" ) -> "+_m.getReturnType().getSimpleName());
		}
	}
}
