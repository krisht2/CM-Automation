package Advisor;



import com.relevantcodes.extentreports.LogStatus;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rough {

    private WebDriver driver;

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver","C:\\Users\\krish.t\\Downloads\\chromedriver.exe");
        System.setProperty("webdriver.gecko.driver","C:\\Users\\krish.t\\Downloads\\geckodriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();


    }

    @Test
    public void testUntitledTestCase() throws Exception {
        Pattern p = Pattern.compile("(.*&#[0-9]{1,4};.*)|(.*&[a-z A-Z]{1,4};.*)");

//        driver.get("https://www.forbes.com/advisor");
//        WebElement element = driver.findElement(By.tagName("p"));
//        ((JavascriptExecutor) driver).executeScript("var ele=arguments[0]; ele.innerText = 'sasdss&#60;sadasdds';", element);
//

        boolean flag = true;
        while (flag) {
            String body = driver.findElement(By.tagName("body")).getText();
            //System.out.println(body);
            //Matcher m = p.matcher(body);
            //Matcher k = p.matcher("sdfsddf&nbsp;dsdfddf");
            String items[] = body.split("\\n");
            for (String item : items) {
            //    System.out.println(p.matcher(item).matches() + " : " + item);
                if (p.matcher(item).matches())
                    System.out.println("Entity found : "+item);
                    //logger.log(LogStatus.FAIL, "HTML Entity found : " + item);
            }
            try{

                WebElement w = driver.findElement(By.xpath("//span[@class='media-credit']"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", w);

                Thread.sleep(500);
                driver.findElement(By.xpath("//div[@class='continue']")).click();
            }
            catch (Exception e){
                System.out.println(Arrays.toString(e.getStackTrace()));
                try{
                    ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight);");
                    Thread.sleep(500);
                    ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight);");
                    Thread.sleep(500);
                    driver.findElement(By.className("next-slide-btn-text")).click();

                }
                catch (Exception ex) {

                    System.out.println(e.getStackTrace());
                    flag=false;
                }
            }
        }
    }
    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        driver.quit();
    }
}