package com.abhibus.qa.pages;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.abhibus.qa.base.testBase;

public class searchPage extends testBase {	
	
	public searchPage() {
		PageFactory.initElements(driver,this);
	}
	
	//Page Repository
	@FindBy(xpath="//div[@class='f-bustype-sec']/ul/li")
	List<WebElement> busTypeList;
		
	@FindBy(xpath="//span[@onclick=\"toggleClass('filtersOnward','f-operator-type-onward')\"]")
	WebElement operator;
	
	@FindBy(xpath="//div[@class='f-operator-checkbox filter-list']//ul/li")
	List<WebElement> TravelsList;
	
	@FindBy(xpath="//div[@class='f-d-time']//ul/li")
	List<WebElement> departureTimings;
	
	@FindBy(xpath="//div[@id='nobusesmsg']/div/h1")
	WebElement noBusesMsg;
	
	@FindBy(linkText="Price")
	WebElement priceLink;
	
	@FindBy(xpath="//div[@id='SerVicesDetOneway1']/div//div[@class='col3 booksts clearfix']//a[1]")
	WebElement topServiceToSelectAfterSorting;
	
	@FindBy(xpath="//div[@id='SerVicesDetOneway1']/div")
	List<WebElement> availableBusesList;
	
	@FindBy(xpath="//div[@id='seatSelect1']//div[@class='seats']/ul/li[@class='sleeper available']/a")
	List<WebElement> availableSleeperSeats;
	
	@FindBy(xpath="//div[@id='seatSelect1']//div[@class='seats']/ul/li[@class='seat available']/a")
	List<WebElement> availableSeats;
	
	@FindBy(xpath="//span[@id='totalfare']")
	WebElement totalFare;
	
	@FindBy(xpath="//select[@id='boardingpoint_id1']")
	WebElement boardingPointDropdown;
	
	@FindBy(xpath="//div[@class='red-landmark']")
	WebElement boardingPointLabel;
	
	public void searchBusesBasedOnFilter() {
		if(prop.getProperty("busType").contains(",")) { //if more than one bus type selected like AC, Sleeper..
			String[] busTypes = prop.getProperty("busType").split(",");
			if(busTypes.length>0) {
				for(int i=0;i<busTypes.length;i++) {
					for(WebElement busType: busTypeList)
					{
						if(busType.getText().equalsIgnoreCase(busTypes[i]))
								busType.click();
					}
				}
			}
		}
		else { //only 1 bus type selected
			for(WebElement busType: busTypeList)
			{
				if(busType.getText().equalsIgnoreCase(prop.getProperty("busType")))
						busType.click();
			}
		}
			
		if(prop.getProperty("travelAgentName").contains(",")) { //if more than one travel agent selected
			String[] travelAgentsList = prop.getProperty("travelAgentName").split(",");
			if(travelAgentsList.length > 0) {
				operator.click();
				for(int i=0;i<travelAgentsList.length;i++) {
					for(WebElement travelAgent: TravelsList)
					{
						if(travelAgent.getText().equalsIgnoreCase(travelAgentsList[i])) {
							travelAgent.click();					
						}
					}						
				}
			}
		}
		else { //only 1 travel agent selected
			operator.click();
			for(WebElement travelAgent: TravelsList)
			{
				if(travelAgent.getText().equalsIgnoreCase(prop.getProperty("travelAgentName"))) 
					travelAgent.click();
			}
		}
		for(WebElement departureTime:departureTimings) //
		{
			if(departureTime.getText().equalsIgnoreCase(prop.getProperty("departureTime")))
				departureTime.click();
		}
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -250)", ""); //to scroll up the page
		if(availableBusesList.size()<1) //if no services are available
			System.out.println(noBusesMsg.getText() + "please select different filters"); //provide different filters in properties file
		else {			
			/*click on price link to sort the services based on price.
			clicking 2 times. first time Descending order, next ascending order.
			once sorting is done, selecting the first service*/
			
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -250)", ""); //to scroll up the page
			priceLink.click();			
			priceLink.click();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			topServiceToSelectAfterSorting.click();		
		}
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 150)", ""); //scroll down the  page
		selectAvailableSeat();
		getTotalFareAmount();
		selectBoardingPoint();
	}
	
	public void selectAvailableSeat() {
		if(prop.getProperty("busType").contains(",")) {
			String[] busTypes = prop.getProperty("busType").split(",");
			if(busTypes.length>0) {
				for(int i=0;i<busTypes.length;i++)
				{
					if(busTypes[i].equalsIgnoreCase("Sleeper")) 
					{
						availableSleeperSeats.get(0).click();
					}
					else {
						availableSeats.get(0).click();
					}
				}
			}
		}
		else {
			if(prop.getProperty("busType").equalsIgnoreCase("sleeper")) {
				availableSleeperSeats.get(0).click();
			}
			else {
				availableSeats.get(0).click();
			}
		}
	}
	public void getTotalFareAmount() {
		System.out.println(totalFare.getText());
	}
	public void selectBoardingPoint() {
		Select boardingPoints = new Select(boardingPointDropdown);
		boardingPoints.selectByIndex(1);
		System.out.println("BoardingPoint: " + boardingPointLabel.getText());
	}
}
