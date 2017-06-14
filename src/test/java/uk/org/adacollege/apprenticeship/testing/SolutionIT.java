package uk.org.adacollege.apprenticeship.testing;

import static org.junit.Assert.assertTrue;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.function.Function;
import java.util.UUID;

public class SolutionIT {
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static String startUrl;
    private static String myWhipbirdsMenuId = "my-whipbirds-menu";
    private static String aboutMenuId = "about-menu";
    private static String logOutMenuId = "log-out-menu";
    private static String logInMenuId = "log-in-menu";
    private static String emailInputId = "email";
    private static String passwordInputId = "password";
    private static String validEmail = "joe.halloran@adacollege.org.uk";
    private static String invalidEmail = validEmail + ".nothing";
    private static String validPassword = "whipit";
    private static String invalidPassword = validPassword + "-invalid";
    private static String logInButtonId = "login-button";
    private static String logOutButtonId = "log-out-button";
    private static String popupMessageId = "popup-message";
    private static String whipBirdName = "Barnaby Beanland" + UUID.randomUUID().toString();
    private static String whipBirdAge = "6";

    // ========= UTILITY METHODS =========

    /**
     * Source & usage: https://stackoverflow.com/a/5709805
     */
    private static Function<WebDriver, WebElement> presenceOfElementLocated(final By locator) {
        return new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                return driver.findElement(locator);
            }
        };
    }

    private static void logIn(Boolean withValidCredentials) {
        String email = withValidCredentials ? validEmail : invalidEmail;
        String password = withValidCredentials ? validPassword : invalidPassword;

        wait.until(presenceOfElementLocated(By.id(logInMenuId)));
        driver.findElement(By.id(logInMenuId)).click();

        wait.until(presenceOfElementLocated(By.id(emailInputId)));
        driver.findElement(By.id(emailInputId)).sendKeys(email);

        wait.until(presenceOfElementLocated(By.id(passwordInputId)));
        driver.findElement(By.id(passwordInputId)).sendKeys(password);

        wait.until(presenceOfElementLocated(By.id(logInButtonId)));
        driver.findElement(By.id(logInButtonId)).click();

        if (withValidCredentials) {
            wait.until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driver) {
                    return driver.getTitle().equals("whipbird: my whipbirds");
                }
            });
        }
    }

    private static void logOut() {
        Boolean isLoggedIn = (driver.findElements(By.id(logOutMenuId)).size() > 0);

        if (isLoggedIn) {
            wait.until(presenceOfElementLocated(By.id(logOutMenuId)));
            driver.findElement(By.id(logOutMenuId)).click();

            wait.until(presenceOfElementLocated(By.id(logOutButtonId)));
            driver.findElement(By.id(logOutButtonId)).click();
        }
    }

    private static void assertElementPresent(String elementId) {
        wait.until(presenceOfElementLocated(By.id(elementId)));
        assertTrue(driver.findElements(By.id(elementId)).size() == 1);
    }

    private static void assertElementNotPresent(String elementId) {
        assertTrue(driver.findElements(By.id(elementId)).size() == 0);
    }

    private static void assertTitleEquals(String expectedTitle) {
        Boolean result = wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return driver.getTitle().equals(expectedTitle);
            }
        });
        assertTrue(result);
    }

    private static void assertUrlEquals(String expectedUrl) {
        Boolean result = wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return driver.getCurrentUrl().equals(expectedUrl);
            }
        });
        assertTrue(result);
    }

    private static void assertElementTextEquals(By selector, String expectedText) {
        Boolean result = wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return driver.findElement(selector).getText().equals(expectedText);
            }
        });
        assertTrue(result);
    }

    private static void createNewUniqueWhipBird() {
        WebElement newWhipForm = driver.findElement(By.tagName("form"));
        WebElement newWhipBirdName = driver.findElement(By.name("name"));
        WebElement newWhipBirdAge = driver.findElement(By.name("age"));
        newWhipBirdName.sendKeys(whipBirdName);
        newWhipBirdAge.sendKeys(whipBirdAge);
        newWhipForm.submit();
    }

    // ========= SCAFFOLDING =========

    @BeforeClass
    public static void beforeAll() {
        startUrl = "http://whipbird.mattcalthrop.com/";
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 5);
    }

    @AfterClass
    public static void afterAll() {
        driver.close();
        driver.quit();
    }

    @Before
    public void beforeEach() {
        driver.get(startUrl);
    }

    @After
    public void afterEach() {
        logOut();
    }

    // ========= TESTS =========

    // --------- WHEN NOT LOGGED IN ---------

    // Step 1
    @Test
    public void notLoggedIn_checkMenus() {
        assertElementPresent(logInMenuId);
        assertElementTextEquals(By.id(logInMenuId), "log in");
        assertElementPresent(aboutMenuId);
        assertElementTextEquals(By.id(aboutMenuId), "about");
        assertElementNotPresent(logOutMenuId);
        assertElementNotPresent(myWhipbirdsMenuId);
    }

    // Step 2
    @Test
    public void notLoggedIn_checkCurrentPage() {
        assertUrlEquals("http://whipbird.mattcalthrop.com/#!/login");
        assertTitleEquals("whipbird: log in");
        assertElementTextEquals(By.tagName("h4"), "Log in");
        assertElementTextEquals(By.id("footer-right"), "");
    }

    // Step 3
    @Test
    public void notLoggedIn_clickAboutMenu() {
        WebElement aboutMenuItem = driver.findElement(By.id(aboutMenuId));
        aboutMenuItem.click();
        assertUrlEquals("http://whipbird.mattcalthrop.com/#!/about");
        assertTitleEquals("whipbird: about");
        assertElementTextEquals(By.tagName("h4"), "About this app");
    }

    // Step 4
    @Test
    public void notLoggedIn_logInWithIncorrectCredentials() {
        logIn(false);
        assertElementPresent(logInMenuId);
        assertElementTextEquals(By.id(logInMenuId), "log in");
        assertElementPresent(aboutMenuId);
        assertElementTextEquals(By.id(aboutMenuId), "about");
        assertElementNotPresent(logOutMenuId);
        assertElementNotPresent(myWhipbirdsMenuId);
        assertUrlEquals("http://whipbird.mattcalthrop.com/#!/login");
        assertTitleEquals("whipbird: log in");
        assertElementTextEquals(By.tagName("h4"), "Log in");
        assertElementTextEquals(By.id("popup-message"), "Username or password incorrect");
    }

    // --------- WHEN LOGGED IN ---------

    // Step 5
    @Test
    public void loggedIn_checkMenus() {
        logIn(true);
        assertElementPresent(logOutMenuId);
        assertElementTextEquals(By.id(logOutMenuId), "log out");
        assertElementPresent(aboutMenuId);
        assertElementTextEquals(By.id(aboutMenuId), "about");
        assertElementPresent(myWhipbirdsMenuId);
        assertElementTextEquals(By.id(myWhipbirdsMenuId), "my whipbirds");
        assertElementNotPresent(logInMenuId);
    }

    // Step 6
    @Test
    public void loggedIn_checkCurrentPage() {
        logIn(true);
        assertUrlEquals("http://whipbird.mattcalthrop.com/#!/my-whipbirds");
        assertTitleEquals("whipbird: my whipbirds");
        assertElementTextEquals(By.tagName("h4"), "Current whipbirds for Joe Halloran");
        assertElementTextEquals(By.id("footer-right"), "Joe Halloran");
    }

    // Step 7
    @Test
    public void loggedIn_clickLogOutMenu() {
        logIn(true);
        WebElement logOutMenuItem = driver.findElement(By.id(logOutMenuId));
        logOutMenuItem.click();
        assertUrlEquals("http://whipbird.mattcalthrop.com/#!/logout");
        assertTitleEquals("whipbird: log out");
        assertElementTextEquals(By.tagName("h4"), "Log out");
    }

    // Step 8void
    @Test
    public void loggedIn_addNewWhipbird() {
        logIn(true);
        createNewUniqueWhipBird();
        assertElementTextEquals(By.id(popupMessageId),"Whipbird added: "+ whipBirdName);
        String whipBirdsDiv = driver.findElement(By.id("whipbirds-list")).getText();
        assertTrue(whipBirdsDiv.contains(whipBirdName + ", " + whipBirdAge + " years old"));
    }

    // Step 9
    @Test
    public void loggedIn_addNewWhipbirdThenDeleteIt() {
        // TODO
    }
}
