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
import org.openqa.selenium.support.ui.WebDriverWait;

import com.paulhammant.ngwebdriver.NgWebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

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
		} catch (Exception e) {
			driver.manage().deleteAllCookies();
			driver.navigate().refresh();
		}
	}
	
	
	public void launchbrowser() throws Exception {
		if((browser).equals("CH")) {
			System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
			WebDriverManager.chromedriver().clearCache();
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-notifications");
			Map prefs = new HashMap();
			prefs.put("profile.default_content_settings.cookies", 2);
			options.setExperimentalOption("prefs", prefs);
			driver = new ChromeDriver(options);
		}else {
			throw new RuntimeException("Chrome Browser Type Unsupported");
		}
	}
		
	public void setText(WebElement element, String value) throws Exception {
		try {       
				waitforElementIsDisplayed(element);
				element.sendKeys(value); 
			} catch (Exception e) {                                                
				throw new Exception("Set Text is not working for :: " + element + " " + e);
			} 
		
	}
	
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
	
	public void click(WebElement element) throws Exception {
		try {
			element.click();
		} catch (Exception e) {
			throw new Exception("Click is not  working on:: " + element + " " + e);
		}
	}
	
	public void actionclick(WebElement element) throws Exception {
		try {
			Actions action = new Actions(driver);
			action.moveToElement(element).click().perform();
		} catch (Exception e) {
			throw new Exception("Actions Click is not  working on:: " + element + " " + e);
		}
	}
	
	public String gettitle() throws Exception {
		String actualTitle;
		try {
			actualTitle = driver.getTitle();
		}catch (Exception e) {
			throw new Exception("Get Title click is not  working on:: " + e);
		}
		return actualTitle;
	}
	
	public void javascriptclick(WebElement element) throws Exception {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", element);
		} catch (Exception e) {
			throw new Exception("Javascript click is not  working on:: " + element + " " + e);
		}
	}
	
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
	
	public List<String> getListWebElementText(List<WebElement> WebElementsList) throws Exception {
		List<String> getData = new ArrayList<String>();
		for (WebElement element : WebElementsList) {
			String text = getText(element);
			getData.add(text.trim());
		}
		return getData;
	}
	
	public void clickListWebElementText(List<WebElement> WebElementsList,String selecttext) throws Exception {
		for (WebElement element : WebElementsList) {
			if(selecttext.equalsIgnoreCase(getText(element))) {
				element.click();
			}
		}
	}
	
	public String getText(WebElement element) throws Exception {
		try {
			return element.getText();
		} catch (Exception e) {
			throw new Exception("getText is not working" + e);

		}
	}
	public void waitTime(final int secs) throws InterruptedException {
		Thread.sleep(secs * 1000);
	}
	
	public String captureScreenshot() throws Exception {
		// Take screenshot and store as a file format
        File src= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String path = "C:/Screenshots/"+System.currentTimeMillis()+".png";
        try {
            // now copy the  screenshot to desired location using copyFile //method
            FileUtils.copyFile(src, new File(path));
        }catch (Exception e)  {
        	throw new Exception("Capture Screenshot is not working" + e);

        }
        return path;
	}
	
}
