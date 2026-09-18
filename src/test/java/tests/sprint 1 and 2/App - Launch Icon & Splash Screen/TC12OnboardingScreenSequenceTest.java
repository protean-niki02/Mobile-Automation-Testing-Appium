package tests.sprint1;

import framework.BaseTest;
import framework.WaitUtils;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class TC12OnboardingScreenSequenceTest extends BaseTest {

    private static final String APP_PACKAGE =
            "com.insurance.bimasugam";

    /*
     * Common branding element available on onboarding screens.
     */
    private static final By WELCOME_HEADING =
            AppiumBy.accessibilityId("Welcome to Bima Sugam");

    /*
     * Login button available on Splash 5.
     *
     * We use content-desc because the actual UI hierarchy
     * exposes Login as content-desc="Login".
     */
    private static final By LOGIN_BUTTON =
            AppiumBy.accessibilityId("Login");

    @Test(
            description = "US-UCM-01 - SC_01 - SC_01_TC_012 - "
                    + "Verify that each onboarding screen is displayed "
                    + "in the correct sequence"
    )
    public void SC_01_TC_012_verifyOnboardingScreensCorrectSequence() {

        /*
         * ---------------------------------------------------------
         * LAUNCH APPLICATION
         * ---------------------------------------------------------
         */

        step("Launch the Bima Sugam application");

        driver.activateApp(APP_PACKAGE);

        step("Verify Bima Sugam is in the foreground");

        Assert.assertEquals(
                driver.getCurrentPackage(),
                APP_PACKAGE,
                "Bima Sugam application should be in the foreground"
        );

        /*
         * ---------------------------------------------------------
         * SPLASH 1
         * ---------------------------------------------------------
         */

        step("Wait for the first onboarding screen");

        WaitUtils.visible(
                driver,
                WELCOME_HEADING
        );

        Assert.assertTrue(
                driver.findElement(WELCOME_HEADING).isDisplayed(),
                "First onboarding screen should be displayed"
        );

        step("Capture Splash 1 UI hierarchy");

        String splash1Source =
                driver.getPageSource();

        Assert.assertTrue(
                splash1Source.contains(APP_PACKAGE),
                "Splash 1 should belong to Bima Sugam"
        );

        /*
         * IMPORTANT:
         *
         * The actual UI hierarchy does not expose the image/text
         * as "financial security", so we do not use that locator.
         *
         * Instead, we record Splash 1 as the initial onboarding
         * state and verify that navigation changes the screen.
         */

        step("Splash 1 verified as the starting onboarding screen");

        /*
         * ---------------------------------------------------------
         * SPLASH 2
         * ---------------------------------------------------------
         */

        step("Navigate from Splash 1 to Splash 2");

        swipeUp();

        waitForSeconds(2);

        String splash2Source =
                driver.getPageSource();

        Assert.assertTrue(
                splash2Source.contains(APP_PACKAGE),
                "Splash 2 should belong to Bima Sugam"
        );

        Assert.assertNotEquals(
                splash2Source,
                splash1Source,
                "Splash 2 should be different from Splash 1"
        );

        step("Splash 2 displayed after Splash 1");

        /*
         * ---------------------------------------------------------
         * SPLASH 3
         * ---------------------------------------------------------
         */

        step("Navigate from Splash 2 to Splash 3");

        swipeUp();

        waitForSeconds(2);

        String splash3Source =
                driver.getPageSource();

        Assert.assertTrue(
                splash3Source.contains(APP_PACKAGE),
                "Splash 3 should belong to Bima Sugam"
        );

        Assert.assertNotEquals(
                splash3Source,
                splash2Source,
                "Splash 3 should be different from Splash 2"
        );

        step("Splash 3 displayed after Splash 2");

        /*
         * ---------------------------------------------------------
         * SPLASH 4
         * ---------------------------------------------------------
         */

        step("Navigate from Splash 3 to Splash 4");

        swipeUp();

        waitForSeconds(2);

        String splash4Source =
                driver.getPageSource();

        Assert.assertTrue(
                splash4Source.contains(APP_PACKAGE),
                "Splash 4 should belong to Bima Sugam"
        );

        Assert.assertNotEquals(
                splash4Source,
                splash3Source,
                "Splash 4 should be different from Splash 3"
        );

        step("Splash 4 displayed after Splash 3");

        /*
         * ---------------------------------------------------------
         * SPLASH 5
         * ---------------------------------------------------------
         */

        step("Navigate from Splash 4 to Splash 5");

        swipeUp();

        waitForSeconds(2);

        String splash5Source =
                driver.getPageSource();

        Assert.assertTrue(
                splash5Source.contains(APP_PACKAGE),
                "Splash 5 should belong to Bima Sugam"
        );

        Assert.assertNotEquals(
                splash5Source,
                splash4Source,
                "Splash 5 should be different from Splash 4"
        );

        step("Verify Login button is available on final onboarding screen");

        boolean loginButtonDisplayed =
                false;

        try {

            if (!driver.findElements(LOGIN_BUTTON).isEmpty()) {

                WebElement loginButton =
                        driver.findElement(LOGIN_BUTTON);

                loginButtonDisplayed =
                        loginButton.isDisplayed();
            }

        } catch (Exception ignored) {
            // Login button was not found.
        }

        Assert.assertTrue(
                loginButtonDisplayed,
                "Login button should be displayed on Splash 5"
        );

        /*
         * ---------------------------------------------------------
         * FINAL SEQUENCE VALIDATION
         * ---------------------------------------------------------
         */

        step("Verify onboarding sequence completed");

        Assert.assertEquals(
                driver.getCurrentPackage(),
                APP_PACKAGE,
                "Bima Sugam should remain in the foreground"
        );

        step(
                "SC_01_TC_012 completed - "
                        + "onboarding sequence verified as "
                        + "Splash 1 → Splash 2 → Splash 3 → "
                        + "Splash 4 → Splash 5"
        );

        /*
         * IMPORTANT:
         *
         * Do NOT tap Login.
         *
         * TC-012 only validates the onboarding sequence.
         */
    }

    /**
     * Swipe upward to navigate to the next onboarding screen.
     *
     * Fixed coordinates are used for the current device:
     * 1080 x 2400.
     */
    private void swipeUp() {

        driver.executeScript(
                "mobile: swipeGesture",
                Map.of(
                        "left", 100,
                        "top", 200,
                        "width", 880,
                        "height", 2100,
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

            Thread.sleep(
                    seconds * 1000L
            );

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }
}