import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;


public class Rough {
    WebDriver driver;
    ExtentReports extent;
    ExtentTest logger;
    ExtentTest consoleTest;
    String url ="https://www3.forbes.com/lifestyle/the-lonely-planet-top-ten-tourism-countries-for-2020-ifs-vue/";
    String domain ="https://www.forbes.com/";

    Boolean DFPEnabled=true;
    @BeforeTest
                public void setUp() {
//            Driver Paths and Setting properties
//        String ffDriverPath ="C:\\Users\\krish.t\\Downloads\\geckodriver.exe";
//        System.setProperty("webdriver.gecko.driver",ffDriverPath);
            String extentReportFile = System.getProperty("user.dir")+"\\CommonTests.html";
            String extentReportImage = System.getProperty("user.dir")+"\\extentReportImage.png";

            String chromeDriverPath = "C:\\Users\\krish.t\\Downloads\\chromedriver.exe";
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);

            extent = new ExtentReports(extentReportFile, true);
            extent.loadConfig(new File(System.getProperty("user.dir")+"\\extent-config.xml"));

            //Logging preferences
            ChromeOptions caps = new ChromeOptions();

            //For Server
           //  caps.addArguments("--headless");
           // caps.addArguments("--no-sandbox");
            LoggingPreferences logs = new LoggingPreferences();
            logs.enable(LogType.BROWSER, Level.ALL);
            logs.enable(LogType.DRIVER, Level.ALL);
            logs.enable(LogType.PERFORMANCE, Level.ALL);
            logs.enable( LogType.PERFORMANCE, Level.ALL );
            caps.setCapability("goog:loggingPrefs", logs );
            caps.setCapability(CapabilityType.LOGGING_PREFS, logs);
            driver = new ChromeDriver(caps);

        }
    @Test
    public void Rough() throws Exception{
        driver.manage().window().maximize();
        driver.get("http://forbes-staging.testingpe.com/ignore-test/forbes-400-top-100-richest-list-makers-2019-ifs-vue-mn-wnb/?s");
        List<WebElement> medias = driver.findElements(By.className("media-credit"));
        System.out.println(medias.size());
        String previous ="";
        for(WebElement w : medias){
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);",w);
        ((JavascriptExecutor)driver).executeScript("window.scrollBy(0,300)");
        Thread.sleep(2000);
        if(!previous.equalsIgnoreCase(driver.getCurrentUrl()))
             System.out.println(driver.getCurrentUrl());
        previous=driver.getCurrentUrl();
        }
    }
    @AfterTest
    public void closeTest()throws Exception{
        //Thread.sleep(50000);
       driver.quit();
       // logger.getTest().setStatus(LogStatus.PASS);
        extent.endTest(logger);
        //extent.endTest(logger);
        extent.flush();
        extent.close();
    }
}