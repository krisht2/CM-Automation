import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class Advisor2 {

    private WebDriver driver;
    ArrayList<String> headers = new ArrayList<String>();
    ArrayList<String> submenu = new ArrayList<String>();
    ArrayList<String> url = new ArrayList<String>();

    ArrayList<String> headers2 = new ArrayList<String>();
    ArrayList<String> submenu2 = new ArrayList<String>();
    ArrayList<String> url2 = new ArrayList<String>();

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver","C:\\Users\\krish.t\\Downloads\\chromedriver.exe");
        System.setProperty("webdriver.gecko.driver","C:\\Users\\krish.t\\Downloads\\geckodriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void testUntitledTestCase() throws Exception {
        driver.get("https://www.forbes.com/advisor");
        WebElement nav = driver.findElement(By.className("menu-header-forbes-style-container"));
        List headers = nav.findElements(By.tagName("ul"));
        System.out.println("Size = "+headers.size());
        for(int i =0;i<headers.size();i++){
            System.out.println(((WebElement)headers.get(i)).getText());
        }
        System.out.println("Complete");
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        driver.quit();
    }
}