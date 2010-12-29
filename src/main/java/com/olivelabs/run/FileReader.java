package com.olivelabs.run;


import java.io.*;
import java.util.*;
import java.util.StringTokenizer;

public class FileReader {

	private String filePath;
	private HashMap<String, ServerAddress> serverAddress;
	public FileReader(String path) {
		this.filePath = path;
		try {
			serverAddress = new HashMap<String, ServerAddress>();
			FileInputStream fstream = new FileInputStream(filePath);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				StringTokenizer stringTokenizer = new StringTokenizer(strLine, "=");
				ServerAddress address = new ServerAddress();
				String placeHolder = stringTokenizer.nextToken();
				String placeHolder2 = stringTokenizer.nextToken();
				serverAddress.put(placeHolder, address);
				StringTokenizer stringTokenizerInner = new StringTokenizer(placeHolder2, ":");
				address.host = stringTokenizerInner.nextToken();
				address.port = stringTokenizerInner.nextToken();
				
			}
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

	}

	public HashMap<String, ServerAddress> getNodesConfiguration(){
		if(serverAddress != null)
			return serverAddress;
		else
			throw new RuntimeException("There was some error when reading node configuration!");
	}
	
	public static void main(String args[]) {
		FileReader reader = new FileReader("/home/kenshin/workspace/Load-Balancer/src/main/servers.txt");
	}
}
