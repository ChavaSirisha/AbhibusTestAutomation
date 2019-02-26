package com.abhibus.qa.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class testBase {
	public static WebDriver driver;
	public static Properties prop;
	
	public testBase() {
		try {
			prop = new Properties();
			FileInputStream ip = new FileInputStream(System.getProperty("user.dir")+"/src/main/java/com/abhibus/qa/config/config.properties");
			prop.load(ip);
		}catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void initialization() {
		String browserName = prop.getProperty("browser");
		if(browserName.equals("chrome")) {
			
			System.setProperty("webdriver.chrome.driver", "C:\\Users\\Ashok\\eclipse-workspace\\AbhibusTestAutomation\\Drivers\\chromedriver.exe");
			driver = new ChromeDriver();
		}
		//add else-if conditions to test the application in different browsers
		
		driver.get(prop.getProperty("url"));
		
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		//driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);		
		
	}

}
