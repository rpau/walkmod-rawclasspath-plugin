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
				String[] bootPath = System.getProperties().get("sun.boot.class.path").toString()
						.split(Character.toString(File.pathSeparatorChar));

				URL[] urlArray = new URL[paths.length + bootPath.length];
				int i = 0;
				for (String lib : bootPath) {

					try {
						urlArray[i] = new File(lib).toURI().toURL();
					} catch (MalformedURLException e) {
						throw new ConfigurationException("Invalid URL for the boot classpath entry " + lib,
								e.getCause());
					}

					i++;
				}

				for (String path : paths) {
					try {
						File file = new File(path);
						urlArray[i] = file.toURI().toURL();
					} catch (MalformedURLException e) {
						throw new ConfigurationException("Invalid path: " + paths[i], e);
					}
					i++;
				}

				URLClassLoader cl = new URLClassLoader(urlArray) {
					@Override
					protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
						Class<?> result = null;
						try {
							result = findClass(name);

						} catch (Throwable e) {

						}
						if (result != null) {
							return result;
						}

						return super.loadClass(name, resolve);
					}

					@Override
					public Class<?> loadClass(String name) throws ClassNotFoundException {
						return loadClass(name, false);
					}
				};
				configuration.getParameters().put("classLoader", cl);
			}
		}

	}

	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}

}
