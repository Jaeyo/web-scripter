package org.jaeyo.webscripter.common;

import java.io.File;

public class Path{
	public static File getPackagePath() {
		String jarPath = Path.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		File jarFile = new File(jarPath);
		File packagePath = jarFile.getParentFile();
		return packagePath;
	} // getPackagePath
	
	public static File getWebInfPath(){
		return new File(getPackagePath(), "../WEB-INF");
	} //getWebInfPath
} //class