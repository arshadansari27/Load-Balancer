package com.olivelabs.run;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;

public class PropertyReader {
	private String filePath;
	private HashMap<String, String> properties;
	public PropertyReader(String path) {
		this.filePath = path;
			Properties props = new Properties();
			properties = new HashMap<String, String>();
			FileInputStream fstream;
			try {
				fstream = new FileInputStream(filePath);
		        props.load(fstream);
		        fstream = new FileInputStream(filePath);
			String strLine;
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				while ((strLine = br.readLine()) != null) {
					StringTokenizer stringTokenizer = new StringTokenizer(strLine, "=");
					String placeHolder = stringTokenizer.nextToken();
					String placeHolder2 = stringTokenizer.nextToken();
					properties.put(placeHolder, placeHolder2);
					
					
				}
				in.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// Close the input stream
			
			for(String key : properties.keySet()){
				System.out.println(key+"=>"+properties.get(key));
			}
			
	}

	public HashMap<String, String> getConfiguration(){
		if(properties != null)
			return properties;
		else
			throw new RuntimeException("There was some error when reading node configuration!");
	}
	
	public static void main(String args[]) {
		PropertyReader reader = new PropertyReader("/home/kenshin/workspace/Load-Balancer/src/main/resource/system.properties");
		
	}
}
