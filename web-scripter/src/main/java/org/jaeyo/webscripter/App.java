package org.jaeyo.webscripter;

import org.jaeyo.webscripter.common.Conf;
import org.jaeyo.webscripter.rdb.DerbySchemaCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args) throws InterruptedException {
		logger.info("started");
	
		new DerbySchemaCreator().check();
		
		Conf.set(Conf.PORT, 1234);
	
		JettyServer jetty = new JettyServer();
		jetty.start();
		jetty.join();
		
		//TODO new database 등록할 때 필수항목 옆에 표시해놓기. script에서도 
	} //main
} //class
