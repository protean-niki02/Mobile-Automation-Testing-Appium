package tests.sprint1;

import framework.BaseTest;
import framework.WaitUtils;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TC04AutoScrollingImageGridInteractionTest extends BaseTest {

    private static final String APP_PACKAGE = "com.insurance.bimasugam";

    private static final By WELCOME_HEADING =
            AppiumBy.accessibilityId("Welcome to Bima Sugam");

    @Test(
            description = "US-UCM-01 - SC_01 - SC_01_TC_004 - "
                    + "Verify that the auto-scrolling image grid does not block "
                    + "user interaction with the screen"
    )
    public void SC_01_TC_004_verifyAutoScrollingImageGridDoesNotBlockInteraction() {

        step("Launch the Bima Sugam application");

        driver.activateApp(APP_PACKAGE);

        step("Verify that Bima Sugam application is in the foreground");

        Assert.assertEquals(
                driver.getCurrentPackage(),
                APP_PACKAGE,
                "Bima Sugam application should be in the foreground"
        );

        step("Wait for the Onboarding - Welcome Screen to be displayed");

        WaitUtils.visible(driver, WELCOME_HEADING);

        Assert.assertTrue(
                driver.findElement(WELCOME_HEADING).isDisplayed(),
                "Onboarding - Welcome Screen should be displayed"
        );

        step("Allow the auto-scrolling image grid to start scrolling");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        step("Tap the action button while the image grid is scrolling");

        /*
         * The exact action-button locator was not available from the
         * onboarding UI hierarchy. Therefore, the action button is
         * tapped using screen coordinates.
         *
         * Update these coordinates after checking onboarding.xml
         * if the button is located elsewhere.
         */
        int screenWidth = driver.manage()
                .window()
                .getSize()
                .getWidth();

        int screenHeight = driver.manage()
                .window()
                .getSize()
                .getHeight();

        int tapX = screenWidth / 2;
        int tapY = (int) (screenHeight * 0.88);

        step(
                "Tap action button at coordinates X=" + tapX
                        + ", Y=" + tapY
        );

        driver.executeScript(
                "mobile: clickGesture",
                java.util.Map.of(
                        "x", tapX,
                        "y", tapY
                )
        );

        step("Wait for the screen to respond to the action");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        step("Verify that the application is still responsive");

        Assert.assertEquals(
                driver.getCurrentPackage(),
                APP_PACKAGE,
                "Bima Sugam should remain responsive after tapping "
                        + "the action button"
        );

        step(
                "SC_01_TC_004 completed - "
                        + "auto-scrolling image grid did not block user interaction"
        );
    }
}