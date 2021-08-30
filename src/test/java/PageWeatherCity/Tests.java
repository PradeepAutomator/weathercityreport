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

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import Pages.WeatherCity;
import support.Generic;

public class Tests {
	
	Generic Application = null;
	public String browser = "";
	private String WorkingDir = "";
	WeatherCity weathercity = new WeatherCity();
	ExtentReports extent;
    ExtentTest logger;
    ExtentHtmlReporter htmlReporter;
    String htmlReportPath = ""; 
    
    @BeforeClass()
    public void initialize() throws Exception {
    	WorkingDir = System.getProperty("user.dir");
		System.out.println("WorkingDir"+WorkingDir);
		htmlReportPath = WorkingDir + File.separator +"reports"+ File.separator+"ExecutionReport.html";
    	htmlReporter = new ExtentHtmlReporter(htmlReportPath);
        extent = new ExtentReports();
        htmlReporter.setAppendExisting(true);
        extent.attachReporter(htmlReporter);
    }
    
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
	
	 @AfterMethod
	    public void getResult(ITestResult result) throws Exception {
	        if (result.getStatus() == ITestResult.FAILURE) {
	            String failureScreenshot = Application.captureScreenshot();
	            logger.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " Test case FAILED due to below issues:", ExtentColor.RED));
	            logger.fail(result.getThrowable());
	            logger.addScreenCaptureFromPath(failureScreenshot);
	        } else if (result.getStatus() == ITestResult.SUCCESS) {
	            logger.log(Status.PASS, MarkupHelper.createLabel(result.getName() + " Test Case PASSED", ExtentColor.GREEN));
	        } else if (result.getStatus() == ITestResult.SKIP) {
	            String skipScreenshot = Application.captureScreenshot();
	            logger.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + " Test Case SKIPPED", ExtentColor.BLUE));
	            logger.addScreenCaptureFromPath(skipScreenshot);
	        }

	    }

	@AfterTest()
	public void tearDown() {
		extent.flush();
	}
	
	@Test(dataProvider="Inputdata")
	public void run(String cityvalue) throws Exception {
		Application.driver.get(Application.weburl);
		weathercity.captureWeatherCity(Application,cityvalue);
		//Thread.sleep(10000);
		
	}
	
	@AfterClass()
	public void closeDown() throws Exception {
		if(!(Application.driver==null)) {
			Application.driver.quit();
		}
    }
	
	@DataProvider(name="Inputdata")
    public static Object[][] getDataFromDataprovider(){
        return new Object[][] {
        	{ "Chennai"}
            
        };  
		}
	
}
