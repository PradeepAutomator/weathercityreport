package PageWeatherCity;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

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

import APIWeatherCity.WeathercityApi;
import Pages.WeatherCity;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import support.Generic;

public class Tests extends WeathercityApi{
	
	Generic Application = null;
	public String browser = "";
    String htmlReportPath = "";
    ResponseOptions<Response> currentResponse = null;
    protected static String apicurrenttemp = "";
    protected static String webcurrenttemp="";
        
	@Parameters({"browser"})
	@BeforeTest()
	public void setup(String browser) throws Exception {
		this.browser = browser;
		Application = new Generic(browser);
		if (Application.driver == null) {
			Application.launchbrowser();
			Application.driver.manage().window().maximize();
		}
		Application.driver.get(Application.weburl);
	}

	@AfterTest()
	public void tearDown() {
		if(!(Application.driver==null)) {
			Application.driver.quit();
		}
	}
	
	@Test(dataProvider="Inputdata")
	public void run(String cityvalue,String drpdownvalue,String metrics) throws Exception {
		WeatherCity weathercity = new WeatherCity(Application);
		WeathercityApi weathercityapi = new WeathercityApi();
		webcurrenttemp = weathercity.captureWeatherCity(cityvalue,drpdownvalue);
		System.out.println("Web Current temp : " + webcurrenttemp);
		HashMap<String, Object> requestJson = createRequestJsonFromList(Arrays.asList(cityvalue, metrics));
        currentResponse = weathercityapi.weatherreportapi(APIKEY, requestJson);
        String statuscode = weathercityapi.getStatusCode(currentResponse);
        //System.out.println("Current Status : " + statuscode);
        apicurrenttemp = weathercityapi.getValueByPath(currentResponse, "main.temp");
        System.out.println("Api Current temp : " + apicurrenttemp);		
        float apicurrtemp=Float.parseFloat(apicurrenttemp);
        float webcurrtemp=Integer.valueOf(webcurrenttemp);
        Application.tempcomparator(webcurrtemp, apicurrtemp);
        Application.tempvariancecheck(webcurrtemp, apicurrtemp);
	}
	
	@DataProvider(name="Inputdata")
    public static Object[][] getDataFromDataprovider(){
        return new Object[][] {
        	{ "chennai","Chennai, Tamil Nadu, IN","metric"}        
        };  
		}
	
}
