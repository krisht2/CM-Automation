package Trial;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

public class ScrollCheck {
    WebDriver driver;
    ExtentReports extent;
    ExtentTest logger;

    @BeforeTest
                    public void setUp() {
                //Driver Paths and Setting properties
//        String ffDriverPath ="C:\\Users\\krish.t\\Downloads\\geckodriver.exe";
//        System.setProperty("webdriver.gecko.driver",ffDriverPath);

                String extentReportFile = System.getProperty("user.dir")+"\\DailyReport.html";
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
                // logs.enable(LogType.PROFILER, Level.ALL);
                // logs.enable(LogType.SERVER, Level.ALL);
                // logs.enable(LogType.CLIENT, Level.ALL);
                caps.setCapability(CapabilityType.LOGGING_PREFS, logs);
                driver = new ChromeDriver(caps);

            }
        @Test
        public void runTest() throws Exception
        {
            String url = "http://forbes-staging.testingpe.com/ignore-test/13-employee-benefits-that-dont-actually-work-ifs-vue";
            logger=extent.startTest("W3 Default layout Automation - "+url, "This test will browse through all the article pages and check for ads, console errors, analytics script and cookies");
            long start = System.currentTimeMillis();

            driver.get(url);
            driver.manage().window().maximize();
            long finish = System.currentTimeMillis();
            long totalTime = finish - start;
            System.out.println("Initial Time for page load - "+totalTime);


            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,300)");
            Thread.sleep(5000);
            driver.navigate().refresh();
            js.executeScript("window.scrollBy(0,-300)");
            js.executeScript("window.scrollBy(0,-300)");
            js.executeScript("window.scrollBy(0,900)");
            Thread.sleep(2000);
            driver.findElement(By.className("next-slide-btn-text")).click();


            driver.quit();
            logger.getTest().setStatus(LogStatus.PASS);
            extent.endTest(logger);

        }
    @AfterTest
    public void closeTest(){
        extent.endTest(logger);
        extent.flush();
        extent.close();
    }
}