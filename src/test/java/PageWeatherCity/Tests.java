package PageWeatherCity;

import java.io.File;

import org.openqa.selenium.*;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import Pages.WeatherCity;
import support.Generic;

public class Tests {
	
	Generic Application = null;
	public String browser = "";
	private String WorkingDir = "";
    String htmlReportPath = ""; 
        
	@Parameters({"browser"})
	@BeforeTest()
	public void setup(String browser) throws Exception {
		this.browser = browser;
		Application = new Generic(browser);
		if (Application.driver == null) {
			Application.launchbrowser();
			Application.driver.manage().window().maximize();
		}
	}

	@AfterTest()
	public void tearDown() {
		if(!(Application.driver==null)) {
			Application.driver.quit();
		}
	}
	
	@Test(dataProvider="Inputdata")
	public void run(String cityvalue,String drpdownvalue) throws Exception {
		Application.driver.get(Application.weburl);
		WeatherCity weathercity = new WeatherCity(Application);
		weathercity.captureWeatherCity(cityvalue,drpdownvalue);
		//Thread.sleep(10000);
		
	}
	
	@DataProvider(name="Inputdata")
    public static Object[][] getDataFromDataprovider(){
        return new Object[][] {
        	{ "Chennai","Chennai, Tamil Nadu, IN"}
            
        };  
		}
	
}
