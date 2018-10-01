package sfi;

import java.io.IOException;

import org.junit.Test;

public class TestIndexTool {

	@Test
	public void testMain() throws IOException {
		IndexTool.main(new String[] {"-i", "src/test/resources/in", "-o", "target/out"});
	}

}
