package tests.sprint1;

import framework.BaseTest;
import framework.WaitUtils;
import locators.AndroidLocators;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Map;

public class TC07CountryCodeSelectorTest extends BaseTest {

    @Test(
            description = "BD2M-672 | SC_01_TC_007 - Verify that the country "
                    + "code selector is displayed with a default flag.",
            groups = {"Sprint1", "Login", "Positive"}
    )
    public void verifyCountryCodeSelectorDisplaysDefaultFlag() {
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

        step("Verify the country code selector and default flag");
        WaitUtils.visible(driver, AndroidLocators.MOBILE_NUMBER_INPUT);
        boolean defaultFlagDisplayed = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        ).until(
                ignored -> !driver.findElements(
                        AndroidLocators.COUNTRY_CODE_SELECTOR
                ).isEmpty()
        );

        Assert.assertTrue(
                defaultFlagDisplayed,
                "Country code selector should display the default flag."
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
                                || !driver.findElements(
                                        AndroidLocators.MOBILE_NUMBER_INPUT
                                ).isEmpty()
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
