import com.github.terefang.jmelange.pdf.ml.PmlToPdf;
import lombok.SneakyThrows;

public class PmlTest
{
    static String[] _ARGS = {
            "-O",
            "/mnt/nas/fredos/_PROJECTS/_0ddz.Engine/coinage-xref.pml",
            "/mnt/nas/fredos/_PROJECTS/_0ddz.Engine/coinage-xref.pdf",
    };

    @SneakyThrows
    public static void main(String[] args)
    {
        PmlToPdf.main(_ARGS);
    }
}
