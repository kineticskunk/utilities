package com.kineticskunk.auto.utilities;

import java.net.UnknownHostException;

import org.junit.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.kineticskunk.utilities.ConfigurationLoader;
import com.kineticskunk.utilities.MongoDataBaseObject;

public class MongoDataBaseObjectTestNG extends ConfigurationLoader {
	
	private MongoDataBaseObject mdbo;

	@Parameters( { "jsonFile" } )
	public MongoDataBaseObjectTestNG(String jsonFile) {
		super(jsonFile);
	}
	
	@BeforeClass
	@Parameters( { "mongoDBHost", "mongoDBPort", "mongoDBName" } )
	public void beforeClass(String mongoDBHost, String mongoDBPort, String mongoDBName) throws NumberFormatException, UnknownHostException {
		mdbo = new MongoDataBaseObject(mongoDBHost, Integer.valueOf(mongoDBPort), mongoDBName);
		mdbo.setMondoDBObject(this.getConfiguration());
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
