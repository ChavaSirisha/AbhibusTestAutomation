package com.abhibus.qa.pages;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.abhibus.qa.base.testBase;

public class homePage extends testBase{
	
	//Object Repositories
	@FindBy(xpath="//img[contains(@src,'logo1.png')]")
	WebElement abhibusLogo;
	
	@FindBy(id="source")
	WebElement leavingFrom;
	
	@FindBy(id="destination")
	WebElement goingTo;
	
	@FindBy(xpath="//ul[@class='ui-autocomplete ui-front ui-menu ui-widget ui-widget-content']/li")
	List<WebElement> leavingFromList;
	
	@FindBy(xpath="//ul[@id='ui-id-2']/li")
	List<WebElement> goingToList;
	
	@FindBy(name="journey_date")
	WebElement dateOfJourney;
	
	@FindBy(name="journey_rdate")
	WebElement dateOfReturnJourney;
	
	@FindBy(xpath="//div[@class='ui-datepicker-group ui-datepicker-group-first']//div[@class='ui-datepicker-title']/span[@class='ui-datepicker-month']")
	WebElement currentMonthOfLeavingFrom;
	
	@FindBy(xpath="//div[@class='ui-datepicker-group ui-datepicker-group-first']//div[@class='ui-datepicker-title']/span[@class='ui-datepicker-year']")
	WebElement currentYearOfLeavingFrom;
	
	@FindBy(xpath="//div[@class='ui-datepicker-group ui-datepicker-group-last']//div[@class='ui-datepicker-title']//span[@class='ui-datepicker-month']")
	WebElement nextMonthOfLeavingFrom;
	
	@FindBy(xpath="//div[@class='ui-datepicker-group ui-datepicker-group-last']//div[@class='ui-datepicker-title']//span[@class='ui-datepicker-year']")
	WebElement nextYearOfLeavingFrom;
	
	@FindBy(xpath="//div[@class='ui-datepicker-group ui-datepicker-group-last']//span[@class='ui-icon ui-icon-circle-triangle-e']")
	WebElement nextButtonInCalendar;	
	
	@FindBy(xpath="//div[@class='ui-datepicker-group ui-datepicker-group-first']//table[@class='ui-datepicker-calendar']/tbody/tr/td/a")
	List<WebElement> totalActiveDatesInFirstCalendar;
	
	@FindBy(xpath="//div[@class='ui-datepicker-group ui-datepicker-group-last']//table[@class='ui-datepicker-calendar']/tbody/tr/td/a")
	List<WebElement> totalActiveDatesInLastCalendar;
	
	@FindBy(xpath="//a[@title='Search Buses']")
	WebElement searchButton;
	
	public homePage() {
		PageFactory.initElements(driver, this);
	}
	
	public String validateLogoTooltip() {
		Actions action = new Actions(driver);
		action.moveToElement(abhibusLogo).perform();		
		String abhibusLogoTooltip = abhibusLogo.getAttribute("title");
		
		return abhibusLogoTooltip;
	}	
		
	public searchPage provideJourneyDetailsForSearchingBuses(String source, String destination) {
		selectSource(source);
		selectDestination(destination);
		selectDateOfJourney();
		selectReturnDateOfJourney();
		searchButton.click();			
		return new searchPage();		
	}
	
	public void selectSource(String source) {
		leavingFrom.clear();
		leavingFrom.sendKeys(source);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		for (WebElement element:leavingFromList)
		{
		    if(element.getText().contains(source))
		        element.click();
		}
	}
	public void selectDestination(String destination) {
		goingTo.clear();
		goingTo.sendKeys(destination);	
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		for (WebElement element:goingToList)
		{
		    if(element.getText().contains(destination))
		        element.click();
		}
	}
	
