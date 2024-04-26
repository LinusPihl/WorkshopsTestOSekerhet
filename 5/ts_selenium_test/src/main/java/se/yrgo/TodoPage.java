package se.yrgo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;


public class TodoPage {
    private WebDriver driver;

    public TodoPage(WebDriver driver) {
        this.driver = driver;
    }

    public void addNewTodoItem(String todoText) {
        WebElement inputField = driver.findElement(By.id("todoInput"));
        inputField.sendKeys(todoText);
        inputField.submit();
    }

    public boolean isTodoItemDisplayed(String todoText) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".todo-item"), todoText));
    }
}
