import com.github.terefang.jmelange.pdf.ml.PmlToPdf;
import lombok.SneakyThrows;

public class PmlTest
{
    static String[] _ARGS = {
            "/data/fredo/_rpg/_0ddz.Engine/merp-csh.pml",
            "/data/fredo/_rpg/_0ddz.Engine/merp-csh.pdf",
    };

    @SneakyThrows
    public static void main(String[] args)
    {
        PmlToPdf.main(_ARGS);
    }
}
