package CM;

import CM.CMPages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class Rough {

    private WebDriver driver;
    ArrayList<String> headers = new ArrayList<String>();
    ArrayList<String> submenu = new ArrayList<String>();
    ArrayList<String> url = new ArrayList<String>();

    ArrayList<String> headers2 = new ArrayList<String>();
    ArrayList<String> submenu2 = new ArrayList<String>();
    ArrayList<String> url2 = new ArrayList<String>();

    Config config = new Config();
    String password=config.password;
    String domain = config.domain;
    String userName=config.userName;
    String email = config.email;

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver","C:\\Users\\krish.t\\Downloads\\chromedriver.exe");
        System.setProperty("webdriver.gecko.driver","C:\\Users\\krish.t\\Downloads\\geckodriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();


    }

    @Test
    public void testUntitledTestCase() throws Exception {
        driver.get("https://cm.internal.reports.mn/template/framework/pagemapping.php?id=800035358");
        CMPages c = new CMPages(driver);
        c.setSsoID(email);
        c.clickSSOIDNext();
        c.setSsoPassword(password);
        c.clickSSOPasswordNext();
        Thread.sleep(30000);
        ArrayList <String>ap=new ArrayList();
        List<WebElement> images = driver.findElements(By.xpath("//table[@id=\"imageTable\"]//tr"));
        System.out.println("total images = "+images.size());
        int i =2;
        for(WebElement w:images ){
            if(i<=images.size()){
                System.out.println(driver.findElement(By.xpath("//table[@id=\"imageTable\"]//tr["+i+"]//td[2]")).getText());
                ap.add(driver.findElement(By.xpath("//table[@id=\"imageTable\"]//tr["+i+"]//td[2]")).getText());
                i++;

            }
        }
        System.out.println(ap);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        driver.quit();
    }
}