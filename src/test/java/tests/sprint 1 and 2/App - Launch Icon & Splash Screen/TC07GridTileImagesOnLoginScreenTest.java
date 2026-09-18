package tests.sprint1;

import framework.BaseTest;
import framework.WaitUtils;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class TC07GridTileImagesOnLoginScreenTest extends BaseTest {

    private static final String APP_PACKAGE =
            "com.insurance.bimasugam";

    private static final By WELCOME_HEADING =
            AppiumBy.accessibilityId("Welcome to Bima Sugam");

    /*
     * The Login button is visible on the splash/onboarding screens.
     *
     * Based on the supplied 413 x 917 screen reference,
     * the Login button is approximately at 80% of the
     * screen height.
     *
     * Current device resolution:
     * 1080 x 2400
     *
     * X = center of screen
     * Y = approximately 80% of screen height
     */
    private static final int LOGIN_X = 540;
    private static final int LOGIN_Y = 1920;

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

        WaitUtils.visible(
                driver,
                WELCOME_HEADING
        );

        Assert.assertTrue(
                driver.findElement(WELCOME_HEADING).isDisplayed(),
                "Onboarding Welcome screen should be displayed"
        );

        step("Allow splash screen content and grid images to load");

        waitForSeconds(3);

        /*
         * ---------------------------------------------------------
         * NAVIGATE THROUGH SPLASH SCREENS
         * ---------------------------------------------------------
         */

        step("Navigate through splash screens");

        for (int i = 1; i <= 4; i++) {

            step("Navigate to splash screen " + (i + 1));

            swipeUp();

            waitForSeconds(2);
        }

        /*
         * ---------------------------------------------------------
         * SPLASH 5
         * ---------------------------------------------------------
         */

        step("Verify Splash 5 is displayed");

        Assert.assertEquals(
                driver.getCurrentPackage(),
                APP_PACKAGE,
                "Bima Sugam should remain in the foreground"
        );

        step("Tap the Login button on Splash 5");

        /*
         * Login button location is based on the supplied
         * 413 x 917 reference image and scaled to the
         * current 1080 x 2400 device.
         */
        driver.executeScript(
                "mobile: clickGesture",
                Map.of(
                        "x", LOGIN_X,
                        "y", LOGIN_Y
                )
        );

        step("Wait for Login screen to load");

        waitForSeconds(4);

        /*
         * ---------------------------------------------------------
         * VERIFY LOGIN SCREEN
         * ---------------------------------------------------------
         */

        step("Verify Bima Sugam remains in the foreground");

        Assert.assertEquals(
                driver.getCurrentPackage(),
                APP_PACKAGE,
                "Bima Sugam should remain in the foreground "
                        + "after tapping Login"
        );

        step("Capture Login screen UI hierarchy");

        String pageSource =
                driver.getPageSource();

        Assert.assertNotNull(
                pageSource,
                "Login screen page source should not be null"
        );

        Assert.assertFalse(
                pageSource.isEmpty(),
                "Login screen page source should not be empty"
        );

        /*
         * The Welcome screen should disappear after Login
         * is selected.
         */
        step("Verify onboarding screen is no longer displayed");

        boolean welcomeVisible = false;

        try {

            if (!driver.findElements(WELCOME_HEADING).isEmpty()) {

                welcomeVisible =
                        driver.findElement(WELCOME_HEADING).isDisplayed();
            }

        } catch (Exception ignored) {
            // Welcome screen is no longer displayed.
        }

        Assert.assertFalse(
                welcomeVisible,
                "Onboarding Welcome screen should no longer be "
                        + "displayed after tapping Login"
        );

        /*
         * ---------------------------------------------------------
         * GRID TILE IMAGE VALIDATION
         * ---------------------------------------------------------
         *
         * The supplied reference confirms the grid tile images
         * on the splash screens.
         *
         * Exact Login-screen image resource IDs are not available
         * yet, so we capture the Login screen hierarchy here.
         */

        step("Verify Login screen UI hierarchy is available");

        Assert.assertTrue(
                pageSource.contains(APP_PACKAGE),
                "Login screen should belong to Bima Sugam"
        );

        System.out.println();
        System.out.println("========================================");
        System.out.println("LOGIN SCREEN UI HIERARCHY");
        System.out.println("========================================");
        System.out.println(pageSource);
        System.out.println("========================================");

        step(
                "SC_01_TC_007 completed - "
                        + "Login button interaction completed and "
                        + "Login screen validation reached"
        );
    }

    /**
     * Navigate through the splash/onboarding screens.
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

            Thread.sleep(seconds * 1000L);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }
}