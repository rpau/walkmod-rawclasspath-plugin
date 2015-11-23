/* 
  Copyright (C) 2015 Raquel Pau.
 
 Walkmod is free software: you can redistribute it and/or modify
 it under the terms of the GNU Lesser General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.
 
 Walkmod is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Lesser General Public License for more details.
 
 You should have received a copy of the GNU Lesser General Public License
 along with Walkmod.  If not, see <http://www.gnu.org/licenses/>.*/
package org.walkmod.rawclasspath.providers;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.walkmod.conf.ConfigurationException;
import org.walkmod.conf.ConfigurationProvider;
import org.walkmod.conf.entities.Configuration;

public class RawClasspathProvider implements ConfigurationProvider {

	private String classpath;

	private Configuration configuration;

	@Override
	public void init(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public void load() throws ConfigurationException {
		if (configuration != null) {

			if (classpath != null) {
				String[] paths = classpath.split(";");

				URL[] urlArray = new URL[paths.length];
				for (int i = 0; i < paths.length; i++) {
					try {
						File file = new File(paths[i]);
						urlArray[i] = file.toURI().toURL();
					} catch (MalformedURLException e) {
						throw new ConfigurationException("Invalid path: " + paths[i], e);
					}
				}

				URLClassLoader cl = new URLClassLoader(urlArray);
				configuration.getParameters().put("classLoader", cl);
			}
		}

	}

	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}

}
