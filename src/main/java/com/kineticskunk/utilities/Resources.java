package com.kineticskunk.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

public class Resources {
	
	public Properties properies;
	public InputStream is;
	public HashMap<String, Object> params;
	public String fileName;

	public Resources() {
		this.properies = new Properties();
		this.is = null;
		this.params = new HashMap<String, Object>();
		this.fileName = null;
	}
	
	public Resources(Builder b) {
		this.properies = b.properies;
		this.is = b.is;
		this.params = b.params;
		this.fileName = b.fileName;
	}
	
	public Properties convertFileToPropertiesObject(String fileName) throws FileNotFoundException, IOException {
		Resources r = new Resources.Builder(fileName).setFileAsInputStream().readInputStreamIntoProperiesObject().build();
		return r.properies;
	}
	
	public HashMap<String, Object> convertFileToHashMap(String fileName) throws FileNotFoundException, IOException {
		Resources r = new Resources.Builder(fileName).setFileAsInputStream().readInputStreamIntoProperiesObject().putAllProperties().build();
		return r.params;
	}
	
	public Properties convertResourceFileToPropertiesObject(String fileName) throws FileNotFoundException, IOException {
		Resources r = new Resources.Builder(fileName).setResourceInputStream().readInputStreamIntoProperiesObject().build();
		return r.properies;
	}
	
	public HashMap<String, Object> convertResourceFileToHashMap(String fileName) throws FileNotFoundException, IOException {
		Resources r = new Resources.Builder(fileName).setResourceInputStream().readInputStreamIntoProperiesObject().putAllProperties().build();
		return r.params;
	}

	public class Builder {

		public Properties properies;
		public InputStream is;
		public HashMap<String, Object> params;
		public String fileName;

		public Builder() {
			this.properies = new Properties();
			this.is = null;
			this.params = new HashMap<String, Object>();
		}

		public Builder(String fileName) {
			this();
			this.fileName = fileName;
		}

		public HashMap<String, Object> getParams() {
			return params;
		}

		/**
		 * Read file into the inputstream is
		 * 
		 * @param file
		 * @throws FileNotFoundException
		 */
		public Builder setFileAsInputStream() throws FileNotFoundException {
			this.is = new FileInputStream(new File(this.fileName));
			return this;
		}

		/**
		 * Read resource file (on class path) into the inputstream is
		 * 
		 * @param resourceFile
		 * @throws FileNotFoundException
		 */
		public Builder setResourceInputStream() throws FileNotFoundException {
			this.is =  this.getClass().getClassLoader().getResourceAsStream(this.fileName);
			return this;
		}
		
		/**
		 * Load InputStream into the Properties Object
		 * 
		 * @return
		 * @throws IOException
		 */
		public Builder readInputStreamIntoProperiesObject() throws IOException {
			this.properies.load(this.is);
			return this;
		}
		
		/**
		 * Put all contents of a Property object into a HashMap
		 * @return
		 * @throws IOException
		 */
		public Builder putAllProperties() throws IOException {
			try {
				Set<String> propertyNames = this.properies.stringPropertyNames();
				
				for (String Property : propertyNames) {
					if (this.properies.getProperty(Property).equalsIgnoreCase("")) {
						this.params.put(Property, "");
					} else if (this.properies.getProperty(Property).equals(null)) {
						this.params.put(Property, null);
					} else {
						this.params.put(Property, properies.getProperty(Property));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return this;
		}

		public Resources build() {
			return new Resources(this);
		}

	}

}
