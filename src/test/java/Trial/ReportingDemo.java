package Trial;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import java.io.File;

public class ReportingDemo {
    ExtentReports extent;
    ExtentTest logger;

    @BeforeTest
    public void setUp(){
        String extentReportFile = System.getProperty("user.dir")+"Krish.html";
        String extentReportImage = System.getProperty("user.dir")+"\\extentReportImage.png";

        String ffDriverPath ="C:\\Users\\krish.t\\Downloads\\geckodriver.exe";
        System.setProperty("webdriver.gecko.driver",ffDriverPath);

        extent = new ExtentReports(extentReportFile, true);
        extent.loadConfig(new File(System.getProperty("user.dir")+"\\extent-config.xml"));
      //  ExtentTest extentTest = extent.startTest("My First Test", "Verify WebSite Title");
    }

    @Test
    public void passTest(){
    logger=extent.startTest("PassTestFunction", "This will be passing");
    Assert.assertTrue(true);
    logger.log(LogStatus.PASS, "This has passed Successfully!!!");
    }

    @Test
    public void failTest(){
        logger=extent.startTest("FailTestFunction", "This will be Failing");
        logger.log(LogStatus.FAIL, "This has failed!!!");
        Assert.assertTrue(false);


    }

    @Test
    public void skipTest(){
        logger=extent.startTest("skipTestFunction", "This will be Skipped");
        logger.log(LogStatus.SKIP, "This has been Skipped Successfully!!!");
        throw  new SkipException("Skipping this test");

    }

    @AfterMethod
    public void afterMethod(ITestResult result)
    {
        if(result.getStatus()==ITestResult.FAILURE){
            logger.log(LogStatus.FAIL,"The test failed : "+result.getName()+" \n Error = "+result.getThrowable());
        }
        else if(result.getStatus()==ITestResult.SKIP){
            logger.log(LogStatus.SKIP,"The test Skipped : "+result.getName());
        }
       extent.endTest(logger);
    }
    @AfterTest
    public void closeTest(){
        extent.flush();
        extent.close();
    }
}