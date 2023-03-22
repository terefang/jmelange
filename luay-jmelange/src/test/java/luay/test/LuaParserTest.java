package luay.test;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static org.junit.jupiter.api.Assertions.fail;

import luay.vm.parser.LuaParser;

public class LuaParserTest extends CompilerTest {

	@Override
	protected void doTest(String name) {
		try {
			LuaParser parser = new LuaParser(inputStreamOfLua(name), ISO_8859_1.name());
			parser.Chunk();
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}
}
