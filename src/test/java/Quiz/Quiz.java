package Quiz;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.List;

public class Quiz {

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
        driver.get("https://quiz.forbes.com/the-ultimate-game-of-thrones-quiz-vz2/");
        int max = 4;
        int min = 1;
        int range = max - min + 1;
        for(int i = 1; i<=30;i++) {
            System.out.println("Answered "+driver.findElement(By.xpath("//span[@class='currentQ']")).getText()+" Questions out of total "+driver.findElement(By.xpath("//span[@class='totalQ']")).getText()+" Questions");
            int option = (int)(Math.random() * range) + min;
            String hint = "//div[@class=\"slide slide-"+i+" clear active\"]//span[@class=\"hint\"]";
            driver.findElement(By.xpath(hint)).click();
            Thread.sleep(1000);
            List credits = driver.findElements(By.className("credit"));
            System.out.println("\n Q"+i+" Data : ");
            System.out.println("Image Credits: "+((WebElement)credits.get(i-1)).getAttribute("innerHTML"));
            System.out.println("Hint for Q"+i+" = "+driver.findElement(By.xpath("//div[@class=\"hint-screen\"]//p[@class=\"content\"]")).getText());
            driver.findElement(By.xpath("//div[@class=\"hint-screen\"]//span[@class=\"continue\"]")).click();
            String xpath = "//div[@class='slide slide-"+i+" clear active']//li["+option+"]";
            driver.findElement(By.xpath(xpath)).click();
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,1000)");
            Thread.sleep(3000);
            driver.findElement(By.xpath("//div[@class='next-question-quiz-btn active']")).click();
            Thread.sleep(1000);
            System.out.println("*************************************************************");
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
                driver.quit();
    }
}