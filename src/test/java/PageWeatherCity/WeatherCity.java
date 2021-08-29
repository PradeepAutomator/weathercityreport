package PageWeatherCity;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.annotations.Test;

import support.Generic;

public class WeatherCity {
	
	@FindBy(xpath="//input[@name='query']")
	WebElement textbox_searchLocation;
	
	@FindBy(xpath="//*[@class='search-results']//*[@class='search-bar-result search-result']")
	WebElement dropdown_searchLocation;
	
	Generic generic;
	//*[@class='search-results']//*[@class='search-bar-result search-result'][text()='Wellington, Wellington, NZ']
	public void captureWeatherCity(String value) throws Exception {
		generic.setText(textbox_searchLocation, value);			
	}

}
