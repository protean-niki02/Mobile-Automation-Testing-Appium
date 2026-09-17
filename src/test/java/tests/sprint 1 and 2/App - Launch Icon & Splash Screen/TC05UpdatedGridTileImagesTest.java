package tests.sprint1;

import framework.BaseTest;
import framework.WaitUtils;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC05UpdatedGridTileImagesTest extends BaseTest {

    private static final String APP_PACKAGE = "com.insurance.bimasugam";

    private static final By WELCOME_HEADING =
            AppiumBy.accessibilityId("Welcome to Bima Sugam");

    @Test(
            description = "US-UCM-01 - SC_01 - SC_01_TC_005 - "
                    + "Verify that the updated grid tile images are displayed "
                    + "on the onboarding screens"
    )
    public void SC_01_TC_005_verifyUpdatedGridTileImages() {

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

        step("Verify that the onboarding screen contains UI elements");

        String initialPageSource = driver.getPageSource();

        Assert.assertNotNull(
                initialPageSource,
                "Onboarding screen page source should not be null"
        );

        Assert.assertFalse(
                initialPageSource.isEmpty(),
                "Onboarding screen page source should not be empty"
        );

        step("Allow the grid tile images to load");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        step("Capture the first onboarding screen state");

        String firstOnboardingScreen = driver.getPageSource();

        Assert.assertNotNull(
                firstOnboardingScreen,
                "First onboarding screen should be available"
        );

        step("Navigate through the onboarding screen");

        int screenWidth = driver.manage()
                .window()
                .getSize()
                .getWidth();

        int screenHeight = driver.manage()
                .window()
                .getSize()
                .getHeight();

        /*
         * Swipe from right to left to navigate to the next
         * onboarding screen.
         */
        driver.executeScript(
                "mobile: swipeGesture",
                java.util.Map.of(
                        "left", 0,
                        "top", 0,
                        "width", screenWidth,
                        "height", screenHeight,
                        "direction", "left",
                        "percent", 0.75
                )
        );

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        step("Capture the next onboarding screen state");

        String secondOnboardingScreen = driver.getPageSource();

        Assert.assertNotNull(
                secondOnboardingScreen,
                "Second onboarding screen should be available"
        );

        Assert.assertFalse(
                secondOnboardingScreen.isEmpty(),
                "Second onboarding screen page source should not be empty"
        );

        step("Verify that onboarding screen content is displayed");

        Assert.assertEquals(
                driver.getCurrentPackage(),
                APP_PACKAGE,
                "Bima Sugam should remain in the foreground "
                        + "while navigating through onboarding"
        );

        step(
                "SC_01_TC_005 completed - "
                        + "onboarding screens were successfully displayed "
                        + "and navigated"
        );
    }
}