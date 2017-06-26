package com.kineticskunk.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONObjectManipulation {

	private final Logger logger = LogManager.getLogger(JSONObjectManipulation.class.getName());
	private final Marker JSONREADER = MarkerManager.getMarker("JSONREADER");

	private JSONParser parser;
	private JSONObject jsonFile;
	private FileReader jsonReadFile;
	private JSONObject nestedJSONObject;
	private String keyValue;
	
	private static JSONObjectManipulation jom;

	public static JSONObjectManipulation getInstance() throws IOException {
		if (jom == null ) {
			synchronized (JSONObjectManipulation.class) {
				if (jom == null) {
					jom = new JSONObjectManipulation();
				}
			}
		}
		return jom;
	}
	
	public JSONObjectManipulation() {
		jom.parser = null;
		jom.jsonFile = null;
		jom.jsonReadFile = null;
		jom.nestedJSONObject = null;
		jom.keyValue = null;
	}
	
	private JSONObjectManipulationBuilder jomb = new JSONObjectManipulationBuilder();
	
	public JSONObjectManipulation(JSONObjectManipulationBuilder jomb) {
		jom.parser = jomb.parser;
		jom.jsonFile = jomb.jsonFile;
		jom.jsonReadFile = jomb.jsonReadFile;
		jom.nestedJSONObject = jomb.nestedJSONObject;
		jom.keyValue = jomb.keyValue;
	}

	public JSONObjectManipulation(String jsonFileName) throws IOException, ParseException {
		jom.jomb.setJSONReadFile(jsonFileName).parseJSON().build();
	}
	
	public String getJSONKeyValue(String[] keys) {
		for (int i = 0; i <= (keys.length - 1); i++) {
			if (i == 0) {
				jom.jomb.setJSONObject(jom.jomb.jsonFile, keys[0]).build();
			} else if (i > 1 && i < (keys.length - 1)) {
				jom.jomb.setJSONObject(jom.jomb.nestedJSONObject, keys[i]).build();
			} else if (i == (keys.length - 1)) {
				jom.jomb.getJSONKeyValue(keys[i]).build();
			}
		}
		return jom.keyValue;
	}

	
	private class JSONObjectManipulationBuilder {
		
		private JSONParser parser;
		private JSONObject jsonFile;
		private FileReader jsonReadFile;
		private JSONObject nestedJSONObject;
		private String keyValue;
		
		private JSONObjectManipulationBuilder() {
			this.parser = new JSONParser();;
			this.jsonFile = new JSONObject();
			this.jsonReadFile = null;
			this.nestedJSONObject = null;
			this.keyValue = null;
		}
		
		private JSONObjectManipulationBuilder setJSONReadFile(String jsonFileName) {
			try {
				this.jsonReadFile = new FileReader(new File(this.getClass().getClassLoader().getResource(jsonFileName).getPath()));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return this;
		}
		
		private JSONObjectManipulationBuilder parseJSON() {
			try {
				this.jsonFile = (JSONObject) jom.parser.parse(this.jsonReadFile);
			} catch (IOException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return this;
		}
		
		private JSONObjectManipulationBuilder setJSONObject(JSONObject jsonObject, String key) {
			if (this.jsonKeyExists(jsonObject, key)) {
				this.nestedJSONObject = (JSONObject) jsonObject.get(key);
			}
			return this;
		}
		
		private JSONObjectManipulationBuilder getJSONKeyValue(String key) {
			this.keyValue = this.nestedJSONObject.get(key).toString();
			return this;
		}
		
		private boolean jsonKeyExists(JSONObject jsonObject, String key) {
			if (jsonObject.containsKey(key)) {
				return true; 
			} else {
				//this.logger.error(JSONREADER, "JSONObject " + (char)34 + jsonObject.toJSONString() + (char)34 + " object doesn't contain key " + (char)34 + key + (char)34);
			}
			return false;
		}
		
		private JSONObjectManipulation build() {
			return new JSONObjectManipulation(this);
		}
	}

}
