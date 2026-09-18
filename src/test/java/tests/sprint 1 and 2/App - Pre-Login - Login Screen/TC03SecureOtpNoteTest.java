package tests.sprint1;

import framework.BaseTest;
import framework.WaitUtils;
import locators.AndroidLocators;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Map;

public class TC03SecureOtpNoteTest extends BaseTest {

    @Test(
            description = "BD2M-672 | SC_01_TC_003 - Verify that the login "
                    + "card displays the note "
                    + "\"Your details will be verified by a secure OTP\".",
            groups = {"Sprint1", "Login", "Positive"}
    )
    public void verifyLoginCardDisplaysSecureOtpNote() {
        step("Wait for the initial Bima Sugam onboarding screen");

        if (waitForInitialScreen()) {
            step("Navigate through onboarding to the Login screen");

            for (int screen = 1; screen <= 4; screen++) {
                swipeUp();
            }

            step("Open the Login screen");
            WaitUtils.clickable(driver, AndroidLocators.LOGIN);
            driver.findElement(AndroidLocators.LOGIN).click();
        } else {
            step("Login screen is already displayed");
        }

        step("Verify the secure OTP note on the login card");
        WaitUtils.visible(driver, AndroidLocators.OTP_HINT);

        Assert.assertTrue(
                driver.findElement(AndroidLocators.OTP_HINT).isDisplayed(),
                "Login card should display the note "
                        + "\"Your details will be verified by a secure OTP\"."
        );
    }

    private boolean waitForInitialScreen() {
        new WebDriverWait(driver, Duration.ofSeconds(20)).until(
                ignored ->
                        !driver.findElements(AndroidLocators.WELCOME_HEADING)
                                .isEmpty()
                                || !driver.findElements(AndroidLocators.OTP_HINT)
                                .isEmpty()
        );

        boolean onboardingDisplayed =
                !driver.findElements(AndroidLocators.WELCOME_HEADING).isEmpty();

        if (onboardingDisplayed) {
            System.out.println("Initial onboarding screen is displayed.");
        } else {
            System.out.println("Login screen is already displayed.");
        }

        return onboardingDisplayed;
    }

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
}
