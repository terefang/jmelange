import com.github.terefang.jmelange.commons.CommonUtil;

public class TestStrings {
    static String TESTSTRING = "Hello World!";
    public static void main(String[] args) {
        System.err.println(CommonUtil.toHex(TESTSTRING));
        System.err.println(CommonUtil.toBase26(TESTSTRING));
        System.err.println(CommonUtil.toBase36(TESTSTRING));
        System.err.println(CommonUtil.toBase62(TESTSTRING));
        System.err.println(CommonUtil.toBase64(TESTSTRING));
        System.err.println(CommonUtil.toGuid(TESTSTRING));
        System.err.println(CommonUtil.toGuid(TESTSTRING,TESTSTRING));
        System.err.println(CommonUtil.toGuid(TESTSTRING,TESTSTRING+1));
    }
}
