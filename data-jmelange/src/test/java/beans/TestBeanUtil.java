package beans;

import com.github.terefang.jmelange.beans.BeanUtil;
import com.github.terefang.jmelange.commons.CommonUtil;
import lombok.Data;

public class TestBeanUtil
{
    public static void main(String[] args)
    {
        TestBean _bean = BeanUtil.mapToBean(CommonUtil.toMap(
                "nameWordStringAlt", "insnsiteive",
                "_B2", "True",
                "_B5", "True"
        ), TestBean.class);

        System.err.println(_bean.toString());
    }

    @Data
    static class TestBean
    {
        Long _l1 = 1L;
        long _l2 = -1L;
        Integer _i1 = 2;
        int _i2 = -2;
        boolean _b1 = true;
        Boolean _b2 = false;
        String _str = "Helloworld!";
        String nameWordString = "nws";
        String namewordstringalt = "nwsa";
    }
}
