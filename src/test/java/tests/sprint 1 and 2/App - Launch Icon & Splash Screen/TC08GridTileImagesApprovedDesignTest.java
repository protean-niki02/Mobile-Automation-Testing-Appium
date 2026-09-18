package tests.sprint1;

import framework.BaseTest;
import framework.WaitUtils;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class TC08GridTileImagesApprovedDesignTest extends BaseTest {

    private static final String APP_PACKAGE =
            "com.insurance.bimasugam";

    private static final By WELCOME_HEADING =
            AppiumBy.accessibilityId("Welcome to Bima Sugam");

    /*
     * Approved design folder.
     */
    private static final String APPROVED_DESIGN_FOLDER =
            "src/test/resources/approved/";

    /*
     * Minimum similarity required between the actual screenshot
     * and approved reference image.
     *
     * 0.95 = 95% similarity.
     */
    private static final double MINIMUM_SIMILARITY = 0.95;

    /*
     * Current device reference:
     * 1080 x 2400
     *
     * The supplied design screenshot was 413 x 917.
     *
     * Grid image area is approximately the upper portion of
     * the splash screen.
     */
    private static final int GRID_LEFT = 0;
    private static final int GRID_TOP = 0;
    private static final int GRID_WIDTH = 1080;
    private static final int GRID_HEIGHT = 850;

    @Test(
            description = "US-UCM-01 - SC_01 - SC_01_TC_008 - "
                    + "Verify that the updated grid tile images match "
                    + "the approved design"
    )
    public void SC_01_TC_008_verifyUpdatedGridTileImagesMatchApprovedDesign()
            throws IOException {

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

        step("Allow grid tile images to load");

        waitForSeconds(3);

        /*
         * ---------------------------------------------------------
         * SPLASH / ONBOARDING SCREEN 1
         * ---------------------------------------------------------
         */

        step("Verify grid tile images on Splash 1");

        verifyGridTileDesign(
                "splash1.png",
                "Splash 1"
        );

        /*
         * ---------------------------------------------------------
         * SPLASH 2
         * ---------------------------------------------------------
         */

        navigateToNextSplashScreen();

        step("Verify grid tile images on Splash 2");

        verifyGridTileDesign(
                "splash2.png",
                "Splash 2"
        );

        /*
         * ---------------------------------------------------------
         * SPLASH 3
         * ---------------------------------------------------------
         */

        navigateToNextSplashScreen();

        step("Verify grid tile images on Splash 3");

        verifyGridTileDesign(
                "splash3.png",
                "Splash 3"
        );

        /*
         * ---------------------------------------------------------
         * SPLASH 4
         * ---------------------------------------------------------
         */

        navigateToNextSplashScreen();

        step("Verify grid tile images on Splash 4");

        verifyGridTileDesign(
                "splash4.png",
                "Splash 4"
        );

        /*
         * ---------------------------------------------------------
         * SPLASH 5
         * ---------------------------------------------------------
         */

        navigateToNextSplashScreen();

        step("Verify grid tile images on Splash 5");

        verifyGridTileDesign(
                "splash5.png",
                "Splash 5"
        );

        /*
         * ---------------------------------------------------------
         * LOGIN
         * ---------------------------------------------------------
         *
         * The Login button is visually present on Splash 5.
         * Use its screen position based on the supplied design.
         */

        step("Tap Login from Splash 5");

        driver.executeScript(
                "mobile: clickGesture",
                Map.of(
                        "x", 540,
                        "y", 1920
                )
        );

        waitForSeconds(4);

        step("Verify that Bima Sugam remains in the foreground");

        Assert.assertEquals(
                driver.getCurrentPackage(),
                APP_PACKAGE,
                "Bima Sugam should remain in the foreground "
                        + "after tapping Login"
        );

        step("Verify Login screen is displayed");

        String loginPageSource =
                driver.getPageSource();

        Assert.assertNotNull(
                loginPageSource,
                "Login screen page source should not be null"
        );

        Assert.assertFalse(
                loginPageSource.isEmpty(),
                "Login screen page source should not be empty"
        );

        step("Compare Login screen grid tile images with approved design");

        verifyGridTileDesign(
                "login.png",
                "Login screen"
        );

        step(
                "SC_01_TC_008 completed - "
                        + "grid tile images were compared against "
                        + "the approved design"
        );
    }

    /**
     * Captures the current screen and compares the grid-image area
     * with the approved reference image.
     */
    private void verifyGridTileDesign(
            String approvedFileName,
            String screenName
    ) throws IOException {

        step(
                "Capture grid tile image area from "
                        + screenName
        );

        File screenshot =
                driver.getScreenshotAs(
                        org.openqa.selenium.OutputType.FILE
                );

        BufferedImage actualImage =
                ImageIO.read(screenshot);

        Assert.assertNotNull(
                actualImage,
                "Actual screenshot could not be read for "
                        + screenName
        );

        File approvedFile =
                new File(
                        APPROVED_DESIGN_FOLDER
                                + approvedFileName
                );

        Assert.assertTrue(
                approvedFile.exists(),
                "Approved design file is missing: "
                        + approvedFile.getAbsolutePath()
        );

        BufferedImage approvedImage =
                ImageIO.read(approvedFile);

        Assert.assertNotNull(
                approvedImage,
                "Approved design image could not be read: "
                        + approvedFileName
        );

        BufferedImage actualGrid =
                cropGridArea(actualImage);

        BufferedImage approvedGrid =
                cropGridArea(approvedImage);

        double similarity =
                calculateImageSimilarity(
                        actualGrid,
                        approvedGrid
                );

        System.out.println();
        System.out.println("========================================");
        System.out.println("VISUAL COMPARISON");
        System.out.println("Screen     : " + screenName);
        System.out.println("Reference  : " + approvedFileName);
        System.out.println(
                "Similarity : "
                        + String.format("%.2f%%", similarity * 100)
        );
        System.out.println(
                "Required   : "
                        + String.format(
                                "%.2f%%",
                                MINIMUM_SIMILARITY * 100
                        )
        );
        System.out.println("========================================");

        Assert.assertTrue(
                similarity >= MINIMUM_SIMILARITY,
                screenName
                        + " grid tile images do not match the "
                        + "approved design. Similarity = "
                        + String.format(
                                "%.2f%%",
                                similarity * 100
                        )
        );

        step(
                screenName
                        + " grid tile images match the approved design"
                        + " with similarity "
                        + String.format(
                                "%.2f%%",
                                similarity * 100
                        )
        );
    }

    /**
     * Crop the grid tile area from the screenshot.
     */
    private BufferedImage cropGridArea(
            BufferedImage image
    ) {

        int x = Math.min(
                GRID_LEFT,
                image.getWidth() - 1
        );

        int y = Math.min(
                GRID_TOP,
                image.getHeight() - 1
        );

        int width = Math.min(
                GRID_WIDTH,
                image.getWidth() - x
        );

        int height = Math.min(
                GRID_HEIGHT,
                image.getHeight() - y
        );

        return image.getSubimage(
                x,
                y,
                width,
                height
        );
    }

    /**
     * Calculates basic pixel similarity between two images.
     *
     * The images are scaled to the same dimensions before comparison.
     */
    private double calculateImageSimilarity(
            BufferedImage actual,
            BufferedImage approved
    ) {

        BufferedImage resizedActual =
                resizeImage(
                        actual,
                        approved.getWidth(),
                        approved.getHeight()
                );

        long totalPixels =
                (long) approved.getWidth()
                        * approved.getHeight();

        long matchingPixels = 0;

        for (int y = 0;
             y < approved.getHeight();
             y++) {

            for (int x = 0;
                 x < approved.getWidth();
                 x++) {

                int actualRgb =
                        resizedActual.getRGB(x, y);

                int approvedRgb =
                        approved.getRGB(x, y);

                int actualRed =
                        (actualRgb >> 16) & 0xFF;

                int actualGreen =
                        (actualRgb >> 8) & 0xFF;

                int actualBlue =
                        actualRgb & 0xFF;

                int approvedRed =
                        (approvedRgb >> 16) & 0xFF;

                int approvedGreen =
                        (approvedRgb >> 8) & 0xFF;

                int approvedBlue =
                        approvedRgb & 0xFF;

                int redDifference =
                        Math.abs(
                                actualRed - approvedRed
                        );

                int greenDifference =
                        Math.abs(
                                actualGreen - approvedGreen
                        );

                int blueDifference =
                        Math.abs(
                                actualBlue - approvedBlue
                        );

                /*
                 * Pixel considered matching when the RGB
                 * difference is within tolerance.
                 */
                if (redDifference <= 15
                        && greenDifference <= 15
                        && blueDifference <= 15) {

                    matchingPixels++;
                }
            }
        }

        return (double) matchingPixels
                / totalPixels;
    }

    /**
     * Resize image using standard Java AWT.
     */
    private BufferedImage resizeImage(
            BufferedImage original,
            int width,
            int height
    ) {

        BufferedImage resized =
                new BufferedImage(
                        width,
                        height,
                        BufferedImage.TYPE_INT_RGB
                );

        java.awt.Graphics2D graphics =
                resized.createGraphics();

        graphics.drawImage(
                original,
                0,
                0,
                width,
                height,
                null
        );

        graphics.dispose();

        return resized;
    }

    /**
     * Navigate to the next splash/onboarding screen.
     */
    private void navigateToNextSplashScreen() {

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

        waitForSeconds(2);
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