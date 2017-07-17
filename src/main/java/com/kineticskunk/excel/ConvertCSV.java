package com.kineticskunk.excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;

public class ConvertCSV {

	private String csv;
	private Pattern pattern;
	private JSONObject json;
	
	public ConvertCSV(String pattern) {
		this.csv = new String();
		this.pattern = Pattern.compile(pattern);
		this.json = new JSONObject();
	}

	public ConvertCSV(String pattern, String csv) {
		this(pattern);
		this.csv = csv;
	}

	public ConvertCSV(String pattern, String csv, JSONObject json) {
		this(pattern, csv);
		this.json = json;
	}

	public JSONObject convertCSVtoJSON(String direction, String csv) {
		switch (direction.toUpperCase()) {
		case "VERTICAL":
			this.json = convertVerticalCSV(csv);

			break;
		case "HORIZONTAL":

			break;
		}



		return this.json;
	}

	private JSONObject convertVerticalCSV(String csv) {
		
		JSONObject verticalJSON = new JSONObject();

		return verticalJSON;
	}

	private BufferedReader bufferedReaderCSV(String csv) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(csv));
			return in;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
