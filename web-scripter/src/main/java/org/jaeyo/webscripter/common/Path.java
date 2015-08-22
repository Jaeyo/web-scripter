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
		File webInfPath = new File(getPackagePath(), "WEB-INF");
		if(webInfPath.exists() == false) //in develop mode
			return new File(getPackagePath(), "../src/main/resources/WEB-INF");
		return webInfPath;
	} //getWebInfPath
} //class