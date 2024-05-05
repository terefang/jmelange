import com.github.terefang.jmelange.pdf.ml.PmlToPdf;
import lombok.SneakyThrows;

public class PmlTest
{
    static String[] _ARGS = {
            "/nas/_PROJECTS/_0ddz.Engine/d6_csh.pml",
            "/nas/_PROJECTS/_0ddz.Engine/d6_csh.pdf",
    };

    @SneakyThrows
    public static void main(String[] args)
    {
        PmlToPdf.main(_ARGS);
    }
}
