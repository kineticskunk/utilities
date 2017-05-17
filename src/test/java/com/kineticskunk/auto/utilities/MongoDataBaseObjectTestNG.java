package com.kineticskunk.auto.utilities;

import org.testng.annotations.BeforeClass;

import com.kineticskunk.utilities.MongoDataBaseObject;

public class MongoDataBaseObjectTestNG {
	
	private MongoDataBaseObject mdbo;

	public MongoDataBaseObjectTestNG() {
		// TODO Auto-generated constructor stub
	}
	
	@BeforeClass
	public void beforeClass() {
		mdbo = new MongoDataBaseObject("192.168.56.101:27017", "automationFramework");
	}

}
