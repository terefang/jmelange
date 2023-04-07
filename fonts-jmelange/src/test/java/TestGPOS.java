import com.github.terefang.jmelange.fonts.SfntUtil;
import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.table.FontDataTable;
import com.google.typography.font.sfntly.table.Table;
import lombok.SneakyThrows;

public class TestGPOS {
    @SneakyThrows
    public static void main(String[] args) {
        Font _font = SfntUtil.loadFont("/home/fredo/.fonts/Lato-Regular.ttf");
        FontDataTable _table = _font.getTable(Tag.GPOS);
        System.err.println(_table);
    }
}
