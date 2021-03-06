package utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import support.Generic;

/****
*
* @author Pradeep
*
*/

public class Logger {
	public ExtentTest Parent;
	public ExtentTest Logger;
	public ExtentTest ChildLogger;
	public ExtentTest Swap;
	private WebDriver driver = null;
	private String WorkingDir = "";
	private String ReportsPath = "";
	private String HtmlReport = "";
	private String ScreenShotsPath = "";
	private Generic generic;
	public static volatile ExtentReports extent;
	public boolean status = true;

	@SuppressWarnings("deprecation")
	public Logger(WebDriver Driver, String browser) throws IOException {
		if (extent == null) {
			synchronized (Logger.class) {
				if (extent == null) {
					driver = Driver;
					WorkingDir = System.getProperty("user.dir");
					System.out.println("WorkingDir"+WorkingDir);
					ReportsPath = WorkingDir + File.separator +"reports";
					createDirectory(WorkingDir + File.separator +"downloads");
					createDirectory(ReportsPath);
					HtmlReport = ReportsPath;
					//createDirectoryIfNeeded(HtmlReport);
					ScreenShotsPath = ReportsPath + File.separator+ "screenshots";
					FileUtils.deleteDirectory(new File(ScreenShotsPath));//for delete the existing old screenshots
					createDirectory(ScreenShotsPath);
					extent = new ExtentReports(HtmlReport + File.separator+"AutomationExecutionReport.html", true);
					Map<String, String> sysInfo = new HashMap<String, String>();
					Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
					sysInfo.put("Browser", caps.getBrowserName());
					sysInfo.put("Browser Version", caps.getVersion());
					sysInfo.put("OS", System.getProperty("os.name"));

					extent.addSystemInfo(sysInfo);
					extent.config().reportName(browser);

				}
			}
		}
	}

	public void setWebDriver(Generic generic) {
		this.generic = generic;
		this.driver = generic.driver;
	}
	
	 /*'#####################################################################################################################################??????
	'Function Name???? : createDirectoryIfNeeded?????? 
	'Purpose???????????????? : This function used to create directory if required
	'Input??????????????????????: directoryName
	'Returns???????????????? : void?????? 
	'####################################################################################################################################### */
	
	private void createDirectory(String directoryName) {
		File theDir = new File(directoryName);
		// if the directory does not exist, create it
		if (!theDir.exists()) {
			theDir.mkdir();
		}
	}

	 /*'#####################################################################################################################################??????
	'Function Name???? : createScreenshot?????? 
	'Purpose???????????????? : This function used to take screenshots and return path
	'Input??????????????????????: NA
	'Returns???????????????? : String?????? 
	'####################################################################################################################################### */
	public synchronized String createScreenshot() {
		String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
		String Path = ScreenShotsPath + File.separator + timeStamp + ".png";
		// generate screenshot as a file object
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(Path));
		} catch (IOException e) {
			System.out.println("Error while generating screenshot:\n" + e.toString());
		}
		return Path;
	}

	 /*'#####################################################################################################################################??????
	'Function Name???? : addStep?????? 
	'Purpose???????????????? : This function used to add step in extent report
	'Input??????????????????????: string, string2
	'Returns???????????????? : void?????? 
	'####################################################################################################################################### */
	
	public void addStep(String string, String string2) {
		this.Logger = extent.startTest(string, string2);
	}
	
	/*'#####################################################################################################################################??????
	'Function Name???? : addsubStep?????? 
	'Purpose???????????????? : This function used to add sub step in extent report
	'Input??????????????????????: info, msg, screenshot
	'Returns???????????????? : void?????? 
	'####################################################################################################################################### */
	
	public void addsubStep(LogStatus info, String msg, boolean addScreenshot) {
		try {
			Logger.log(info, addScreenshot ? msg + ":" + Logger.addScreenCapture(createScreenshot()) : msg);
			//System.out.println(info + " : " + msg);
			if (info.equals(LogStatus.FAIL) || info.equals(LogStatus.ERROR) || info.equals(LogStatus.FATAL)
					|| info.equals(LogStatus.WARNING)) {
				generic.ErrorMsg = generic.ErrorMsg + msg;
				status=false;
				if(info.equals(LogStatus.WARNING)) {
					generic.ErrorMsg="Warning:"+msg+":::"+generic.ErrorMsg;
				}
			}

		} catch (Exception e) {
			this.Logger.log(info,
					"Unable to capture screenshot. Reason: Browser is closed. Exception:" + e.getMessage());
		}
	}
	
	
	/*'#####################################################################################################################################??????
	'Function Name???? : endStep?????? 
	'Purpose???????????????? : This function used to end step in extent report
	'Input??????????????????????: NA
	'Returns???????????????? : void?????? 
	'####################################################################################################################################### */
	
	public void endStep() {
		Parent.appendChild(this.Logger);
	}
	
	/*'#####################################################################################################################################??????
	'Function Name???? : addException?????? 
	'Purpose???????????????? : This function used to add exception in extent report
	'Input??????????????????????: exception
	'Returns???????????????? : void?????? 
	'####################################################################################################################################### */
	
	public void addException(String ex) {
		addsubStep(LogStatus.ERROR, ex, true);
		generic.ErrorMsg = generic.ErrorMsg + ex;
		System.out.println(LogStatus.ERROR.toString() + ":" + ex);
		endStep();
	}

}
