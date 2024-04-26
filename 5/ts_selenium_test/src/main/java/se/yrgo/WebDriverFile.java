package se.yrgo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class WebDriverFile {
    public static void main(String[] args) {
        // Set system property to specify the path to GeckoDriver
        System.setProperty("webdriver.gecko.driver", "C:\\Users\\linus\\Downloads\\geckodriver-v0.34.0-win32\\geckodriver.exe");


        // Initialize Firefox WebDriver
        WebDriver driver = new FirefoxDriver();

        // Now you can use 'driver' to interact with the Firefox browser
        // Example: driver.get("https://example.com");
    }
}
