import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.testng.annotations.*;
import org.testng.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class Advisor {

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
        WebElement headerMenu = driver.findElement(By.className("menu-header-forbes-style-container"));
        List e= headerMenu.findElements(By.tagName("ul"));
        for(int i =0;i<e.size();i++) {
            WebElement header = ((WebElement) e.get(i));
            if (header.getText() != ""&&header.getText() !=null&&header.getText() !=" "&& !(header.getText().equalsIgnoreCase("")))
            {

                System.out.println("Header = " + header.getText());
                List subMenu = header.findElements(By.tagName("a"));
                  System.out.println("Size of submenu = "+subMenu.size());
                  headers.add(header.getText());
                String all = "All "+header.getText();
                System.out.println("All value = "+all);
                for (int j = 0; j < subMenu.size(); j++) {

                    WebElement subMenuItem = (WebElement) subMenu.get(j);
                    String text = subMenuItem.getAttribute("innerHTML");
                    //If condition not to allow all wala things and also the BrandVoice

                    if(!text.contains("<span>")&&!text.contains("<div>")&&!text.equalsIgnoreCase(all)&&!text.contains("<span>")&&!text.contains("Branc"))
                    System.out.println("Submenu item = "+subMenuItem.getAttribute("innerHTML"));
                    System.out.println("Submenu item URL = "+subMenuItem.getAttribute("href"));
                    url.add(subMenuItem.getAttribute("href"));
                    submenu.add(subMenuItem.getAttribute("innerHTML"));
                }
                System.out.println("*********************************************************************");
            }
        }
        System.out.println("Navigating to Industry page");
        System.out.println("**********************************\n\n");

        driver.get("https://www.forbes.com/industry");
         headerMenu = driver.findElement(By.className("header__channels"));
         e= headerMenu.findElements(By.tagName("ul"));
        for(int i =0;i<e.size();i++) {
            WebElement header = ((WebElement) e.get(i));
            if (header.getText() != ""&&header.getText() !=null&&header.getText() !=" "&& !(header.getText().equalsIgnoreCase("")))
            {
                System.out.println("Header = " + header.getText());
                List subMenu = header.findElements(By.tagName("a"));
                System.out.println("Size of submenu = "+subMenu.size());
                headers2.add(header.getText());
                for (int j = 0; j < subMenu.size(); j++) {
                    WebElement subMenuItem = (WebElement) subMenu.get(j);
                    System.out.println("Submenu item = "+subMenuItem.getAttribute("innerHTML"));
                    System.out.println("Submenu item URL = "+subMenuItem.getAttribute("href"));
                    url2.add(subMenuItem.getAttribute("href"));
                    submenu2.add(subMenuItem.getAttribute("innerHTML"));
                }
                System.out.println("*********************************************************************");
            }
        }
        if(headers.size()==headers2.size()){
            System.out.println("Sizes equal : "+headers2.size());
            if(headers2.equals(headers)){
                System.out.println("All the Menu headers are same!!!");
            }
        }
        else
            System.out.println("Header Mismatch!!! Reporting needed!!!!");

        if(submenu.size()==submenu2.size()){
            System.out.println("Sizes equal : "+submenu.size());
            if(submenu2.equals(submenu)){
                System.out.println("All the Sub Menu headers are same!!!");
            }
        }
        else
            System.out.println("Sub Menu item Mismatch!!! Reporting needed!!!!");

        if(url.size()==url2.size()){
            System.out.println("Sizes equal : "+url.size());
            if(url.equals(url2)){
                System.out.println("All the URLs are same!!!");
            }
        }
        else
            System.out.println("URL Mismatch!!! Reporting needed!!!!");

    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        driver.quit();
    }
}