	public void selectDateOfJourney() {
		dateOfJourney.click();
		String dateArray[] = prop.getProperty("expectedJourneyDate").split("-");
		String day = dateArray[0];
		String month = dateArray[1];
		String year = dateArray[2];		
		
		String currentMonth = currentMonthOfLeavingFrom.getText();
		String currentYear = currentYearOfLeavingFrom.getText();
		String currentMonthAndYear = currentMonth + " " + currentYear; //February 2019
		String expectedMonthAndYear = month + " " + year; //March 2019
		int i = 1;
		boolean NextButtonClickflag = false;
		
		/*As this site allows booking tickets only for next 2 months, writing the logic accordingly 
		which means checking current month and next 2 months. This logic also works when the year changes*/	
		if((expectedMonthAndYear.equals(currentMonthAndYear))) //February 2019
		{
			for (WebElement date : totalActiveDatesInFirstCalendar) {
				
				if(date.getText().equals(day))
				{
					date.click();
					break;
				}
			}
		}
		else {
			while(i<=2) 
			{
				if(!NextButtonClickflag) { //setting date from first calendar
					nextButtonInCalendar.click();
					NextButtonClickflag=true;
					WebDriverWait wait = new WebDriverWait(driver, 10);
					wait.until(ExpectedConditions.elementToBeClickable(nextButtonInCalendar));
					currentMonth = currentMonthOfLeavingFrom.getText();
					currentYear = currentYearOfLeavingFrom.getText();
					currentMonthAndYear = currentMonth + " " + currentYear; //March 2019
					if((expectedMonthAndYear.equals(currentMonthAndYear))) {
						for (WebElement date : totalActiveDatesInFirstCalendar) {
							if(date.getText().equals(String.valueOf(Integer.parseInt(day)))) //in case if day is 01 to 09
							{	
								date.click();
								break;
							}
						} 
						break;
					}
					else
						i=i+1;
				}
				else //setting date from second calendar
				{						
					currentMonth = nextMonthOfLeavingFrom.getText();
					currentYear = nextYearOfLeavingFrom.getText();
					currentMonthAndYear = currentMonth + " " + currentYear; //April 2019
					if((expectedMonthAndYear.equals(currentMonthAndYear))) {
						for (WebElement date : totalActiveDatesInLastCalendar) {							
							if(date.getText().equals(String.valueOf(Integer.parseInt(day))))
							{
								date.click();
								break;
							}
						} 
						break;
					}
					else
						i=i+1;
				}
			}//while
		}		
	}
	
	public void selectReturnDateOfJourney() {
		dateOfReturnJourney.click();
		String dateArray[] = prop.getProperty("expectedReturnJourneyDate").split("-");
		String day = dateArray[0];
		String month = dateArray[1];
		String year = dateArray[2];		
		
		String currentMonth = currentMonthOfLeavingFrom.getText();
		String currentYear = currentYearOfLeavingFrom.getText();
		String currentMonthAndYear = currentMonth + " " + currentYear; //February 2019
		String expectedReturnMonthAndYear = month + " " + year; //March 2019
		int i=1;
		boolean NextButtonClickflag=false;
		if((expectedReturnMonthAndYear.equals(currentMonthAndYear))) //February 2019
		{
			for (WebElement date : totalActiveDatesInFirstCalendar) {
				
				if(date.getText().equals(day))
				{
					date.click();
					break;
				}
			}
		}
		else {
			while(i<=2) 
			{
				if(!NextButtonClickflag) { //setting date from first calendar
					nextButtonInCalendar.click();
					NextButtonClickflag=true;
					WebDriverWait wait = new WebDriverWait(driver, 10);
					wait.until(ExpectedConditions.elementToBeClickable(nextButtonInCalendar));
					currentMonth = currentMonthOfLeavingFrom.getText();
					currentYear = currentYearOfLeavingFrom.getText();
					currentMonthAndYear = currentMonth + " " + currentYear; //March 2019
					if((expectedReturnMonthAndYear.equals(currentMonthAndYear))) {
						for (WebElement date : totalActiveDatesInFirstCalendar) {
							if(date.getText().equals(String.valueOf(Integer.parseInt(day)))) //in case if day is 01 to 09
							{	
								date.click();
								break;
							}
						} 
						break;
					}
					else
						i=i+1;
				}
				else //setting date from second calendar
				{						
					currentMonth = nextMonthOfLeavingFrom.getText();
					currentYear = nextYearOfLeavingFrom.getText();
					currentMonthAndYear = currentMonth + " " + currentYear; //April 2019
					if((expectedReturnMonthAndYear.equals(currentMonthAndYear))) {
						for (WebElement date : totalActiveDatesInLastCalendar) {							
							if(date.getText().equals(String.valueOf(Integer.parseInt(day))))
							{
								date.click();
								break;
							}
						} 
						break;
					}
					else
						i=i+1;
				}
			}//while
		}	
	}
	
}
