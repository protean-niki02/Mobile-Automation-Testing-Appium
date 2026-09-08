package tests.sprint1;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class LoginPage {

    private final AppiumDriver driver;

    private final By loginButton =
            AppiumBy.accessibilityId("Login");

    private final By mobileNumber =
            By.xpath("//android.widget.EditText[@hint='Enter Mobile Number']");

    private final By dateOfBirth =
            By.xpath("//android.widget.EditText[@hint='Date of Birth']");

    private final By loginViaOtp =
            AppiumBy.accessibilityId("Login via OTP");

    public LoginPage(AppiumDriver driver) {
        this.driver = driver;
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    public void enterMobileNumber(String number) {
        driver.findElement(mobileNumber).click();
        driver.findElement(mobileNumber).sendKeys(number);
    }

    public void enterDateOfBirth(String dob) {
        driver.findElement(dateOfBirth).click();
        driver.findElement(dateOfBirth).sendKeys(dob);
    }

    public void clickLoginViaOtp() {
        driver.findElement(loginViaOtp).click();
    }
}