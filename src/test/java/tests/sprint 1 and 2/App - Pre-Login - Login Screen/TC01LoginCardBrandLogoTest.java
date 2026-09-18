package tests.sprint1;

import framework.BaseTest;
import framework.WaitUtils;
import io.appium.java_client.AppiumBy;
import locators.AndroidLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.time.Duration;
import java.util.Map;

public class TC01LoginCardBrandLogoTest extends BaseTest {

    private static final String APP_PACKAGE = "com.insurance.bimasugam";
    private static final By WELCOME_HEADING =
            AppiumBy.accessibilityId("Welcome to Bima Sugam");

    @Test(
            description = "BD2M-672 | SC_01_TC_001 - Verify that the "
                    + "login card displays the brand logo.",
            groups = {"Sprint1", "Login", "Positive"}
    )
    public void verifyLoginCardDisplaysBrandLogo() {
        if (waitForInitialScreen()) {
            for (int screen = 1; screen <= 4; screen++) {
                swipeUp();
                waitForSeconds(2);
            }

            driver.executeScript(
                    "mobile: clickGesture",
                    Map.of("x", 540, "y", 1920)
            );
        }

        WaitUtils.visible(driver, AndroidLocators.OTP_HINT);

        Assert.assertEquals(
                driver.getCurrentPackage(),
                APP_PACKAGE,
                "Login screen does not belong to the Bima Sugam app."
        );
        Assert.assertTrue(
                hasBrandLogo(),
                "Bima Sugam brand logo is not visible in the expected "
                        + "login-screen region."
        );
    }

    private boolean waitForInitialScreen() {
        new WebDriverWait(driver, Duration.ofSeconds(20)).until(
                ignored -> !driver.findElements(WELCOME_HEADING).isEmpty()
                        || !driver.findElements(AndroidLocators.OTP_HINT).isEmpty()
        );
        return !driver.findElements(WELCOME_HEADING).isEmpty();
    }

    private void swipeUp() {
        driver.executeScript(
                "mobile: swipeGesture",
                Map.of(
                        "left", 100, "top", 200,
                        "width", 880, "height", 2100,
                        "direction", "up", "percent", 0.70
                )
        );
    }

    private void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private boolean hasBrandLogo() {
        try {
            byte[] screenshotBytes = driver.getScreenshotAs(OutputType.BYTES);
            BufferedImage screenshot = ImageIO.read(
                    new ByteArrayInputStream(screenshotBytes)
            );

            if (screenshot == null) {
                return false;
            }

            int navyPixelCount = 0;
            int left = screenshot.getWidth() / 8;
            int right = screenshot.getWidth() * 7 / 8;
            int top = screenshot.getHeight() * 16 / 100;
            int bottom = screenshot.getHeight() * 26 / 100;

            for (int y = top; y < bottom; y++) {
                for (int x = left; x < right; x++) {
                    int rgb = screenshot.getRGB(x, y);
                    int red = (rgb >> 16) & 0xFF;
                    int green = (rgb >> 8) & 0xFF;
                    int blue = rgb & 0xFF;

                    if (red < 90 && green < 100
                            && blue >= 60 && blue <= 180) {
                        navyPixelCount++;
                    }
                }
            }

            return navyPixelCount >= 500;
        } catch (Exception ignored) {
            return false;
        }
    }
}
