import com.github.terefang.jmelange.data.FilterCLI;

public class TestFilter {
    public static void main(String[] args) {
        FilterCLI.main(new String[]{
                "-f", "csv", "-t", "xlsx",
                "-L", "jexl",
                "script-cli/src/test/filter/map.jexl",
                "examples/data/test.csv",
                "examples/data/test.filter.xlsx"
        });
    }
}
