package com.abhibus.qa.testcases;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.abhibus.qa.base.testBase;
import com.abhibus.qa.pages.homePage;
import com.abhibus.qa.pages.searchPage;

public class searchPageTest extends testBase {
	homePage homePageObj;
	searchPage searchPageObj;
	public searchPageTest() {
		super();
	}
	
	@BeforeMethod
	public void setUp() {		
		initialization();
		homePageObj = new homePage();
		searchPageObj = new searchPage();
	}
	
	@Parameters({"leavingFrom","goingTo"})
	@Test
	public void searchBusesBasedOnFilter(String leavingFrom, String goingTo) {
		searchPageObj=homePageObj.provideJourneyDetailsForSearchingBuses(leavingFrom,goingTo);
		searchPageObj.searchBusesBasedOnFilter();
	}
	
	@AfterMethod
	public void tearDown() {
		driver.quit();		
	}

}
