package com.kineticskunk.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class ApplicationProperties {
	
	private static final Logger logger = LogManager.getLogger(Thread.currentThread().getName());
	private static final Marker APPLICATIONPROPERTIES = MarkerManager.getMarker("APPLICATIONPROPERTIES");
	
	private static ApplicationProperties ap;
	
	/**
	 * Singleton Constructor
	 */
	public static ApplicationProperties getInstance() {
		if (ap == null ) {
			synchronized (ApplicationProperties.class) {
				if (ap == null) {
					ap = new ApplicationProperties();
				}
			}
		}
		return ap;
	}
	
	private static final Properties prop = new Properties();
	
	/**
	 * Constructor
	 */
	public ApplicationProperties() {
	}
	
	/**
	 * 
	 * @param configFileLocation
	 * @param configFileName
	 * @return
	 * @throws FileNotFoundException
	 */
	private InputStream getInputStream(String configFileLocation, String configFileName) throws FileNotFoundException {
		if (!configFileLocation.endsWith(System.getProperty("file.separator"))) {
			configFileLocation = configFileLocation + System.getProperty("file.separator");
		}
		return ap.getResourceInputStream(configFileName);
	}
	
	/**
	 * 
	 * @param configFileName
	 * @return
	 * @throws FileNotFoundException
	 */
	private InputStream getResourceInputStream(String configFileName) throws FileNotFoundException {
		return this.getClass().getClassLoader().getResourceAsStream(configFileName);
	}
	
	
	
	/**
	 * 
	 * @param configFileName
	 * @throws IOException
	 */
	public void loadPropertiesFile(String configFileName) throws IOException{
		InputStream inputStream = ap.getResourceInputStream(configFileName);
		if (inputStream != null) {
			getProperties().load(inputStream);
		} else {
			
		}
		inputStream.close();
	}
	
	/**
	 * 
	 * @param configFileLocation
	 * @param configFileName
	 * @throws IOException
	 */
	public void loadPropertiesFile(String configFileLocation, String configFileName) throws IOException{
		InputStream inputStream = ap.getInputStream(configFileLocation, configFileName);
		if (inputStream != null) {
			getProperties().load(inputStream);
		} else {
		}
		inputStream.close();
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	private boolean isPropValueNull(String key) {
		return getProperties().get(key).equals(null);
	}
	
	/**
	 * 
	 * @param sKey
	 * @return
	 */
	private boolean keyExists(String sKey) {
		return getProperties().containsKey(sKey);
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getPropValue(String key) {
		if (!ap.keyExists(key)) {
			this.getLogger().info("Property '" + key + "' does not exist in the properties object");
			return null;
		}
		if (ap.isPropValueNull(key)) {
			ap.getLogger().info("Property '" + key + "' is NULL.");
			return null;
		}
		return getProperties().getProperty(key);
	}
	
	/**
	 * Add Property to properties object
	 * 
	 * @param key
	 * @param value
	 */
	public void addProperty(String key, String value) {
		if (ap.keyExists(key)) {
			getProperties().remove(key);
		}
		getProperties().put(key, value);
		
		if (!ap.keyExists(key)) {
			getLogger().info("Property '" + key + "' has NOT been stored in the properties object");
		}
	}
	
	public Properties readResourcePropertyFile(InputStream is)  throws IOException {
		this.getProperties().load(is);
		return getProperties();
	}
	
	public HashMap<String, Object> readResourcePropertyFile(String resourceFile) throws IOException {
		Properties prop = new Properties();
		try {
			File f = new File(this.getClass().getClassLoader().getResource(resourceFile).getPath());
			if (f.exists() == false) {
				this.getLogger().fatal(APPLICATIONPROPERTIES, resourceFile.toString() + " does not exist");
			} else {
				prop.load(this.getClass().getClassLoader().getResourceAsStream(resourceFile));
			}
		} catch (FileNotFoundException ex) {
			this.getLogger().fatal(APPLICATIONPROPERTIES, ex.getLocalizedMessage());
			return null;
		}
		return putPropertiesInHashTable(prop);
	}
	
	public File returnResourceAsFile(String resourceFile) throws IOException {
		return new File(this.convertInputStreamToStr(this.getClass().getClassLoader().getResourceAsStream(resourceFile)));
	}
	
	public String convertInputStreamToStr(InputStream is) throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString().trim();
		} else {
			return "";
		}
	}
	
	/**
	 * 
	 * @param prop
	 * @return
	 */
	public HashMap<String, Object> putPropertiesInHashTable(Properties prop) {
		HashMap<String, Object> propvals = new HashMap<String, Object>();
		Set<String> propertyNames = prop.stringPropertyNames();
		for (String Property : propertyNames) {
			if (prop.getProperty(Property).equalsIgnoreCase("")) {
				propvals.put(Property, "");
			} else if (prop.getProperty(Property).equals(null)) {
				propvals.put(Property, null);
			} else {
				propvals.put(Property, prop.getProperty(Property));
			}
		}
		return propvals;
	}
	
	/**
	 * 
	 * @param propertiesFile
	 * @return
	 * @throws IOException
	 */
	public Hashtable<String, Object> readResourcePropertyFile(Properties prop)  throws IOException {
		Hashtable<String, Object> propvals = new Hashtable<String, Object>();
		try {
			Set<String> propertyNames = prop.stringPropertyNames();
			for (String Property : propertyNames) {
				if (prop.getProperty(Property).equalsIgnoreCase("")) {
					propvals.put(Property, "");
				} else if (prop.getProperty(Property).equals(null)) {
					propvals.put(Property, null);
				} else {
					propvals.put(Property, prop.getProperty(Property));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return propvals;
	}
	
	/**
	 * 
	 * @param params
	 * @param propertiesFileLocation
	 * @param propertiesFile
	 * @return
	 * @throws IOException
	 */
	public HashMap<String, Object> readPropertyFile(HashMap<String, Object> params, String propertiesFile)  throws IOException {
		Properties prop = new Properties();
		InputStream inputStream = ap.getResourceInputStream(propertiesFile);
		try {
			prop.load(inputStream);
			Set<String> propertyNames = prop.stringPropertyNames();
			for (String Property : propertyNames) {
				if (prop.getProperty(Property).equalsIgnoreCase("")) {
					params.put(Property, "");
				} else if (prop.getProperty(Property).equals(null)) {
					params.put(Property, null);
				} else {
					params.put(Property, this.getProperties().getProperty(Property));
				}
			}
		} catch (FileNotFoundException ex) {
			this.getLogger().fatal(APPLICATIONPROPERTIES, ex.getLocalizedMessage());
		} catch (IOException ex) {
			this.getLogger().fatal(APPLICATIONPROPERTIES, ex.getLocalizedMessage());
		} catch (Exception ex) {
			this.getLogger().fatal(APPLICATIONPROPERTIES, ex.getLocalizedMessage());
		}
		return params;
	}

	public Logger getLogger() {
		return logger;
	}

	public Properties getProperties() {
		return prop;
	}
	
	
}