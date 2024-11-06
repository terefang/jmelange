package examples.easylayout;

import com.github.terefang.jmelange.swing.SwingHelper;
import com.github.terefang.jmelange.swing.easylayout.Constraint;
import com.github.terefang.jmelange.swing.easylayout.EasyLayout;
import com.github.terefang.jmelange.swing.widgets.voops.MiniColorChooser;
import com.github.terefang.jmelange.swing.widgets.voops.MiniColorChooserDialog;
import lombok.SneakyThrows;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Dimension;

public class TestEL
{
   
    @SneakyThrows
    public static void main(String[] args)
    {
        System.err.println(new MiniColorChooserDialog().showFuture().get());
    }
    
    public static void main_3(String[] args)
    {
        JFrame _frame = new JFrame();
        _frame.setPreferredSize(new Dimension(600,600));
        _frame.setResizable(true);
        _frame.setLocationRelativeTo(null);
        
        JPanel       _panel = SwingHelper.createEGrid(3,3,"Title Border");
        
        _panel.add(SwingHelper.createLabel("NW", Color.WHITE, Color.BLACK), Constraint.next(Constraint.ALIGN_NW));
        _panel.add(SwingHelper.createLabel("NNE", Color.WHITE, Color.BLACK), Constraint.next(2,Constraint.ALIGN_NF));
        
        _panel.add(SwingHelper.createLabel("West", Color.WHITE, Color.BLACK), Constraint.next(1,Constraint.ALIGN_FC));
        //FontComboBox      _fc = new FontComboBox(24);
        //        Dimension _sz = _fc.getPreferredSize();
        //        _sz.height=24;
        //        _sz.width=200;
        //        _fc.setPreferredSize(_sz);
        //        _sz.width=300;
        //        _fc.setMaximumSize(_sz);
        _panel.add(new MiniColorChooser(), Constraint.from(1,1,Constraint.ALIGN_FF));
        //_panel.add(SwingHelper.createLabel("0", Color.WHITE, Color.BLACK), Constraint.from(1,1,Constraint.ALIGN_CC));
        //TRenderedComboBox<IColorDef> _fc = SwingHelper.createIColorDefComboBox(MiscColorDef._GRUVBOX_COLORS);
        //_panel.add(new BasicTitledEditorPane("edit"), Constraint.from(1,1,Constraint.ALIGN_CC));
        
        _panel.add(SwingHelper.createLabel("East", Color.WHITE, Color.BLACK), Constraint.next(2,Constraint.ALIGN_EC));
        
        _panel.add(SwingHelper.createLabel("SSW", Color.WHITE, Color.BLACK), Constraint.next(2,Constraint.ALIGN_SF));
        _panel.add(SwingHelper.createLabel("SE", Color.WHITE, Color.BLACK), Constraint.next(Constraint.ALIGN_SE));
        
        _frame.add(_panel);
        _frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        _frame.pack();
        _frame.setVisible(true);
    }
    public static void main_2(String[] args)
    {
        JFrame _frame = new JFrame();
        _frame.setPreferredSize(new Dimension(600,600));
        _frame.setResizable(true);
        _frame.setLocationRelativeTo(null);
        
        JPanel       _panel = SwingHelper.createEGrid(3,3,"Title Border");
        
        _panel.add(SwingHelper.createLabel("NW", Color.WHITE, Color.BLACK), Constraint.next(Constraint.ALIGN_NW));
        _panel.add(SwingHelper.createLabel("North", Color.WHITE, Color.BLACK), Constraint.next(Constraint.ALIGN_NC));
        _panel.add(SwingHelper.createLabel("NE", Color.WHITE, Color.BLACK), Constraint.next(Constraint.ALIGN_NE));
        
        _panel.add(SwingHelper.createLabel("West", Color.WHITE, Color.BLACK), Constraint.next(Constraint.ALIGN_WC));
        _panel.add(SwingHelper.createLabel("0", Color.WHITE, Color.BLACK), Constraint.next(Constraint.ALIGN_CC));
        _panel.add(SwingHelper.createLabel("East", Color.WHITE, Color.BLACK), Constraint.next(Constraint.ALIGN_EC));
        
        _panel.add(SwingHelper.createLabel("SW", Color.WHITE, Color.BLACK), Constraint.next(Constraint.ALIGN_SW));
        _panel.add(SwingHelper.createLabel("South", Color.WHITE, Color.BLACK), Constraint.next(Constraint.ALIGN_SC));
        _panel.add(SwingHelper.createLabel("SE", Color.WHITE, Color.BLACK), Constraint.next(Constraint.ALIGN_SE));
        
        _frame.add(_panel);
        _frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        _frame.pack();
        _frame.setVisible(true);
    }
    public static void main_1(String[] args)
    {
        JFrame _frame = new JFrame();
        _frame.setPreferredSize(new Dimension(600,600));
        _frame.setResizable(true);
        _frame.setLocationRelativeTo(null);
        
        Border       _etch  = BorderFactory.createEtchedBorder((EtchedBorder.LOWERED));
        TitledBorder _title = BorderFactory.createTitledBorder(_etch, "Title Border");
        
        JPanel       _panel = new JPanel();
        _panel.setBorder(_title);
        _panel.setLayout(EasyLayout.from(3,3, 2));
        
        _panel.add(SwingHelper.createLabel("North", Color.WHITE, Color.BLACK), Constraint.from(1,0, Constraint.ALIGN_NC));
        _panel.add(SwingHelper.createLabel("N/fill", Color.WHITE, Color.RED), Constraint.from(1,0, Constraint.ALIGN_NF));
        _panel.add(SwingHelper.createLabel("F/C", Color.BLACK, Color.YELLOW), Constraint.from(0,0, 2,1, Constraint.ALIGN_FC));
        
        _panel.add(SwingHelper.createLabel("NE", Color.WHITE, Color.BLACK), Constraint.from(0,0, Constraint.ALIGN_NE));
        _panel.add(SwingHelper.createLabel("NW", Color.WHITE, Color.BLACK), Constraint.from(2,0, Constraint.ALIGN_NW));
        _panel.add(SwingHelper.createLabel("C/F", Color.BLACK, Color.YELLOW), Constraint.from(2,0, 1,2, Constraint.ALIGN_CF));
        
        
        _panel.add(SwingHelper.createLabel("South", Color.WHITE, Color.BLACK), Constraint.from(1,2, Constraint.ALIGN_SC));
        _panel.add(SwingHelper.createButton("South btn", () -> {}), Constraint.from(1,2, 2, Constraint.ALIGN_FC));
        _panel.add(SwingHelper.createLabel("S/fill", Color.WHITE, Color.RED), Constraint.from(1,2, 1,1, Constraint.ALIGN_SF));
        
        _panel.add(SwingHelper.createLabel("SE", Color.WHITE, Color.BLACK), Constraint.from(0,2, Constraint.ALIGN_SE));
        _panel.add(SwingHelper.createLabel("SW", Color.WHITE, Color.BLACK), Constraint.from(2,2, Constraint.ALIGN_SW));
        
        _panel.add(SwingHelper.createLabel("East", Color.WHITE, Color.BLACK), Constraint.from(0,1, Constraint.ALIGN_EC));
        _panel.add(SwingHelper.createLabel("East/fill", Color.WHITE, Color.RED), Constraint.from(0,1, Constraint.ALIGN_EF));
        
        _panel.add(SwingHelper.createLabel("West", Color.WHITE, Color.BLACK), Constraint.from(2,1, Constraint.ALIGN_WC));
        _panel.add(SwingHelper.createLabel("West/fill", Color.WHITE, Color.RED), Constraint.from(2,1, Constraint.ALIGN_WF));
        
        _panel.add(SwingHelper.createLabel("0", Color.WHITE, Color.BLACK), Constraint.from(1,1, Constraint.ALIGN_CC));
        _panel.add(SwingHelper.createLabel("1", Color.WHITE, Color.RED), Constraint.from(1,1, Constraint.ALIGN_FF));
        
        _frame.add(_panel);
        _frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        _frame.pack();
        _frame.setVisible(true);
    }
}
