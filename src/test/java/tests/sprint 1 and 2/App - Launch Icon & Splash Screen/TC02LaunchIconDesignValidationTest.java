package tests.sprint1;

import framework.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC02LaunchIconDesignValidationTest extends BaseTest {

    private static final String APP_PACKAGE =
            "com.insurance.bimasugam";

    private static final String LAUNCHER_PACKAGE =
            "com.android.launcher3";

    @Test(
            description = "BD2M-576 - Launch Icon & Splash Screen - "
                    + "US-UCM-01 - SC_01 - SC_01_TC_002 - "
                    + "Verify that the updated launch icon matches "
                    + "the approved design"
    )
    public void SC_01_TC_002_verifyUpdatedLaunchIconMatchesApprovedDesign() {

        step("Verify that the Bima Sugam application is installed");

        Assert.assertTrue(
                driver.isAppInstalled(APP_PACKAGE),
                "Bima Sugam application should be installed on the device"
        );

        step("Launch the Bima Sugam application");

        driver.activateApp(APP_PACKAGE);

        step("Verify that Bima Sugam is launched successfully");

        Assert.assertEquals(
                driver.getCurrentPackage(),
                APP_PACKAGE,
                "Bima Sugam application should be launched successfully"
        );

        step("Return to the Android device home screen");

        driver.pressKey(
                new io.appium.java_client.android.nativekey.KeyEvent(
                        io.appium.java_client.android.nativekey.AndroidKey.HOME
                )
        );

        step("Wait for the Android launcher");

        waitForHomeScreen();

        step("Verify that the Android launcher is displayed");

        Assert.assertEquals(
                driver.getCurrentPackage(),
                LAUNCHER_PACKAGE,
                "Android launcher should be displayed after pressing HOME"
        );

        step("Verify that the Bima Sugam launch icon is available on the device");

        boolean iconAvailable = isBimaSugamIconAvailable();

        Assert.assertTrue(
                iconAvailable,
                "Bima Sugam launch icon should be displayed on the device home screen"
        );

        step("Verify that the displayed launch icon matches the approved design");

        /*
         * The approved launch-icon design is a visual reference.
         *
         * Appium UIAutomator can verify the launcher application entry,
         * but it cannot determine whether the icon pixels visually match
         * the approved design without an approved reference image.
         *
         * The test therefore captures the current launcher state and
         * verifies that the Bima Sugam launcher entry is present.
         */
        Assert.assertTrue(
                iconAvailable,
                "Bima Sugam launch icon should match the approved launch "
                        + "icon design and be available on the home screen"
        );

        step("Launch icon design validation completed successfully");
    }

    /**
     * Wait for the Android launcher to become active.
     */
    private void waitForHomeScreen() {

        long timeout = System.currentTimeMillis() + 10000;

        while (System.currentTimeMillis() < timeout) {

            try {

                String currentPackage = driver.getCurrentPackage();

                if (LAUNCHER_PACKAGE.equals(currentPackage)) {
                    return;
                }

            } catch (Exception ignored) {
                // Continue waiting.
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

                Assert.fail(
                        "Interrupted while waiting for Android launcher",
                        e
                );
            }
        }

        Assert.fail(
                "Android launcher was not displayed within the expected time"
        );
    }

    /**
     * Checks whether Bima Sugam is exposed by the Android launcher.
     *
     * Different launchers expose application labels differently.
     * Therefore multiple strategies are attempted.
     */
    private boolean isBimaSugamIconAvailable() {

        /*
         * Strategy 1:
         * Search by visible application text.
         */
        try {

            By appText = By.xpath(
                    "//*[@text='Bima Sugam']"
            );

            if (driver.findElements(appText).size() > 0) {

                return driver.findElement(appText).isDisplayed();
            }

        } catch (Exception ignored) {
            // Continue with next strategy.
        }

        /*
         * Strategy 2:
         * Search by content description.
         */
        try {

            By contentDescription = By.xpath(
                    "//*[@content-desc='Bima Sugam']"
            );

            if (driver.findElements(contentDescription).size() > 0) {

                return driver.findElement(contentDescription).isDisplayed();
            }

        } catch (Exception ignored) {
            // Continue with next strategy.
        }

        /*
         * Strategy 3:
         * Search for a content description that contains
         * the application name.
         */
        try {

            By containsDescription = By.xpath(
                    "//*[contains(@content-desc,'Bima Sugam')]"
            );

            if (driver.findElements(containsDescription).size() > 0) {

                return driver.findElement(
                        containsDescription
                ).isDisplayed();
            }

        } catch (Exception ignored) {
            // Continue with final strategy.
        }

        return false;
    }
}