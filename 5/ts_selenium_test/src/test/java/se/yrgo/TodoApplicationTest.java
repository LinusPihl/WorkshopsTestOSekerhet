package se.yrgo;

import org.junit.jupiter.api.*;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.netty.handler.timeout.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration; // Import Duration from java.time package
import java.util.List;

public class TodoApplicationTest {

    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.firefoxdriver().setup();
    }

    @BeforeEach
    void setUp() {
        driver = new FirefoxDriver();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test // 1
    @DisplayName("UndoneTasksOnFirstPageCheck")
    void undoneTasksOnFirstPageCheck() {
        // Navigate to the TODO application page
        driver.get("https://yrgo-amazing-todo-app.netlify.app");

        List<WebElement> doneTodoItems = driver.findElements(By.cssSelector("ul.todolist > li:not(.todolist__done)"));

        // Check if there are any 'done' todo items
        if (doneTodoItems.isEmpty()) {
            // If there are no 'done' todo items, the test passes
            assertTrue(true, "No 'done' todo items found on the page");
        } else {
            // If 'done' todo items are found, the test fails
            fail("Found 'done' todo items on the page");
        }
    }

    @Test // 2
    @DisplayName("DoneTasksOnFirstPageCheck")
    void doneTasksOnFirstPageCheck() {
        // Navigate to the TODO application page
        driver.get("https://yrgo-amazing-todo-app.netlify.app/done");

        List<WebElement> todoItems = driver.findElements(By.className("li.todolist__done"));

        // Check if there are any undone todo items
        if (todoItems.isEmpty()) {
            // If there are no undone todo items, the test passes
            assertTrue(true, "No undone todo items found on the page");
        } else {
            // If undone todo items are found, the test fails
            fail("Found undone todo items on the page");
        }
    }

    @Test // 3
    @DisplayName("AddNewTodo")
    void addNewTodo() {
        driver.get("https://yrgo-amazing-todo-app.netlify.app/");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Use Duration for timeout

        // Wait for the new todo input field to be visible
        WebElement newTodoInput = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".form-control")));

        // Send keys to add a new todo
        newTodoInput.sendKeys("Buy groceries\n");

        // Find the button element
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.btn")));

        // Click the button
        addButton.click();

        // Wait for the added todo to be visible
        WebElement addedTodo = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//ul[@class='todolist']/li/span[contains(text(), 'Buy groceries')]")));

        // Assert that the added todo is displayed
        assertTrue(addedTodo.isDisplayed());
    }

    @Test // 4
    @DisplayName("markTodoAsDone")
    void markTodoAsDone() {
        // Navigate to the TODO application page
        driver.get("https://yrgo-amazing-todo-app.netlify.app/");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Use Duration for timeout

        // Wait for the new todo input field to be visible
        WebElement newTodoInput = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".form-control")));

        // Send keys to add a new todo
        newTodoInput.sendKeys("Buy groceries\n");

        // Find the addButton element
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.btn")));

        // Click the button
        addButton.click();

        // Find the markAsDoneButton element
        WebElement markAsDoneButton = wait.until(ExpectedConditions
                .elementToBeClickable(By.cssSelector(".todolist > li:nth-child(1) > input:nth-child(1)")));

        // Click the button
        markAsDoneButton.click();

        // Find the goToDonePageButton element
        WebElement goToDonePageButton = wait
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.btn:nth-child(2)")));

        // Click the button
        goToDonePageButton.click();

        // Wait for the added todo to be visible
        WebElement markedAsDone = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("/html/body/div/div/div/ul/li[1]/span[text()='Buy groceries']")));

        // Assert that the added todo is displayed
        assertTrue(markedAsDone.isDisplayed());
    }

    @Test // 5
    @DisplayName("markDoneTodoAsNotDone")
    void markDoneTodoAsNotDone() {
        // Navigate to the TODO application page
        driver.get("https://yrgo-amazing-todo-app.netlify.app/");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Use Duration for timeout

        // Wait for the new todo input field to be visible
        WebElement newTodoInput = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".form-control")));

        // Send keys to add a new todo
        newTodoInput.sendKeys("Buy groceries again\n");

        // Find the addButton element
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.btn")));

        // Click the button
        addButton.click();

        // Find the markAsDoneButton element
        WebElement markAsDoneButton = wait.until(ExpectedConditions
                .elementToBeClickable(By.cssSelector(".todolist > li:nth-child(1) > input:nth-child(1)")));

        // Click the button
        markAsDoneButton.click();

        // Find the goToDonePageButton element
        WebElement goToDonePageButton = wait
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.btn:nth-child(2)")));

        // Click the button
        goToDonePageButton.click();

        // Find the makeDoneTodoAsNoneDoneButton element
        WebElement makeDoneTodoAsNoneDoneButton = wait
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector(".form-check-input")));

        // Click the button
        makeDoneTodoAsNoneDoneButton.click();

        // Find the goToTodoPageButton element
        WebElement goToTodoPageButton = wait
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.btn:nth-child(1)")));

        // Click the button
        goToTodoPageButton.click();

        // Wait for the done todo to be visible at the todopage as none done
        WebElement wasDoneButMarkedUndone = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("/html/body/div/div/main/ul/li[1]/span[text()='Buy groceries again']")));

        // Assert that the done todo but now is undone is displayed
        assertTrue(wasDoneButMarkedUndone.isDisplayed());

    }

    @Test // 6
    @DisplayName("DeleteDoneTodo")
    void deleteDoneTodo() {
        // Navigate to the TODO application page
        driver.get("https://yrgo-amazing-todo-app.netlify.app/");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Use Duration for timeout

        // Wait for the new todo input field to be visible
        WebElement newTodoInput = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".form-control")));

        // Send keys to add a new todo
        newTodoInput.sendKeys("Buy 3 apples\n");

        // Find the addButton element
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.btn")));

        // Click the button
        addButton.click();

        // Find the markAsDoneButton element
        WebElement markAsDoneButton = wait.until(ExpectedConditions
                .elementToBeClickable(By.cssSelector(".todolist > li:nth-child(1) > input:nth-child(1)")));

        // Click the button
        markAsDoneButton.click();

        // Find the goToDonePageButton element
        WebElement goToDonePageButton = wait
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.btn:nth-child(2)")));

        // Click the button
        goToDonePageButton.click();

        // Find the clickOnDeleteDoneTodoButton element
        WebElement clickOnDeleteDoneTodo = wait
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector(
                        "html body div#app div.container.bg-light.p-3 div.about ul.todolist li.todolist__done button.todo__done__remove")));

        // Click the button
        clickOnDeleteDoneTodo.click();

        // To be able to accept the popup
        Alert alert = driver.switchTo().alert();

        // Accept the alert (clicking "Ok")
        alert.accept();

        List<WebElement> doneTodoItems = driver.findElements(By.cssSelector("ul.todolist > li:not(.todolist__done)"));

        // Check if there are any 'done' todo items left
        if (doneTodoItems.isEmpty()) {
            // If there are no 'done' todo items, the test passes
            assertTrue(true, "it got removed");
        } else {
            // If 'done' todo items are found, the test fails
            fail("It wasnt removed");
        }
    }

    @Test // 7
    @DisplayName("pageStateMaintainedWhenReloadingPage")
    void pageStateMaintainedWhenReloadingPage() {
        // Navigate to the TODO application page
        driver.get("https://yrgo-amazing-todo-app.netlify.app/");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Use Duration for timeout

        // Wait for the new todo input field to be visible
        WebElement newTodoInput = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".form-control")));

        // Send keys to add a new todo
        newTodoInput.sendKeys("Buy coke\n");

        // Find the addButton element
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.btn")));

        // Click the button
        addButton.click();
        
        // Reload the page
        driver.navigate().refresh();

        try {
            Thread.sleep(2000); // Sleep for 1 second (1000 milliseconds)
        } catch (InterruptedException e) {
            // Handle the exception
        }
        
        // Wait for the done todo to be visible at the todopage as none done
        WebElement todoStillThereAfterReload = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("/html/body/div/div/main/ul/li[1]/span[text()='Buy coke']")));

        // Assert that the done todo but now is undone is displayed
        assertTrue(todoStillThereAfterReload.isDisplayed());
    }
}
