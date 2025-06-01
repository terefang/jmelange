import com.github.terefang.jmelange.commons.util.ListMapUtil;
import com.github.terefang.jmelange.swing.SwingHelper;
import lombok.SneakyThrows;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.io.File;

public class TestFT  extends JFrame
{
    public static TestFT INSTANCE;
    
    @SneakyThrows
    public static void main(String[] args)
    {
        INSTANCE = new TestFT();
        INSTANCE.init();
    }
    
    private void init()
    {
        this.setMinimumSize(new Dimension(1024,768));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(SwingHelper.createButton("DO", ()->{
            SwingHelper.executeFileAndTypeOpenChooser(this,"choose fiel and Type","open", null,null,(_f,_t)->{
                System.out.println(_f);
                System.out.println(_t);
            }, null, ListMapUtil.toMap("k1","v1","k2","v2"), "type1", "type1");
        }));
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
    }
}
