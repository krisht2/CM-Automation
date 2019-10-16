import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

public class Rough2 {

    public static void main(String args[]) throws  Exception {

        String ffDriverPath ="C:\\Users\\krish.t\\Downloads\\geckodriver.exe";
        System.setProperty("webdriver.gecko.driver",ffDriverPath);

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\krish.t\\Downloads\\chromedriver.exe");
        ChromeOptions caps = new ChromeOptions();
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
        WebDriver driver = new ChromeDriver(caps);
        String url ="http://localhost/kw/728x200.html";
        Actions a =new Actions(driver);

        driver.get(url);
        driver.manage().window().maximize();
        int w =300;
        int h =300;
        driver.switchTo().frame(1);
        List keywords =driver.findElements(By.tagName("a"));
        System.out.println(keywords.size()+" is the size");
        for(int k =0;k<keywords.size();k++)
        {
            driver.switchTo().defaultContent();
            driver.switchTo().frame(1);
            keywords =driver.findElements(By.tagName("a"));
            System.out.println(keywords.size()+" is the size of the Links inside Loop");
            System.out.println("inside loop");
            WebDriverWait wait = new WebDriverWait(driver,30);
            WebElement linkText =(WebElement) keywords.get(k);
            System.out.println(linkText.getSize());
            String href=linkText.getAttribute("href");
            System.out.println(href+" is the link");
            if(!href.equalsIgnoreCase(url+"#")) {
                if(k==0){
                    System.out.println("Condition fullfilled");
                    driver.switchTo().parentFrame();
                    driver.switchTo().frame(1);
                    a.moveToElement(((WebElement) keywords.get(k))).build().perform();
                    File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
                    Thread.sleep(1000);
                    FileUtils.copyFile(screenshotFile, new File("C:\\xampp\\htdocs\\AdTags\\Screenshots\\krish"+k+".jpg"));
                    ((WebElement) keywords.get(k)).click();
                    Thread.sleep(500);
                    driver.switchTo().defaultContent();
                    driver.navigate().back();
//                    if(driver.getCurrentUrl().contains("media.net"))
//                        driver.close();
                    Thread.sleep(3000);
                }
                if(k>0&& !href.equalsIgnoreCase(((WebElement)keywords.get(k-1)).getAttribute("href"))) {
                    System.out.println("Condition fullfilled");
                    driver.switchTo().parentFrame();
                    driver.switchTo().frame(1);

                    a.moveToElement(((WebElement) keywords.get(k))).build().perform();
                    File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
                    Thread.sleep(1000);
                    FileUtils.copyFile(screenshotFile, new File("C:\\xampp\\htdocs\\AdTags\\Screenshots\\krish"+k+".jpg"));
                    ((WebElement) keywords.get(k)).click();
                    Thread.sleep(500);

                    driver.switchTo().defaultContent();
                    driver.navigate().back();
                    Thread.sleep(5000);
                }
            }
            else
                System.out.println("Test String found");

        }
        for (int i =0;i<5;i++) {

            Dimension d = new Dimension(w,driver.manage().window().getSize().height);
            driver.manage().window().setSize(d);
            driver.navigate().refresh();
            Thread.sleep(5000);
            w = w+300;
        }

    }
}