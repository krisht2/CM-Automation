package Advisor;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdvisorSEOChecks {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest SEOTest;

    String url = "";
    String domain ="https://www.forbes.com/";
    Boolean topten=true;
    Boolean DFPEnabled=true;
    List<String> urls=new ArrayList<>();
    int id = 0;

    @BeforeTest
    public void setUp() {
        //Driver Paths and Setting properties
        //        String ffDriverPath ="C:\\Users\\krish.t\\Downloads\\geckodriver.exe";
        //        System.setProperty("webdriver.gecko.driver",ffDriverPath);

        String extentReportFile = System.getProperty("user.dir")+"\\SEOChecks.html";
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
        driver = new ChromeDriver(caps);
        driver.manage().window().maximize();


    }
    @Test()
    public void SEOChecks() throws IOException {
        Set<String> list = new HashSet<>();
        File file = new File("C:\\Users\\krish.t\\Desktop\\quiz\\resources\\SeoUrls.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String source;
        while ((source =br.readLine()) != null)
            list.add(source);
        System.out.println(list.size()+" "+list);
        for(String link: list) {
            try {
                SEOTest = extent.startTest("SEO TESTS : " + link);
                System.out.println(link);
                SEOTest.log(LogStatus.INFO, " URL = " + link);
                driver.get(link);
                if (driver.findElements(By.tagName("h1")).size() > 1) {
                    System.out.println("More than 1 H1 found");
                    SEOTest.log(LogStatus.FAIL, "Total number of H1 Tags : " + driver.findElements(By.tagName("h1")).size());
                } else if (driver.findElements(By.tagName("h1")).size() == 1)
                    SEOTest.log(LogStatus.PASS, "Only 1 H1 Tag");
                else
                    SEOTest.log(LogStatus.FAIL, "Total number of H1 Tags : " + driver.findElements(By.tagName("h1")).size());
                extent.endTest(SEOTest);
            } catch (Exception e) {
                SEOTest.log(LogStatus.FAIL, "Error in Selenium Script : </br>" + e);
                extent.endTest(SEOTest);
            }

            if (driver.getTitle().length() < 1)
                SEOTest.log(LogStatus.FAIL, "NO Title" + driver.getTitle());
            else
                SEOTest.log(LogStatus.PASS, "Title : " + driver.getTitle());

            String s = driver.getPageSource();
            int start = s.indexOf("<link rel=\"canonical\" href=");
            int stop = start + 2;
            try {
                String key = s.substring(start, s.indexOf(">", stop)) + ">";
                System.out.println(key);
                SEOTest.log(LogStatus.PASS, key.replaceAll("<", "&lt; ").replaceAll(">", "&gt;"));
            } catch (Exception ex) {//Testcase fail
                System.out.println("Could not find");
                SEOTest.log(LogStatus.FAIL, "Could not find canonical link");

            }

            start = s.indexOf("<link rel=\"amphtml\" href=");
            stop = start + 2;
            try {
                String key = s.substring(start, s.indexOf(">", stop)) + ">";
                System.out.println(key);
                SEOTest.log(LogStatus.PASS, key.replaceAll("<", "&lt; ").replaceAll(">", "&gt;"));

            } catch (Exception ex) {//Testcase fail
                System.out.println("Could not find");
                SEOTest.log(LogStatus.FAIL, "could not find AMP version");

            }

            start = s.indexOf("<script type=\"application/ld+json\">");
            stop = start + 2;
            try {
                String schema = s.substring(start, s.indexOf("</script>", stop)) + "</script>";
                if (schema.length() > 46) {
                    System.out.println(schema);
                    SEOTest.log(LogStatus.PASS, schema.replaceAll("<", "&lt; ").replaceAll(">", "&gt;"));

                } else {
                    System.out.println("FAIL");
                    SEOTest.log(LogStatus.FAIL, "could not find Schema.org");

                }
            } catch (Exception ex) {//Testcase fail
                System.out.println("Could not find");
                SEOTest.log(LogStatus.FAIL, "could not find Schema.org");

            }

            List<WebElement> elements = driver.findElements(By.tagName("meta"));
            checkPresence("og:site_name", "property", elements);
            checkPresence("og:title", "property", elements);
            checkPresence("og:url", "property", elements);
            checkPresence("og:type", "property", elements);
            checkPresence("og:description", "property", elements);
            checkPresence("og:image", "property", elements);
            checkPresence("name", "itemprop", elements);
            checkPresence("headline", "itemprop", elements);
            checkPresence("description", "itemprop", elements);
            checkPresence("image", "itemprop", elements);
            checkPresence("author", "itemprop", elements);
            checkPresence("twitter:title", "name", elements);
            checkPresence("twitter:url", "name", elements);
            checkPresence("twitter:description", "name", elements);
            checkPresence("twitter:image", "name", elements);
            checkPresence("twitter:card", "name", elements);
            checkPresence("twitter:site", "name", elements);
            checkPresence("description", "name", elements);
            checkPresence("robots", "name", elements);

            String code2 = driver.getPageSource().replaceAll("amp;", "");
            System.out.println(code2);
            String script = "<script async=\"async\" src=\"//consent.trustarc.com/notice?domain=forbes.com&js=nj&noticeType=bb&c=teconsent\" crossorigin";
            if (code2.toLowerCase().contains(script.toLowerCase()))
                SEOTest.log(LogStatus.PASS, "CCPA Script present");
            else
                SEOTest.log(LogStatus.FAIL, "CCPA Not Script present");

            extent.endTest(SEOTest);
        }
    }
    @AfterTest
    public void closeTest(){
        driver.quit();
        extent.flush();
        extent.close();
        try {
            sendEmail();
        }
        catch (Exception e){System.out.println(e);}
    }

    public void checkPresence(String key,String attribute,List<WebElement> elements){
        int flag =0;
        WebElement reference=null;
        for(WebElement w : elements) {
            try {
                if (w.getAttribute(attribute).contains(key) && w.getAttribute("content").length() > 0)
                {flag = 1;
                    reference=w;
                }
            }catch (Exception e){}
        }
        if(flag ==0) {
            System.out.println("Not found : " + key);
            SEOTest.log(LogStatus.FAIL,"could not find : "+key);

        }
        else {
            System.out.println(" found : " + key);
            System.out.println("Content : " + reference.getAttribute("content"));
            SEOTest.log(LogStatus.PASS,"Found : "+key+"<br/> Content ="+reference.getAttribute("content"));

        }
    }
    public void sendEmail() throws EmailException {
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath(System.getProperty("user.dir")+"\\AdvisorHomepage.html");
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        String to []={"krish.t@media.net","saili.m@media.net","fenil.g@media.net"};

        MultiPartEmail email = new MultiPartEmail();
        email.setHostName("smtp.googlemail.com");
        email.setSmtpPort(587);
        email.setAuthenticator(new DefaultAuthenticator("krish.t@media.net", "fstomkzuhqlyvcuv"));
        email.setSSLOnConnect(true);
        email.addTo("krish.t@media.net");
        email.setFrom("krish.t@media.net", "Krish");
        email.setSubject("Advisor Tracking - Automation Report");
        email.setMsg("PFA The below Advisor Tracking");

        email.attach(attachment);

        email.send();
        System.out.println("Email method executed");
    }
}
