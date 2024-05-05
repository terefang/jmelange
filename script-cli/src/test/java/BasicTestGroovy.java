import com.github.terefang.jmelange.ScriptCli;

public class BasicTestGroovy
{
    public static void main(String[] args) {
        ScriptCli.main(new String[]{"-L","groovy","-s","script-cli/src/test/test.gy","--","1","2","3"});
    }
}
