package tests.sprint1;

import framework.BaseTest;
import framework.WaitUtils;
import locators.AndroidLocators;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Map;

public class TC05ValidMobileNumberTest extends BaseTest {

    private static final String VALID_MOBILE_NUMBER = "8237300176";

    @Test(
            description = "BD2M-672 | SC_01_TC_005 - Verify that a valid "
                    + "10-digit Indian mobile number is accepted.",
            groups = {"Sprint1", "Login", "Positive"}
    )
    public void verifyValidMobileNumberIsAccepted() {
        step("Wait for the initial Bima Sugam onboarding screen");

        if (waitForInitialScreen()) {
            step("Navigate through onboarding to the Login screen");
            for (int screen = 1; screen <= 4; screen++) {
                swipeUp();
            }

            step("Open the Login screen");
            driver.executeScript(
                    "mobile: clickGesture",
                    Map.of("x", 540, "y", 1920)
            );
        }

        step("Enter a valid 10-digit Indian mobile number");
        WaitUtils.visible(driver, AndroidLocators.MOBILE_NUMBER_INPUT);
        var mobileNumber = driver.findElement(
                AndroidLocators.MOBILE_NUMBER_INPUT
        );
        mobileNumber.clear();
        mobileNumber.sendKeys(VALID_MOBILE_NUMBER);

        step("Verify that the mobile number was accepted");
        Assert.assertEquals(
                mobileNumber.getAttribute("text").trim(),
                VALID_MOBILE_NUMBER,
                "The valid mobile number was not retained in the field."
        );
        Assert.assertTrue(
                mobileNumber.getAttribute("text").matches("\\d{10}"),
                "Mobile Number should contain exactly 10 digits."
        );
    }

    private boolean waitForInitialScreen() {
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ignored ->
                        !driver.findElements(AndroidLocators.WELCOME_HEADING)
                                .isEmpty()
                                || !driver.findElements(AndroidLocators.LOGIN_HEADING)
                                .isEmpty()
                                || !driver.findElements(AndroidLocators.OTP_HINT)
                                .isEmpty()
        );
        return !driver.findElements(AndroidLocators.WELCOME_HEADING).isEmpty();
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
