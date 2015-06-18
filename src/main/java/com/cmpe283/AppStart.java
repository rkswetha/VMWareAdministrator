package com.cmpe283;

import java.io.IOException;

import com.cmpe283.ApplicationConfig;
import com.cmpe283.LogWriter;
import com.cmpe283.StatsCollector;


public class AppStart {

	public static void main(String[] args) throws IOException {
			
		ApplicationConfig.init();   //Initializing Configuration Utility
		StatsCollector.init();    //Initializing Statistics Generator
		
		LogWriter logGenerator = new LogWriter();
		logGenerator.setVirtualMachine(ApplicationConfig.getVM());
		logGenerator.setvHost(ApplicationConfig.getvHost());
		logGenerator.start();

	}

}
