package Advisor;

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
        driver.get("https://www.forbes.com/");
        List temp = driver.findElement(By.xpath("//ul[@class='header__channels']")).findElements(By.tagName("li"));
        ArrayList <String> list = new ArrayList<>();
        System.out.println("Size = "+temp.size());
        for(int i =0;i<temp.size();i++){
            try {
                String k = ((WebElement) temp.get(i)).findElement(By.className("header__title")).getText();

            if(k.length()>1) {
                System.out.println(k);
                list.add(k);
                List e =((WebElement)temp.get(i)).findElements(By.tagName("li"));
                System.out.println("Cpontains "+e.size()+" Submenu items");
                for(int x =0;x<e.size();x++)
                {
                    System.out.println("Submenu Item : "+((WebElement)e.get(i)).findElement(By.tagName("a")).getAttribute("innerHTML"));
                }

            }
            }            catch (Exception e){}

        }
        System.out.println("Complete");
    System.out.println(list.size()+" "+list);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        driver.quit();
    }
}