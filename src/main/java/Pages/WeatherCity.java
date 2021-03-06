package Pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;
import support.Generic;


/****
*
* @author Pradeep
*
*/


public class WeatherCity extends Generic{
	
	@FindBy(css="form > input")
	WebElement textbox_searchLocation;
	
	@FindBy(xpath="//*[@id='privacy-policy-banner']//div[text()='I Understand']")
	WebElement textbox_searchLocationparent;
	
	@FindBy(xpath="//*[@class='search-results']//*[@class='search-bar-result search-result']")
	WebElement dropdown_searchLocation;
	
	@FindBy(xpath="//div[@class='cur-con-weather-card__panel']//div[@class='temp']")
	WebElement text_currweather;
	
	public WeatherCity(Generic generic){
		super(generic);
		PageFactory.initElements(driver, this);
	}

	 /*'#####################################################################################################################################   
	'Function Name   : captureWeatherCity    
	'Purpose         : This function used to capture temperature from web application
	'Input           : value, dropdown value
	'Returns         : String    
	'####################################################################################################################################### */
	
	public String captureWeatherCity(String value,String drpdownvalue) throws Exception {
		try {
			String acttitle = generic.gettitle();
			System.out.println(acttitle);
			generic.setText(textbox_searchLocation, value);
			List<WebElement> elements = driver.findElements(By.xpath("//*[@class='search-results']//*[@class='search-bar-result search-result']"));
			List<String> citydrpdownvalues = generic.getListWebElementText(elements); 
			for(String citydrpdownvalue : citydrpdownvalues) {
				if(drpdownvalue.equalsIgnoreCase(citydrpdownvalue)) {
						System.out.println(citydrpdownvalue); } }
			generic.clickListWebElementText(elements, drpdownvalue);
			currentweather=generic.getText(text_currweather);
			currentweather = currentweather.replace("°C", "");
		}catch(Exception e) {
			System.out.println("Unable to fetch weather data" + e.getMessage());
			currentweather ="NA";
		}
		return currentweather;
		
	}
	

}
