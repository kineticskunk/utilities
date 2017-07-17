package com.kineticskunk.auto.utilities;

import java.net.UnknownHostException;

import org.junit.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.kineticskunk.utilities.ConfigurationLoader;
import com.kineticskunk.utilities.Converter;
import com.kineticskunk.utilities.MongoDataBaseObject;

public class MongoDataBaseObjectTestNG extends ConfigurationLoader {
	
	private MongoDataBaseObject mdbo;

	@Parameters( { "jsonFile" } )
	public MongoDataBaseObjectTestNG(String jsonFile) {
		this.loadConfigurationFile(jsonFile);
	}
	
	@BeforeClass
	@Parameters( { "mongoHost", "mongoPort", "mongoDB" } )
	public void beforeClass(String mongoHost, String mongoPort, String mongoDB) throws NumberFormatException, UnknownHostException {
		mdbo = MongoDataBaseObject.getInstance();
		mdbo.setMongoHost(mongoHost);
		mdbo.setMongoPort(Converter.toInteger(mongoPort));
		mdbo.setMongoDB(mongoDB);
		mdbo.setMongoClient();
		mdbo.setMongoDBObject();
		mdbo.setMondoDBObject(this.getConfiguration());
	}
	
	@Test (groups = "collections", priority = 0)
	public void verfiyCollectionExists() {
		Assert.assertTrue(mdbo.doesCollectionExist(mdbo.getMongoDB(), "clientportal"));
	}
	
	@Test (groups = "collections", priority = 0)
	public void verfiyClientPortal() {
		Assert.assertTrue(mdbo.getFieldValue("clientportal", "Commission").equalsIgnoreCase("Buy"));
	}
	
	@Test (groups = "collections", priority = 0)
	public void verfiyCompanyMandateDetaisl() {
		Assert.assertTrue(mdbo.getFieldValue("companymandatedetails", "COUNTRY").equalsIgnoreCase("South Africa"));
	}
	
	@Test (groups = "collections", priority = 0)
	public void verfiyProTrader() {
		Assert.assertTrue(mdbo.getFieldValue("protrader", "Autotrade").equalsIgnoreCase("Manual"));
	}

}
