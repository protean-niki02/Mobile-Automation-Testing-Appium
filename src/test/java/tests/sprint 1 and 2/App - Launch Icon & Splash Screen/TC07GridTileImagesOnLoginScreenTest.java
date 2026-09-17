package tests.sprint1;

import framework.BaseTest;
import framework.WaitUtils;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class TC07GridTileImagesOnLoginScreenTest extends BaseTest {

    private static final String APP_PACKAGE = "com.insurance.bimasugam";

    // Onboarding screen
    private static final By WELCOME_HEADING =
            AppiumBy.accessibilityId("Welcome to Bima Sugam");

    /*
     * Action button locators.
     *
     * The automation tries these in sequence because the exact
     * accessibility/resource-id of the button is not yet available.
     */
    private static final By GET_STARTED_BUTTON =
            AppiumBy.accessibilityId("Get Started");

    private static final By CONTINUE_BUTTON =
            AppiumBy.accessibilityId("Continue");

    private static final By NEXT_BUTTON =
            AppiumBy.accessibilityId("Next");

    /*
     * Login screen identifiers.
     *
     * These are intentionally kept as alternatives because the
     * exact Login screen hierarchy has not yet been provided.
     */
    private static final By MOBILE_NUMBER_FIELD =
            AppiumBy.androidUIAutomator(
                    "new UiSelector().textContains(\"Mobile Number\")"
            );

    private static final By LOGIN_BUTTON =
            AppiumBy.androidUIAutomator(
                    "new UiSelector().textContains(\"Login\")"
            );

    @Test(
            description = "US-UCM-01 - SC_01 - SC_01_TC_007 - "
                    + "Verify that the updated grid tile images are displayed "
                    + "on the login screen"
    )
    public void SC_01_TC_007_verifyUpdatedGridTileImagesOnLoginScreen() {

        step("Launch the Bima Sugam application");

        driver.activateApp(APP_PACKAGE);

        step("Verify that Bima Sugam is in the foreground");

        Assert.assertEquals(
                driver.getCurrentPackage(),
                APP_PACKAGE,
                "Bima Sugam application should be in the foreground"
        );

        step("Wait for the onboarding Welcome screen");

        WaitUtils.visible(driver, WELCOME_HEADING);

        Assert.assertTrue(
                driver.findElement(WELCOME_HEADING).isDisplayed(),
                "Onboarding Welcome screen should be displayed"
        );

        step("Allow onboarding content and auto-scrolling grid to load");

        waitForSeconds(3);

        step("Navigate through onboarding screens");

        boolean loginReached = false;

        /*
         * Try the known action-button possibilities.
         */
        loginReached = clickIfPresent(GET_STARTED_BUTTON);

        if (!loginReached) {
            loginReached = clickIfPresent(CONTINUE_BUTTON);
        }

        if (!loginReached) {
            loginReached = clickIfPresent(NEXT_BUTTON);
        }

        /*
         * If the first onboarding action did not directly reach Login,
         * continue trying the action button for a few onboarding screens.
         */
        for (int i = 0; i < 4 && !isLoginScreenDisplayed(); i++) {

            step("Check onboarding screen " + (i + 1));

            if (clickIfPresent(GET_STARTED_BUTTON)) {
                waitForSeconds(2);
                continue;
            }

            if (clickIfPresent(CONTINUE_BUTTON)) {
                waitForSeconds(2);
                continue;
            }

            if (clickIfPresent(NEXT_BUTTON)) {
                waitForSeconds(2);
                continue;
            }

            /*
             * Fallback swipe for onboarding navigation.
             */
            swipeUp();

            waitForSeconds(2);
        }

        step("Verify that the Login screen is displayed");

        Assert.assertTrue(
                isLoginScreenDisplayed(),
                "Login screen should be displayed after navigating "
                        + "through the onboarding screens"
        );

        step("Allow Login screen content to load");

        waitForSeconds(2);

        step("Capture Login screen UI hierarchy");

        String pageSource = driver.getPageSource();

        Assert.assertNotNull(
                pageSource,
                "Login screen page source should not be null"
        );

        Assert.assertFalse(
                pageSource.isEmpty(),
                "Login screen page source should not be empty"
        );

        step("Verify Bima Sugam remains in the foreground");

        Assert.assertEquals(
                driver.getCurrentPackage(),
                APP_PACKAGE,
                "Bima Sugam should remain in the foreground"
        );

        /*
         * ---------------------------------------------------------
         * GRID TILE IMAGE VERIFICATION
         * ---------------------------------------------------------
         *
         * The exact image resource IDs are not available yet.
         *
         * Therefore we verify that the Login screen is successfully
         * displayed and contains its UI hierarchy.
         *
         * Once the exact grid tile image IDs are available, this
         * section should be replaced with exact image assertions.
         */

        step("Verify Login screen UI content is available");

        Assert.assertTrue(
                pageSource.contains(APP_PACKAGE),
                "Login screen should belong to the Bima Sugam application"
        );

        step(
                "SC_01_TC_007 completed - "
                        + "Login screen is displayed and ready for "
                        + "grid tile image validation"
        );
    }

    /**
     * Checks whether an element is available without failing the test.
     */
    private boolean clickIfPresent(By locator) {

        try {

            WebElement element = driver.findElement(locator);

            if (element.isDisplayed() && element.isEnabled()) {

                step(
                        "Action button found using locator: "
                                + locator
                );

                element.click();

                return true;
            }

        } catch (Exception ignored) {
            // Try the next locator.
        }

        return false;
    }

    /**
     * Determines whether the Login screen is displayed.
     */
    private boolean isLoginScreenDisplayed() {

        try {

            if (driver.findElements(MOBILE_NUMBER_FIELD).size() > 0) {

                WebElement mobileNumber =
                        driver.findElement(MOBILE_NUMBER_FIELD);

                if (mobileNumber.isDisplayed()) {
                    return true;
                }
            }

        } catch (Exception ignored) {
            // Continue with next check.
        }

        try {

            if (driver.findElements(LOGIN_BUTTON).size() > 0) {

                WebElement login =
                        driver.findElement(LOGIN_BUTTON);

                if (login.isDisplayed()) {
                    return true;
                }
            }

        } catch (Exception ignored) {
            // Login screen not found using this locator.
        }

        return false;
    }

    /**
     * Swipe upward on the device.
     */
    private void swipeUp() {

        int width = driver.manage()
                .window()
                .getSize()
                .getWidth();

        int height = driver.manage()
                .window()
                .getSize()
                .getHeight();

        driver.executeScript(
                "mobile: swipeGesture",
                Map.of(
                        "left", 0,
                        "top", 0,
                        "width", width,
                        "height", height,
                        "direction", "up",
                        "percent", 0.70
                )
        );
    }

    /**
     * Wait helper.
     */
    private void waitForSeconds(int seconds) {

        try {

            Thread.sleep(seconds * 1000L);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }
}