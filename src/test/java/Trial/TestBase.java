package Trial;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.Logs;
import org.testng.TestNG;
import org.testng.annotations.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.*;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
public class TestBase {

    @DataProvider(name ="driver")
    public Object[] sendDriver(){  String chromeDriverPath = "C:\\Users\\krish.t\\Downloads\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);return new Object[]{new Object[]{new ChromeDriver()}};}
    static TestNG testSuite = new TestNG();
//    public static void main(String args[])
//    {
//        String chromeDriverPath = "C:\\Users\\krish.t\\Downloads\\chromedriver.exe";
//        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
//        testSuite.setTestClasses(new Class[] { TestChild.class });
//        testSuite.setDefaultSuiteName("My Test Suite");
//        testSuite.setDefaultTestName("My Test");
//        testSuite.run();
//
//    }
    public void sendEmail(String attachmentPath) throws EmailException {
        EmailAttachment attachment = new EmailAttachment();
       // attachment.setPath(System.getProperty("user.dir")+"\\AdvisorHeader.html");
        attachment.setPath(attachmentPath);
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        String to []={"krish.t@media.net","saili.m@media.net","fenil.g@media.net"};

        MultiPartEmail email = new MultiPartEmail();
        email.setHostName("smtp.googlemail.com");
        email.setSmtpPort(587);
        email.setAuthenticator(new DefaultAuthenticator("krish.t@media.net", "fstomkzuhqlyvcuv"));
        email.setSSLOnConnect(true);
        email.addTo(to);
        email.setFrom("krish.t@media.net", "Krish");
        email.setSubject("Advisor - Header Test - Automation Report");
        email.setMsg("PFA The below Advisor Header Test Report");
        email.attach(attachment);
        email.send();
    }
}
