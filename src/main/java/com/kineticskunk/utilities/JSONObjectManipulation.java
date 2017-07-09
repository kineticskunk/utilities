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
	private JSONObject json;
	private String jsonFileName;
	private String keyValue;

	
	public JSONObjectManipulation() {
		this.json = null;
		this.keyValue = null;
	}
	
	public JSONObjectManipulation(JSONObject json) {
		this();
		this.json = json;
	}
	
	public JSONObjectManipulation(JSONObject json, String jsonFileName) {
		this(json);
		this.json = json;
		this.jsonFileName = jsonFileName;
	}
	
	private JSONObjectManipulationBuilder jomb = new JSONObjectManipulationBuilder();
	
	public JSONObjectManipulation(JSONObjectManipulationBuilder jomb) {
		this.keyValue = jomb.keyValue;
	}

	public JSONObjectManipulation(String jsonFileName) throws IOException, ParseException {
		this.jomb.setJSONReadFile(jsonFileName).parseJSON().build();
	}
	
	public void setJSONReadFile(String jsonFileName) {
		try {
			this.jsonReadFile = new FileReader(new File(this.getClass().getClassLoader().getResource(jsonFileName).getPath()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
				this.jsonFile = (JSONObject) this.parser.parse(this.jsonReadFile);
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
		
		public JSONObjectManipulationBuilder getJSONKeyValue(String[] keys) {
			for (int i = 0; i <= (keys.length - 1); i++) {
				if (i == 0) {
					this.setJSONObject(this.jsonFile, keys[0]).build();
				} else if (i > 1 && i < (keys.length - 1)) {
					this.setJSONObject(this.nestedJSONObject, keys[i]).build();
				} else if (i == (keys.length - 1)) {
					this.getJSONKeyValue(keys[i]).build();
				}
			}
			
			return this;
		}
		
		private JSONObjectManipulation build() {
			return new JSONObjectManipulation(this);
		}
	}

}
