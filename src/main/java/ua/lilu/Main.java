package ua.lilu;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import ua.lilu.model.PokerPlayer;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Lilu on 22.11.2016.
 */
public class Main {
    private static Random rnd = new Random();
    private static final String USERNAME_ID = "ff14642ac1c__us_login";
    private static final String EMAIL_ID = "ff14642ac1c__us_email";
    private static final String PASSWORD_ID = "ff14642ac1c__us_password";
    private static final String CONFIRM_PASSWORD_ID = "ff14642ac1c__confirm_password";
    private static final String FIRST_NAME_ID = "ff14642ac1c__us_fname";
    private static final String LAST_NAME_ID = "ff14642ac1c__us_lname";
    private static final String CITY_ID = "ff14642ac1c__us_city";
    private static final String ADDRESS_ID = "ff14642ac1c__us_address";
    private static final String PHONE_ID = "ff14642ac1c__us_phone";

    public static void main(String[] args) {
        WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        String URL = "http://80.92.229.236:81"; // Poker URL
        driver.get(URL + "/auth/login"); // Open Poker
        login(driver);
        String username = "ld" + generateRandomNumbers(3);
        String email = username + "@test.com";
        PokerPlayer player = new PokerPlayer(username, email, "123456", "John",
                "Dou", "Kiev", "Shevchenka street", generateRandomNumbers(10));
        insertPlayer(driver, player);
        String actualTitle = driver.getTitle();
        String expectedTitle = "Players";
        assertText(actualTitle, expectedTitle); // Make assertions

        searchPlayerAndClickEdit(driver, player);
        verifyPlayer(driver, player);
        player.setEmail(player.getUsername() + "@mail.ru");
        player.setFirstName("Alice");
        player.setLastName("West");
        player.setCity("Kharkov");
        player.setAddress("Nauky street");
        player.setPhone("0971234567");
        editPlayer(driver, player);
        searchPlayerAndClickEdit(driver, player);
        verifyPlayer(driver, player);

        driver.quit();
    }

    private static void login(WebDriver driver) {
        WebElement usernameInput = driver.findElement(By.id("username")); // Find username input
        usernameInput.sendKeys("admin"); // Set username

        WebElement passwordInput = driver.findElement(By.id("password")); // Find password input
        passwordInput.sendKeys("123"); // Set password

        WebElement loginButton = driver.findElement(By.id("logIn")); // Find login button
        loginButton.click(); // click on LogIn button
    }

    private static void insertPlayer(WebDriver driver, PokerPlayer player) {
        WebElement insertButton = driver.findElement(By.linkText("Insert"));
        insertButton.click();

        WebElement usernameInput = driver.findElement(By.id(USERNAME_ID));
        usernameInput.sendKeys(player.getUsername());

        WebElement emailInput = driver.findElement(By.id(EMAIL_ID));
        emailInput.sendKeys(player.getEmail());

        WebElement passwordInput = driver.findElement(By.id(PASSWORD_ID));
        passwordInput.sendKeys(player.getPassword());

        WebElement confirmPasswordInput = driver.findElement(By.id(CONFIRM_PASSWORD_ID));
        confirmPasswordInput.sendKeys(player.getPassword());

        WebElement firstNameInput = driver.findElement(By.id(FIRST_NAME_ID));
        firstNameInput.sendKeys(player.getFirstName());

        WebElement lastNameInput = driver.findElement(By.id(LAST_NAME_ID));
        lastNameInput.sendKeys(player.getLastName());

        WebElement cityInput = driver.findElement(By.id(CITY_ID));
        cityInput.sendKeys(player.getCity());

        WebElement addressInput = driver.findElement(By.id(ADDRESS_ID));
        addressInput.sendKeys(player.getAddress());

        WebElement phoneInput = driver.findElement(By.id(PHONE_ID));
        phoneInput.sendKeys(player.getPhone());

        WebElement saveButton = driver.findElement(By.name("button_save"));
        saveButton.click();
    }

    private static void searchPlayerAndClickEdit(WebDriver driver, PokerPlayer player) {
        WebElement usernameInput = driver.findElement(By.id("723a925886__login"));
        usernameInput.clear();
        usernameInput.sendKeys(player.getUsername());

        WebElement searchInput = driver.findElement(By.name("search"));
        searchInput.click();

        List<WebElement> players = driver.findElements(By.xpath(".//a[.='" + player.getUsername() + "']"));
        if (players.isEmpty()) {
            System.out.println("Failed. Player is not found!");
        } else if (players.size() > 1) {
            System.out.println("Failed. More than one player is found!");
        }

        WebElement editButton = driver.findElement(By.cssSelector("img[alt='Edit']"));
        editButton.click();
    }

    private static void editPlayer(WebDriver driver, PokerPlayer player) {
        WebElement emailInput = driver.findElement(By.id(EMAIL_ID));
        emailInput.clear();
        emailInput.sendKeys(player.getEmail());

        WebElement firstNameInput = driver.findElement(By.id(FIRST_NAME_ID));
        firstNameInput.clear();
        firstNameInput.sendKeys(player.getFirstName());

        WebElement lastNameInput = driver.findElement(By.id(LAST_NAME_ID));
        lastNameInput.clear();
        lastNameInput.sendKeys(player.getLastName());

        WebElement cityInput = driver.findElement(By.id(CITY_ID));
        cityInput.clear();
        cityInput.sendKeys(player.getCity());

        WebElement addressInput = driver.findElement(By.id(ADDRESS_ID));
        addressInput.clear();
        addressInput.sendKeys(player.getAddress());

        WebElement phoneInput = driver.findElement(By.id(PHONE_ID));
        phoneInput.clear();
        phoneInput.sendKeys(player.getPhone());

        WebElement saveButton = driver.findElement(By.name("button_save"));
        saveButton.click();
    }

    private static void verifyPlayer(WebDriver driver, PokerPlayer player) {
        verifyValue(driver, EMAIL_ID, player.getEmail());
        verifyValue(driver, FIRST_NAME_ID, player.getFirstName());
        verifyValue(driver, LAST_NAME_ID, player.getLastName());
        verifyValue(driver, CITY_ID, player.getCity());
        verifyValue(driver, ADDRESS_ID, player.getAddress());
        verifyValue(driver, PHONE_ID, player.getPhone());
    }

    private static String generateRandomNumbers(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(rnd.nextInt(10));
        }
        return sb.toString();
    }

    public static void assertText(String actualValue, String expectedValue) {
        if (actualValue.equals(expectedValue)) {
            System.out.println("Passed.");
        } else {
            System.err.println("Failed. Expected text is "
                    + expectedValue + ", but Actual title is " + actualValue);
        }
    }

    private static void verifyValue(WebDriver driver, String id, String expectedValue) {
        WebElement input = driver.findElement(By.id(id));
        String actualValue = input.getAttribute("value");
        if (actualValue.equals(expectedValue)) {
            System.out.println("Passed.");
        } else {
            System.err.println("Failed. Expected text is "
                    + expectedValue + ", but Actual text is " + actualValue);
        }
    }
}
