import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class Lighthouse {
    WebDriver driver;
    ExtentReports extent;
    ExtentTest logger;
    List<String> mode =new ArrayList<>();
    WebDriverWait wait;
    int i =0;
    @BeforeTest
                public void setUp() {
//            Driver Paths and Setting properties
//        String ffDriverPath ="C:\\Users\\krish.t\\Downloads\\geckodriver.exe";
//        System.setProperty("webdriver.gecko.driver",ffDriverPath);
            String extentReportFile = System.getProperty("user.dir")+"\\Lighthouse.html";
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
            caps.setCapability("goog:loggingPrefs", logs );
            caps.setCapability(CapabilityType.LOGGING_PREFS, logs);
            mode.add("mobile");
            mode.add("desktop");
        }
    @Test(invocationCount = 2)
    public void lightHouse() throws Exception{
        Set<String> list = new HashSet<>();
        File file = new File("C:\\Users\\krish.t\\Desktop\\quiz\\resources\\AdHoc.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String s;
        while ((s =br.readLine()) != null)
            list.add(s);
        System.out.println(list.size()+" "+list);
            for(String link: list) {
                try{
                if (link != null) {
                    String type = mode.get(i);
                    logger = extent.startTest(type +" : Lighthouse : " + link);
                    System.out.println(link + "\n*************************************************************************");
                    driver = new ChromeDriver();
                    wait = new WebDriverWait(driver, 300);
                    String testURL = "https://googlechrome.github.io/lighthouse/viewer/?psiurl=" + link + "&strategy=" + type + "&category=performance&category=accessibility&category=best-practices&category=seo&utm_source=lh-chrome-ext";
                    driver.get(testURL);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='lh-header-container']//a[1]//div[2]")));
                    System.out.println(driver.findElement(By.className("lh-gauge__percentage")).getText());
                    logger.log(LogStatus.INFO, "URL = " + link);
                    for (int i = 1; i < 5; i++) {
                        System.out.println("**************");
                        String score = driver.findElement(By.xpath("//div[@class='lh-header-container']//a[" + i + "]//div[2]")).getText();
                        System.out.println(score);
                        String category = driver.findElement(By.xpath("//div[@class='lh-header-container']//a[" + i + "]//div[3]")).getText();
                        System.out.println(category);

                        if (Integer.parseInt(score) < 40)
                            logger.log(LogStatus.FATAL, category + " : " + score);
                        else if (Integer.parseInt(score) < 60)
                            logger.log(LogStatus.FAIL, category + " : " + score);
                        else
                            logger.log(LogStatus.PASS, category + " : " + score);

                    }
                    driver.quit();
                    extent.endTest(logger);
                }}catch (Exception e){logger.log(LogStatus.SKIP,"Timed out while auditing the URL : "+link);
                driver.quit();
                extent.endTest(logger);}
            }
        i++;
    }
    @AfterTest
    public void closeTest(){
       driver.quit();
       extent.endTest(logger);
       extent.flush();
       try {
           sendEmail();
       }catch (Exception e){}
       extent.close();
    }

    public void sendEmail() throws EmailException {
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath(System.getProperty("user.dir")+"\\Lighthouse.html");
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        String to []={"krish.t@media.net","saili.m@media.net","fenil.g@media.net"};

        MultiPartEmail email = new MultiPartEmail();
        email.setHostName("smtp.googlemail.com");
        email.setSmtpPort(587);
        email.setAuthenticator(new DefaultAuthenticator("krish.t@media.net", "fstomkzuhqlyvcuv"));
        email.setSSLOnConnect(true);
        email.addTo(to);
        email.setFrom("krish.t@media.net", "Krish");
        email.setSubject("Lighthouse Test - Automation Report");
        email.setMsg("PFA The below Lighthouse Audit Report");
        email.attach(attachment);
        email.send();
    }
}