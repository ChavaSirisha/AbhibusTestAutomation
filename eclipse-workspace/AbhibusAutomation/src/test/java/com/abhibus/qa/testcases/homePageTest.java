package com.abhibus.qa.testcases;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.abhibus.qa.base.testBase;
import com.abhibus.qa.pages.homePage;
import com.abhibus.qa.pages.searchPage;

public class homePageTest extends testBase {
	
	homePage homePageObj;
	searchPage searchPageObj;
	
	public homePageTest() {
		super();
	}
	
	@BeforeMethod
	public void setUp() {		
		initialization();
		homePageObj = new homePage();
		searchPageObj = new searchPage();
	}
	
	@Test(priority=1)
	public void validateLogoTooltip() {
		Assert.assertEquals(homePageObj.validateLogoTooltip(), prop.getProperty("abhibusLogoTooltip"));
	}
		
	//data driven from testng.xml
	@Parameters({"leavingFrom","goingTo"})
	@Test(priority=2)
	public void provideJourneyDetailsForSearchingBuses(String leavingFrom, String goingTo) {
		searchPageObj=homePageObj.provideJourneyDetailsForSearchingBuses(leavingFrom,goingTo);
	}		
	
	@AfterMethod
	public void tearDown() {
		driver.quit();	
		//driver.quit();	
	}

}
