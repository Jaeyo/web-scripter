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
	} //main
} //class
