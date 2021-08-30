package Pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.annotations.Test;

import support.Generic;

public class WeatherCity {
	
	@FindBy(css="form > input")
	WebElement textbox_searchLocation;
	
	@FindBy(xpath="//*[@id='privacy-policy-banner']//div[text()='I Understand']")
	WebElement textbox_searchLocationparent;
	
	@FindBy(xpath="//*[@class='search-results']//*[@class='search-bar-result search-result']")
	WebElement dropdown_searchLocation;
		
	Generic generic;

	//*[@class='search-results']//*[@class='search-bar-result search-result'][text()='Wellington, Wellington, NZ']
	public void captureWeatherCity(Generic Application, String value) throws Exception {
		String acttitle = Application.gettitle();
		System.out.println(acttitle);
		Application.javascriptclick(textbox_searchLocationparent);
		Application.setText(textbox_searchLocation, value);			
	}

}
