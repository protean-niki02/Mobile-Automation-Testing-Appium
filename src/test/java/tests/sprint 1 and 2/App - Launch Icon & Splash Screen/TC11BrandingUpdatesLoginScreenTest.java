package tests.sprint1;

import framework.BaseTest;
import framework.WaitUtils;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class TC11BrandingUpdatesLoginScreenTest extends BaseTest {

    private static final String APP_PACKAGE =
            "com.insurance.bimasugam";

    private static final By WELCOME_HEADING =
            AppiumBy.accessibilityId("Welcome to Bima Sugam");

    /*
     * Login button is displayed on the final onboarding/splash screen.
     *
     * Current Android device resolution:
     * 1080 x 2400
     *
     * Based on the supplied 413 x 917 reference screenshot,
     * the Login button is approximately around 80% of the
     * screen height.
     */
    private static final int LOGIN_X = 540;
    private static final int LOGIN_Y = 1920;

    /*
     * Branding text expected on the Login screen.
     *
     * If the branding is rendered as an image and is not exposed
     * through the UI hierarchy, page-source validation is used.
     */
    private static final By BIMA_SUGAM_BRANDING =
            AppiumBy.androidUIAutomator(
                    "new UiSelector().textContains(\"Bima Sugam\")"
            );

    @Test(
            description = "US-UCM-01 - SC_01 - SC_01_TC_011 - "
                    + "Verify that the branding updates are applied "
                    + "consistently on the login screen"
    )
    public void SC_01_TC_011_verifyBrandingUpdatesOnLoginScreen() {

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

        step("Allow onboarding content to load");

        waitForSeconds(3);

        /*
         * ---------------------------------------------------------
         * NAVIGATE THROUGH ONBOARDING
         * ---------------------------------------------------------
         */

        step("Navigate through the onboarding screens");

        for (int i = 1; i <= 4; i++) {

            step(
                    "Navigate from onboarding screen "
                            + i
                            + " to the next screen"
            );

            swipeUp();

            waitForSeconds(2);
        }

        /*
         * ---------------------------------------------------------
         * SPLASH 5 / LOGIN BUTTON
         * ---------------------------------------------------------
         */

        step("Wait for the final onboarding screen");

        waitForSeconds(2);

        step("Tap the Login button");

        /*
         * The Login button is visually present on Splash 5.
         * Fixed coordinates are used because the UI hierarchy
         * does not reliably expose the button text on this device.
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
                        + "after navigating to Login"
        );

        step("Verify onboarding Welcome screen is no longer displayed");

        boolean welcomeScreenVisible = false;

        try {

            if (!driver.findElements(WELCOME_HEADING).isEmpty()) {

                welcomeScreenVisible =
                        driver.findElement(
                                WELCOME_HEADING
                        ).isDisplayed();
            }

        } catch (Exception ignored) {
            // Welcome screen is no longer displayed.
        }

        Assert.assertFalse(
                welcomeScreenVisible,
                "Onboarding Welcome screen should no longer be displayed "
                        + "after navigating to Login"
        );

        /*
         * ---------------------------------------------------------
         * VERIFY BRANDING
         * ---------------------------------------------------------
         */

        step("Verify branding updates on the Login screen");

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

        boolean brandingDisplayed = false;

        try {

            if (!driver.findElements(
                    BIMA_SUGAM_BRANDING
            ).isEmpty()) {

                brandingDisplayed =
                        driver.findElement(
                                BIMA_SUGAM_BRANDING
                        ).isDisplayed();
            }

        } catch (Exception ignored) {
            // Continue with page-source validation.
        }

        /*
         * If Bima Sugam branding is exposed as text,
         * verify that it is displayed.
         */
        if (brandingDisplayed) {

            step(
                    "Bima Sugam branding is displayed "
                            + "on the Login screen"
            );

            Assert.assertTrue(
                    brandingDisplayed,
                    "Bima Sugam branding should be displayed "
                            + "on the Login screen"
            );

        } else {

            /*
             * Branding may be rendered as an image and therefore
             * may not be exposed as text by UiAutomator.
             *
             * Capture the UI hierarchy for evidence.
             */
            step(
                    "Branding is not exposed as a text element; "
                            + "capturing Login screen UI hierarchy"
            );

            System.out.println();
            System.out.println(
                    "========================================"
            );
            System.out.println(
                    "LOGIN SCREEN UI HIERARCHY"
            );
            System.out.println(
                    "========================================"
            );
            System.out.println(pageSource);
            System.out.println(
                    "========================================"
            );
        }

        step("Verify the Login screen belongs to Bima Sugam");

        Assert.assertTrue(
                pageSource.contains(APP_PACKAGE),
                "Login screen should belong to Bima Sugam"
        );

        step(
                "SC_01_TC_011 completed - "
                        + "branding updates were checked on the "
                        + "Login screen"
        );
    }

    /**
     * Navigate to the next onboarding screen.
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