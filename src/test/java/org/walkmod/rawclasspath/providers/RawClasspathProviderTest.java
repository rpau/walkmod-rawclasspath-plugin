package org.walkmod.rawclasspath.providers;

import org.junit.Assert;
import org.junit.Test;
import org.walkmod.conf.entities.Configuration;
import org.walkmod.conf.entities.impl.ConfigurationImpl;

public class RawClasspathProviderTest {


	@Test
	public void testeable() {
		Assert.assertTrue(true);
	}

	

	@Test
	public void testClassLoaderIsSet() {
		RawClasspathProvider reader = new RawClasspathProvider();
		Configuration conf = new ConfigurationImpl();
		reader.setClasspath("target/classes;target/test-classes");
		reader.init(conf);
		reader.load();
		Assert.assertNotNull(conf.getParameters().get("classLoader"));
	}

}
