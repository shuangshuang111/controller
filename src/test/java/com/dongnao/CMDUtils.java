package com.dongnao;

import java.io.IOException;

public class CMDUtils {
	public static void execCMD() {
		Runtime runtime = Runtime.getRuntime();
		try {
			String cmdStr = "cmd /k cd " + System.getProperty("user.dir") + " && mvn dependency:tree > c:/a.txt";
			System.out.println(cmdStr);
			runtime.exec(cmdStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		execCMD();
	}
}