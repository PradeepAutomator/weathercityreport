package support;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.paulhammant.ngwebdriver.NgWebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;


/****
*
* @author Pradeep
*
*/


public class Generic {
	
	public static WebDriver driver = null;
	public Properties properties;
	public String browser;
	String WorkingDir;
	public Generic generic;
	public String weburl;
	public String apiurl;
	public String ErrorMsg = "";
	public utils.Logger Logger;
	public String currentweather;
	public String variancelimit;
	
	public Generic(Generic generic) {
		this.setGeneric(generic);
		this.driver = generic.driver;
		PageFactory.initElements(driver, this);
	}
	
	private void setGeneric(Generic generic) {
		this.generic = generic;
		
	}
	
	public Generic(String browser) throws Exception {
		properties = new Properties();
		FileInputStream propfile = new FileInputStream("./src/test/resources/config.properties");
		properties.load(propfile);
		this.browser = browser;
		WorkingDir = System.getProperty("user.dir");
		launchbrowser();
		Logger = new utils.Logger(driver, browser);
		Logger.setWebDriver(this);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		//driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		try {
			weburl = properties.getProperty("weburl");
			apiurl = properties.getProperty("apiurl");
			variancelimit = properties.getProperty("variancelimit");
		} catch (Exception e) {
			driver.manage().deleteAllCookies();
			driver.navigate().refresh();
		}
	}
	
	/*'#####################################################################################################################################   
	'Function Name   : launchbrowser    
	'Purpose         : This function used to launch browser  
	'Input           : Browser    
	'Returns         : Void    
	'####################################################################################################################################### */
	public void launchbrowser() throws Exception {
		if((browser).equals("CH")) {
			System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
			WebDriverManager.chromedriver().clearCache();
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			//options.addArguments("--headless", "--start-maximized", "--incognito");
			options.addArguments("--disable-notifications");
			Map prefs = new HashMap();
			prefs.put("profile.default_content_settings.cookies", 2);
			options.setExperimentalOption("prefs", prefs);
			driver = new ChromeDriver(options);
		}else {
			throw new RuntimeException("Chrome Browser Type Unsupported");
		}
	}
		
	/*'#####################################################################################################################################   
	'Function Name   : setText    
	'Purpose         : This function used Set Text to textbox  
	'Input           : Webelement, Value to be entered    
	'Returns         : Void    
	'####################################################################################################################################### */
	
	public void setText(WebElement element, String value) throws Exception {
		try {       
				waitforElementIsDisplayed(element);
				element.sendKeys(value); 
			} catch (Exception e) {                                                
				throw new Exception("Set Text is not working for :: " + element + " " + e);
			} 
		
	}
	
	/*'#####################################################################################################################################   
	'Function Name   : waitforElementIsDisplayed    
	'Purpose         : This function used to check whether element is displayed 
	'Input           : Webelement 
	'Returns         : Void    
	'####################################################################################################################################### */
	
	public void waitforElementIsDisplayed(final WebElement element) throws Exception {
		try {
			(new WebDriverWait(driver, 100)).until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver drive) {
					try {
						return Boolean.valueOf(element != null && element.isDisplayed());
					} catch (Exception e) {
						return Boolean.valueOf(false);
					}
				}
			});
		} catch (Exception e) {
			throw new Exception("Wait for Element to be Displayed is not working.. :: " + element + " " + e);
		}

	}
	
	/*'#####################################################################################################################################   
	'Function Name   : click    
	'Purpose         : This function used to click button or links
	'Input           : Webelement 
	'Returns         : Void    
	'####################################################################################################################################### */
	
	public void click(WebElement element) throws Exception {
		try {
			element.click();
		} catch (Exception e) {
			throw new Exception("Click is not  working on:: " + element + " " + e);
		}
	}
	
	/*'#####################################################################################################################################   
	'Function Name   : actionclick    
	'Purpose         : This function used to click button or links using Actions
	'Input           : Webelement 
	'Returns         : Void    
	'####################################################################################################################################### */
	
	
	public void actionclick(WebElement element) throws Exception {
		try {
			Actions action = new Actions(driver);
			action.moveToElement(element).click().perform();
		} catch (Exception e) {
			throw new Exception("Actions Click is not  working on:: " + element + " " + e);
		}
	}
	
	/*'#####################################################################################################################################   
	'Function Name   : gettitle    
	'Purpose         : This function used to cget title of WebPage
	'Input           : NA 
	'Returns         : String    
	'####################################################################################################################################### */
	
	public String gettitle() throws Exception {
		String actualTitle;
		try {
			actualTitle = driver.getTitle();
		}catch (Exception e) {
			throw new Exception("Get Title click is not  working on:: " + e);
		}
		return actualTitle;
	}
	
	/*'#####################################################################################################################################   
	'Function Name   : javascriptclick    
	'Purpose         : This function used to click button or links using Javacsript
	'Input           : Webelement 
	'Returns         : Void    
	'####################################################################################################################################### */
	
	public void javascriptclick(WebElement element) throws Exception {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", element);
		} catch (Exception e) {
			throw new Exception("Javascript click is not  working on:: " + element + " " + e);
		}
	}
	
	/*'#####################################################################################################################################   
	'Function Name   : waitForPageLoad    
	'Purpose         : This function used to wait for Webpage to load
	'Input           : NA 
	'Returns         : Void    
	'####################################################################################################################################### */
	
	public  void waitForPageLoad() {
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
			}
		};
		try {
			Thread.sleep(5);
			WebDriverWait wait = new WebDriverWait(driver, 50);
			wait.until(expectation);
		} catch (Exception e) {
			throw new RuntimeException("Timeout waiting for Page Load Request to complete. ", e);
		}
	}
	
	/*'#####################################################################################################################################   
	'Function Name   : waitForPageLoad    
	'Purpose         : This function used to wait for Webpage to load for particular time
	'Input           : Time 
	'Returns         : Void    
	'####################################################################################################################################### */
	
	public void waitForPageLoad(int time) {
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
			}
		};
		try {
			Thread.sleep(5);
			WebDriverWait wait = new WebDriverWait(driver, time);
			wait.until(expectation);
		} catch (Exception e) {
			throw new RuntimeException(time + " Sec Timeout waiting for Page Load Request to complete. ", e);
		}
	}
	
	
	/*'#####################################################################################################################################   
	'Function Name   : getListWebElementText    
	'Purpose         : This function used return list of text from webelement
	'Input           : WebElementsList 
	'Returns         : List of Strings    
	'####################################################################################################################################### */
	
	public List<String> getListWebElementText(List<WebElement> WebElementsList) throws Exception {
		List<String> getData = new ArrayList<String>();
		for (WebElement element : WebElementsList) {
			String text = getText(element);
			getData.add(text.trim());
		}
		return getData;
	}
	
	/*'#####################################################################################################################################   
	'Function Name   : clickListWebElementText    
	'Purpose         : This function used to click particular button or link in a list of elements
	'Input           : WebElementsList, selecttext 
	'Returns         : Void    
	'####################################################################################################################################### */
	
	public void clickListWebElementText(List<WebElement> WebElementsList,String selecttext) throws Exception {
		try {
			for (WebElement element : WebElementsList) {
				if(selecttext.equalsIgnoreCase(getText(element))) {
					element.click();
				}
			}
		} catch (Exception e) {
			throw new Exception("clickListWebElementText is not working" + e);

		}
	}
	
	/*'#####################################################################################################################################   
	'Function Name   : getText    
	'Purpose         : This function used to get text of a element
	'Input           : Webelement 
	'Returns         : String    
	'####################################################################################################################################### */
	
	public String getText(WebElement element) throws Exception {
		try {
			return element.getText();
		} catch (Exception e) {
			throw new Exception("getText is not working" + e);

		}
	}
	
	/*'#####################################################################################################################################   
	'Function Name   : waitTime    
	'Purpose         : This function used to wait for particular time
	'Input           : secs 
	'Returns         : Void    
	'####################################################################################################################################### */
	
	public void waitTime(final int secs) throws InterruptedException {
		Thread.sleep(secs * 1000);
	}
	
	/*'#####################################################################################################################################   
	'Function Name   : captureScreenshot    
	'Purpose         : This function used to capture screenshot and return screenshot path
	'Input           : NA 
	'Returns         : String    
	'####################################################################################################################################### */
	
	public String captureScreenshot() throws Exception {
		// Take screenshot and store as a file format
        File src= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String path = WorkingDir + "/reports/Screenshots/"+System.currentTimeMillis()+".png";
        try {
            // now copy the  screenshot to desired location using copyFile //method
            FileUtils.copyFile(src, new File(path));
        }catch (Exception e)  {
        	throw new Exception("Capture Screenshot is not working" + e);

        }
        return path;
	}
	
	/*'#####################################################################################################################################   
	'Function Name   : tempcomparator    
	'Purpose         : This function used to compare temperature from web and api
	'Input           : webtemp, apitemp
	'Returns         : Boolean    
	'####################################################################################################################################### */
	
	public boolean tempcomparator(float webtemp,float apitemp) throws Exception {
		boolean returnvalue;
		try {
			if((Math.round(webtemp))==(Math.round(apitemp))) {
				returnvalue = true;
				System.out.println("Temp comparison between Web : " + Math.round(webtemp) +" and API : " + Math.round(apitemp) +" is similiar");
			}else {
				returnvalue = false;
				System.out.println("Temp comparison between Web : " + Math.round(webtemp) +" and API : " + Math.round(apitemp) +" is different");
			}
		}catch(Exception e) {
			returnvalue = false;
			throw new Exception("tempcomparator is not working " + e);			
		}		
		return returnvalue;
	}
	
	/*'#####################################################################################################################################   
	'Function Name   : tempvariancecheck    
	'Purpose         : This function used to check variance of temperature from web and api
	'Input           : webtemp, apitemp
	'Returns         : Boolean    
	'####################################################################################################################################### */
	
	public boolean tempvariancecheck(float webtemp,float apitemp) throws Exception {
		boolean returnvalue;
		int varlimit = Integer.valueOf(variancelimit);
		try {
			if(((webtemp-apitemp)<0)||((webtemp-apitemp)>varlimit)) {
				returnvalue = false;
				System.out.println("Temp variance between Web : " + webtemp +" and API : " + apitemp +" is : " + (webtemp-apitemp) + " which has failed due to difference in Variance limit");
				throw new Exception("Temp variance between Web : " + webtemp +" and API : " + apitemp +" is : " + (webtemp-apitemp) + "  which has failed due to difference in Variance limit");
			}else {
				returnvalue = true;
				System.out.println("Temp variance between Web : " + webtemp +" and API : " + apitemp +" is : " + (webtemp-apitemp) + " which is in Variance limit");
			}
		}catch(Exception e) {
			returnvalue = false;
			throw new Exception("tempvariancecheck is not working " + e);			
		}
		return returnvalue;
	}
	
}
