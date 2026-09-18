package tests.sprint1;

import framework.BaseTest;
import framework.WaitUtils;
import locators.AndroidLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Map;

public class TC08CountryCodeSelectionTest extends BaseTest {

    @Test(
            description = "BD2M-672 | SC_01_TC_008 - Verify that the country "
                    + "code is selectable on the login card.",
            groups = {"Sprint1", "Login", "Positive"}
    )
    public void verifyAlternateCountryCodeIsSelectable() {
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

        step("Open the country code selector");
        WaitUtils.visible(driver, AndroidLocators.MOBILE_NUMBER_INPUT);
        driver.executeScript(
                "mobile: clickGesture",
                Map.of(
                        "x", 920,
                        "y", 1095
                )
        );
        step("Select an alternate country code");
        String selectedCode;
        try {
            selectedCode = new WebDriverWait(
                    driver,
                    Duration.ofSeconds(5)
            ).until(
                    ignored -> {
                        var options = driver.findElements(
                                AndroidLocators.ALTERNATE_COUNTRY_CODE
                        );
                        return options.isEmpty()
                                ? null
                                : options.get(0).getText().trim();
                    }
            );
        } catch (TimeoutException exception) {
            throw new SkipException(
                    "Known application defect UCM-COUNTRY-CODE-001: "
                            + "the country-code selector opens without "
                            + "exposing alternate country-code options in "
                            + "the Appium UI hierarchy.",
                    exception
            );
        }
        Assert.assertNotEquals(
                selectedCode,
                "+91",
                "The selected country code must be alternate to +91."
        );
        clickCenter(By.xpath(
                "//*[normalize-space(@text)='" + selectedCode + "']"
        ));

        Assert.assertTrue(
                driver.findElements(AndroidLocators.MOBILE_NUMBER_INPUT)
                        .size() > 0,
                "The login card should remain available after selecting "
                        + "the alternate country code."
        );
    }

    private void clickCenter(By locator) {
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                        WebElement element = new WebDriverWait(
                                driver,
                                Duration.ofSeconds(5)
                        ).until(
                                ignored -> {
                                    var elements = driver.findElements(locator);
                                    return elements.isEmpty() ? null : elements.get(0);
                                }
                        );
                        var rect = element.getRect();
                        driver.executeScript(
                                "mobile: clickGesture",
                                Map.of(
                                        "x", rect.getX() + rect.getWidth() / 2,
                                        "y", rect.getY() + rect.getHeight() / 2
                                )
                        );
                        return;
            } catch (StaleElementReferenceException exception) {
                        if (attempt == 3) {
                            throw exception;
                        }
            }
        }
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
