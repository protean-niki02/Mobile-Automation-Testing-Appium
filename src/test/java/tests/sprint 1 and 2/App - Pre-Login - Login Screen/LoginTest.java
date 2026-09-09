package tests.sprint1;

import framework.BaseTest;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(description = "BD2M-672 - TC01 - Valid Login")
    public void TC01_validLogin() {

        LoginPage loginPage = new LoginPage(driver);

        step("Tap Login");
        loginPage.clickLogin();

        step("Enter valid mobile number");
        loginPage.enterMobileNumber("8921639271");

        step("Enter date of birth");
        loginPage.enterDateOfBirth("01/01/1999");

        step("Tap Login via OTP");
        loginPage.clickLoginViaOtp();

        step("Capture OTP screen");

        System.out.println("========== OTP SCREEN ==========");
        System.out.println(driver.getPageSource());
        System.out.println("========== END OTP SCREEN ==========");
    }
}