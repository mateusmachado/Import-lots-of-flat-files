package com.ilegra;


import java.io.File;

import org.junit.Assert;
import org.junit.Test;

public class MainTest {

	@Test
	public void testFilesOk() {
		Main main = new Main();
		File[] result = main.getListOfFiles("/Users/mateus/test/data/in/");
		Assert.assertEquals(result.length > 0, true);
	}
	
	@Test
	public void testFilesWrongDirectory() {
		Main main = new Main();
		File[] result = main.getListOfFiles("/Users/mateus/test/data/inaaa/");
		Assert.assertNull(result);
	}
	
}
