import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

public class Comments {
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
            String url = "https://www3.forbes.com/business/worst-cars-and-best-cars-of-2019/?utm_source=msn&lcid=90223099";
            logger=extent.startTest("W3 Default layout Automation - "+url, "This test will browse through all the article pages and check for ads, console errors, analytics script and cookies");
            long start = System.currentTimeMillis();

            driver.get(url);
            driver.manage().window().maximize();
            long finish = System.currentTimeMillis();
            long totalTime = finish - start;
            System.out.println("Initial Time for page load - "+totalTime);

            String source = driver.getPageSource();
            int startComment = source.indexOf("<!--");
            int stopComment = source.indexOf("-->");
            int difference = stopComment-startComment;
            System.out.println("Start = "+startComment+"\n Stop = "+ stopComment+"Difference = "+difference);
            int k =2;
            while (source.indexOf("<!--",stopComment)!=-1)
            {
                System.out.println("\n\nOld : "+startComment +" "+ stopComment+" "+difference+" ");
                startComment = source.indexOf("<!--",stopComment);
                stopComment = source.indexOf("-->",startComment);
                difference = stopComment-startComment;
                System.out.println("New : "+startComment +" "+ stopComment+" "+difference+" ");
                System.out.println("Comment #"+k+ ", Length = "+difference);
                k++;
            }

            List elements = driver.findElements(By.xpath("//*"));
//            System.out.println("Number of emels  = "+elements.size());
//            for(int i =0;i<elements.size();i++){
//                try{
//                String className = ((WebElement)elements.get(i)).getAttribute("class");
//                String id = ((WebElement)elements.get(i)).getAttribute("id");
//                System.out.println("Element "+(i+1)+" , Class = "+className+", ID = "+id);
//            }
//                catch (StaleElementReferenceException e){}
//            }
//            System.out.println("\n\n\n\n\n");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,300)");
            Thread.sleep(5000);

            elements = driver.findElements(By.xpath("//*"));
            System.out.println("Number of emels  = "+elements.size());
            for(int i =0;i<elements.size();i++){
                try{
                    String className = ((WebElement)elements.get(i)).getAttribute("class").toLowerCase();
                    String id = ((WebElement)elements.get(i)).getAttribute("id").toLowerCase();
//                   if( org.apache.commons.lang3.StringUtils.containsIgnoreCase(className,"ad"));
                    if(className.equals("")&&id.equals(""));
                    else if (className.equals(" ")&&id.equals(" "));
                    else {
                        String lowID =id.toLowerCase();
                        String lowClass = className.toLowerCase();
                        if(lowClass.contains("ad")||lowClass.contains("pe")||lowClass.contains("banner")||lowClass.contains("project")||lowClass.contains("mnet")||lowClass.contains("media.net")||lowClass.contains("kw")||lowClass.contains("btf")||lowClass.contains("recirc")||lowClass.contains("overlay")
                        ||lowID.contains("ad")||lowID.contains("pe")||lowID.contains("banner")||lowID.contains("project")||lowID.contains("mnet")||lowID.contains("media.net")||lowID.contains("kw")||lowID.contains("btf")||lowID.contains("recirc")||lowID.contains("overlay"))
                        System.out.println("Element Name : " +((WebElement)elements.get(i)).getText() + " , Class = " + className + ", ID = " + id);

                    }
                }
                catch (StaleElementReferenceException e){}
            }
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