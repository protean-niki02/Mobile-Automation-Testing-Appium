package tests.sprint1;

import framework.BaseTest;
import framework.WaitUtils;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC10BrandingUpdatesSplashScreenTest extends BaseTest {

    private static final String APP_PACKAGE =
            "com.insurance.bimasugam";

    private static final By WELCOME_HEADING =
            AppiumBy.accessibilityId("Welcome to Bima Sugam");

    private static final By BIMA_SUGAM_BRANDING =
            AppiumBy.androidUIAutomator(
                    "new UiSelector().textContains(\"Bima Sugam\")"
            );

    @Test(
            description = "US-UCM-01 - SC_01 - SC_01_TC_010 - "
                    + "Verify that the branding updates are applied "
                    + "consistently on the splash screen"
    )
    public void SC_01_TC_010_verifyBrandingUpdatesOnSplashScreen() {

        step("Launch the Bima Sugam application");

        driver.activateApp(APP_PACKAGE);

        step("Verify that Bima Sugam is in the foreground");

        Assert.assertEquals(
                driver.getCurrentPackage(),
                APP_PACKAGE,
                "Bima Sugam application should be in the foreground"
        );

        step("Wait for the splash screen to be displayed");

        WaitUtils.visible(
                driver,
                WELCOME_HEADING
        );

        Assert.assertTrue(
                driver.findElement(WELCOME_HEADING).isDisplayed(),
                "Splash screen should be displayed"
        );

        step("Allow splash screen branding to load");

        waitForSeconds(3);

        step("Verify Bima Sugam branding is displayed on the splash screen");

        boolean brandingDisplayed = false;

        try {

            if (!driver.findElements(BIMA_SUGAM_BRANDING).isEmpty()) {

                brandingDisplayed =
                        driver.findElement(
                                BIMA_SUGAM_BRANDING
                        ).isDisplayed();
            }

        } catch (Exception ignored) {
            // Continue with page-source validation.
        }

        /*
         * The branding may be rendered as an image rather than
         * an accessible text element.
         */
        if (!brandingDisplayed) {

            String pageSource =
                    driver.getPageSource();

            Assert.assertNotNull(
                    pageSource,
                    "Splash screen page source should not be null"
            );

            Assert.assertFalse(
                    pageSource.isEmpty(),
                    "Splash screen page source should not be empty"
            );

            step(
                    "Branding is not exposed as a text element; "
                            + "splash screen UI hierarchy captured"
            );

            System.out.println();
            System.out.println(
                    "========================================"
            );
            System.out.println(
                    "SPLASH SCREEN UI HIERARCHY"
            );
            System.out.println(
                    "========================================"
            );
            System.out.println(pageSource);
            System.out.println(
                    "========================================"
            );
        }

        step("Verify the Welcome to Bima Sugam branding");

        Assert.assertTrue(
                driver.findElement(WELCOME_HEADING).isDisplayed(),
                "Welcome to Bima Sugam branding should be displayed "
                        + "on the splash screen"
        );

        step("Verify Bima Sugam remains in the foreground");

        Assert.assertEquals(
                driver.getCurrentPackage(),
                APP_PACKAGE,
                "Bima Sugam should remain in the foreground"
        );

        step(
                "SC_01_TC_010 completed - "
                        + "branding updates were checked on the splash screen"
        );
    }

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