package support;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Generic {
	
	public static WebDriver driver = null;
	public Properties properties;
	public String browser;
	String WorkingDir;
	public Generic generic;
	public String weburl;
	public String apiurl;
	
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
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		weburl = properties.getProperty("weburl");
		apiurl = properties.getProperty("apiurl");
		
	}
	
	
	public void launchbrowser() throws Exception {
		if((browser).equals("CH")) {
			System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
			WebDriverManager.chromedriver().clearCache();
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-gpu");
			options.addArguments("disable-infobars");
			options.addArguments("--disable-dev-shm-usage");
			driver = new ChromeDriver(options);
		}else {
			throw new RuntimeException("Chrome Browser Type Unsupported");
		}
	}
	
	public void setText(WebElement element, String value) throws Exception {
		try {       
				waitforElementIsDisplayed(element);
				element.sendKeys(value); 
				Thread.sleep(10000);
			} catch (Exception e) {                                                
				throw new Exception("setText is not working for :: " + element + " " + e);
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
			throw new Exception("waitforElementIsDisplayed is not working.. :: " + element + " " + e);
		}

	}

	private void waitTime(int i) {
		// TODO Auto-generated method stub
		
	}
	
}
