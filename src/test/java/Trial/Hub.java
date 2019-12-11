package Trial;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class Hub {

    public static final String USERNAME = "root";
    public static final String AUTOMATE_KEY = "1qazxswed$%T1";
    public static final  String URL ="http://localhost:4444/wd/hub";
    public static void main(String[] args) throws Exception {

        DesiredCapabilities caps = new DesiredCapabilities();
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");
//        options.addArguments("--no-sandbox");

        caps.setCapability(ChromeOptions.CAPABILITY, options);
        caps.setCapability("browserName", "chrome");

        WebDriver driver = new RemoteWebDriver(new URL(URL), caps);
        driver.get("http://www.google.com");
        WebElement element = driver.findElement(By.name("q"));
        element.sendKeys("Media.net");
        element.submit();
        System.out.println(driver.getTitle());
        driver.quit();

    }
}