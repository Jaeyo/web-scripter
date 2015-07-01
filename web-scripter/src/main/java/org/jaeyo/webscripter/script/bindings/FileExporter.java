package org.jaeyo.webscripter.script.bindings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import org.jaeyo.webscripter.common.SpringBeans;
import org.jaeyo.webscripter.outputfiledelete.OutputFileLastModified;
import org.jaeyo.webscripter.statistics.FileWriteStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileExporter {
	private static final Logger logger = LoggerFactory.getLogger(FileExporter.class);

	public void write(String filename, String content) {
		write(filename, content, null);
	} // write
	
	public void write(String filename, String content, String charsetName){
		if(content==null)
			return;
		
		File exportFile = new File(filename);

		try {
			if(exportFile.getParentFile()!=null && !exportFile.getParentFile().exists())
				exportFile.getParentFile().mkdirs();
			if (!exportFile.exists())
				exportFile.createNewFile();

			Charset charset=null;
			if(charsetName==null)
				charset=Charset.defaultCharset();
			else
				charset=Charset.forName(charsetName);
			
			PrintWriter output = new PrintWriter(new OutputStreamWriter(new FileOutputStream(exportFile, false), charset));
			output.println(content);
			output.flush();
			output.close();
			
			OutputFileLastModified.getInstance().pushLastModifiedTime(exportFile.getAbsolutePath(), System.currentTimeMillis());
	
			incrementStatistics();
		} catch (IOException e) {
			logger.error("", e);
		} //catch
	} //write
	
	public void append(String filename, String content){
		append(filename, content, null);
	} //append

	public void append(String filename, String content, String charsetName){
		if(content==null)
			return;
		
		File exportFile = new File(filename);

		try {
			if(exportFile.getParentFile()!=null && !exportFile.getParentFile().exists())
				exportFile.getParentFile().mkdirs();
			if (!exportFile.exists())
				exportFile.createNewFile();

			Charset charset=null;
			if(charsetName==null)
				charset=Charset.defaultCharset();
			else
				charset=Charset.forName(charsetName);
			
			PrintWriter output = new PrintWriter(new OutputStreamWriter( new FileOutputStream(exportFile, true), charset));
			output.append(content);
			output.flush();
			output.close();
			
			OutputFileLastModified.getInstance().pushLastModifiedTime(exportFile.getAbsolutePath(), System.currentTimeMillis());
	
			incrementStatistics();
		} catch (IOException e) {
			logger.error("", e);
		} // catch
	} //append
	
	public void createFile(String filename) {
		File file = new File(filename);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				logger.error("", e);
			} // catch
		} // if
	} // createFile
	
	private void incrementStatistics(){
		long scriptSequence = Long.parseLong(Thread.currentThread().getName().replace("ScriptThread-", ""));
		SpringBeans.getBean(FileWriteStatistics.class).incrementCount(scriptSequence);
	} //incrementStatistics
} // class