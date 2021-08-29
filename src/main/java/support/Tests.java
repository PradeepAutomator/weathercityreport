package support;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import PageWeatherCity.WeatherCity;

public class Tests {
	
	Generic Application = null;
	public String browser = "";
	WeatherCity weathercity;
	
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
	public void run(String cityvalue) throws Exception {
		Application.driver.get(Application.weburl);
		//weathercity.captureWeatherCity(cityvalue);
		//Thread.sleep(10000);
		
	}
	
	@DataProvider(name="Inputdata")
    public static Object[][] getDataFromDataprovider(){
        return new Object[][] {
        	{ "Chennai"}
            
        };  
		}
	
}
