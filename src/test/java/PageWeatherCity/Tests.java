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

import com.relevantcodes.extentreports.LogStatus;

import APIWeatherCity.WeathercityApi;
import Pages.WeatherCity;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import support.Generic;


/****
 *
 * @author Pradeep
 *
 */


public class Tests extends WeathercityApi{
	
	Generic Application = null;
	public String browser = "";
    String htmlReportPath = "";
    ResponseOptions<Response> currentResponse = null;
    protected static String apicurrenttemp = "";
    protected static String webcurrenttemp="";
    boolean returnflag;
        
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
		Application.Logger.Parent = Application.Logger.extent.startTest("TempCheck","Comparison and variance check");
		Application.Logger.addStep("1.Open the application", "Application url should be opened");
		Application.Logger.setWebDriver(Application);
		WeatherCity weathercity = new WeatherCity(Application);
		WeathercityApi weathercityapi = new WeathercityApi();
		System.out.println("Web Current temp : " + webcurrenttemp);
		webcurrenttemp = weathercity.captureWeatherCity(cityvalue,drpdownvalue);
		if(webcurrenttemp.equals(null)||webcurrenttemp.equalsIgnoreCase("")) {
			Application.Logger.addsubStep(LogStatus.FAIL, "Current temp return was unsuccessful from Web", true);
		}else {
			Application.Logger.addsubStep(LogStatus.PASS, "Current temp returned successfully from Web", false);
		}		
		HashMap<String, Object> requestJson = createRequestJsonFromList(Arrays.asList(cityvalue, metrics));
        currentResponse = weathercityapi.weatherreportapi(APIKEY, requestJson);
        String statuscode = weathercityapi.getStatusCode(currentResponse);
        if(statuscode.equalsIgnoreCase("200")) {
			Application.Logger.addsubStep(LogStatus.PASS, "API returned success response : " + statuscode, false);
		}else {
			Application.Logger.addsubStep(LogStatus.FAIL, "API returned unsuccessful reposnse : " + statuscode, true);
		}	
        //System.out.println("Current Status : " + statuscode);
        apicurrenttemp = weathercityapi.getValueByPath(currentResponse, "main.temp");
        System.out.println("Api Current temp : " + apicurrenttemp);		
        if(apicurrenttemp.equals(null)||apicurrenttemp.equalsIgnoreCase("")) {
			Application.Logger.addsubStep(LogStatus.FAIL, "Current temp return was unsuccessful from API", false);
		}else {
			Application.Logger.addsubStep(LogStatus.PASS, "Current temp returned successfully from API", false);
		}	
        float webcurrtemp=Integer.valueOf(webcurrenttemp);
        float apicurrtemp=Float.parseFloat(apicurrenttemp);
        returnflag = Application.tempcomparator(webcurrtemp, apicurrtemp);
        if(returnflag) {
			Application.Logger.addsubStep(LogStatus.PASS, "Temp comparison between Web : " + Math.round(webcurrtemp) +" and API : " + Math.round(apicurrtemp) +" is similiar", false);
		}else {
			Application.Logger.addsubStep(LogStatus.FAIL, "Temp comparison between Web : " + Math.round(webcurrtemp) +" and API : " + Math.round(apicurrtemp) +" is different", false);
		}	
        returnflag = Application.tempvariancecheck(webcurrtemp, apicurrtemp);
        if(returnflag) {
			Application.Logger.addsubStep(LogStatus.PASS, "Temp variance between Web : " + webcurrtemp +" and API : " + apicurrtemp +" is : " + (webcurrtemp-apicurrtemp) + " which is in Variance limit", false);
		}else {
			Application.Logger.addsubStep(LogStatus.FAIL, "Temp variance between Web : " + webcurrtemp +" and API : " + apicurrtemp +" is : " + (webcurrtemp-apicurrtemp) + "  which has failed due to difference in Variance limit", false);
		}	
        Application.Logger.endStep();
        Application.Logger.extent.endTest(Application.Logger.Parent);
		Application.Logger.extent.flush();
	}
	
	@DataProvider(name="Inputdata")
    public static Object[][] getDataFromDataprovider(){
        return new Object[][] {
        	{ "chennai","Chennai, Tamil Nadu, IN","metric"}        
        };  
		}
	
}
