package framework;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class BaseTest {

    protected AndroidDriver driver;
    protected VideoRecorder videoRecorder;

    private static final ExtentReports EXTENT =
            ReportManager.getExtent();

    private ExtentTest extentTest;

    private static final String APP_PACKAGE =
            "com.insurance.bimasugam";

    /**
     * Start a fresh Appium driver before every test.
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp(ITestResult result) {

        String testName =
                result.getMethod().getMethodName();

        System.out.println();
        System.out.println("========================================");
        System.out.println("Starting Test: " + testName);
        System.out.println("========================================");

        try {

            /*
             * Start a fresh Appium driver.
             */
            if (!DriverManager.hasDriver()) {

                System.out.println(
                        "Starting fresh Android Appium Driver..."
                );

                DriverManager.startAndroidDriver();
            }

            driver = DriverManager.getDriver();

            /*
             * Create Extent test.
             */
            extentTest =
                    EXTENT.createTest(testName);

            extentTest.info(
                    "Test execution started."
            );

            /*
             * Start video recording.
             */
            videoRecorder =
                    new VideoRecorder();

            videoRecorder.start(testName);

            System.out.println(
                    "Video recording started for: "
                            + testName
            );

            System.out.println(
                    "Driver initialized successfully."
            );

        } catch (Exception e) {

            System.err.println(
                    "Failed during test setup: "
                            + e.getMessage()
            );

            throw e;
        }
    }

    /**
     * Common test step method.
     *
     * All test classes can use:
     *
     * step("Open application");
     * step("Verify splash screen");
     * step("Click Next");
     *
     * The step is added to both Allure and Extent reports.
     */
    protected void step(String description) {

        System.out.println(
                "[STEP] " + description
        );

        /*
         * Allure step.
         */
        try {

            Allure.step(description);

        } catch (Exception e) {

            System.out.println(
                    "Allure step could not be added: "
                            + e.getMessage()
            );
        }

        /*
         * Extent step.
         */
        try {

            if (extentTest != null) {

                extentTest.info(description);
            }

        } catch (Exception e) {

            System.out.println(
                    "Extent step could not be added: "
                            + e.getMessage()
            );
        }
    }

    /**
     * Cleanup after every test.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {

        String testName =
                result.getMethod().getMethodName();

        System.out.println();
        System.out.println("========================================");
        System.out.println(
                "Finishing Test: " + testName
        );
        System.out.println("========================================");

        /*
         * 1. Stop video recording and pull video.
         */
        stopVideoRecording(testName);

        /*
         * 2. Update Extent Report.
         */
        updateExtentReport(result);

        /*
         * 3. Capture screenshot if test failed.
         */
        if (result.getStatus()
                == ITestResult.FAILURE) {

            captureFailureScreenshot(testName);
        }

        /*
         * 4. Flush Extent Report.
         */
        try {

            EXTENT.flush();

        } catch (Exception e) {

            System.out.println(
                    "Extent flush failed: "
                            + e.getMessage()
            );
        }

        /*
         * 5. Force close Bima Sugam.
         */
        forceCloseApplication();

        /*
         * 6. Close Appium driver.
         */
        closeDriver();

        /*
         * 7. Clear references.
         */
        driver = null;
        videoRecorder = null;
        extentTest = null;

        System.out.println(
                "Test cleanup completed: "
                        + testName
        );

        System.out.println(
                "========================================"
        );
    }

    /**
     * Stops video recording and pulls MP4
     * from Android device.
     */
    private void stopVideoRecording(
            String testName) {

        try {

            if (videoRecorder != null) {

                System.out.println(
                        "Stopping video recording..."
                );

                String videoPath =
                        videoRecorder.stopAndPull(
                                testName
                        );

                if (videoPath != null) {

                    System.out.println(
                            "Video saved successfully: "
                                    + videoPath
                    );

                    if (extentTest != null) {

                        extentTest.info(
                                "Video recording saved: "
                                        + videoPath
                        );
                    }

                } else {

                    System.out.println(
                            "Video file was not created."
                    );
                }

            } else {

                System.out.println(
                        "Video recorder is not initialized."
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Could not stop/save video recording: "
                            + e.getMessage()
            );
        }
    }

    /**
     * Updates Extent Report.
     */
    private void updateExtentReport(
            ITestResult result) {

        try {

            if (extentTest == null) {
                return;
            }

            if (result.getStatus()
                    == ITestResult.SUCCESS) {

                extentTest.pass(
                        "Test Passed"
                );

            } else if (result.getStatus()
                    == ITestResult.FAILURE) {

                extentTest.fail(
                        "Test Failed"
                );

                if (result.getThrowable() != null) {

                    extentTest.fail(
                            result.getThrowable()
                    );
                }

            } else if (result.getStatus()
                    == ITestResult.SKIP) {

                extentTest.skip(
                        "Test Skipped"
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Extent report update failed: "
                            + e.getMessage()
            );
        }
    }

    /**
     * Force closes Bima Sugam.
     *
     * Uses:
     *
     * 1. Appium terminateApp()
     * 2. ADB force-stop
     * 3. pidof verification
     */
    private void forceCloseApplication() {

        System.out.println();
        System.out.println(
                "========== APPLICATION CLEANUP =========="
        );

        /*
         * STEP 1:
         * Appium terminateApp().
         */
        if (DriverManager.hasDriver()) {

            try {

                AndroidDriver androidDriver =
                        DriverManager.getDriver();

                System.out.println(
                        "Appium: terminating application..."
                );

                androidDriver.terminateApp(
                        APP_PACKAGE
                );

                System.out.println(
                        "Appium: application terminated."
                );

            } catch (Exception e) {

                System.out.println(
                        "Appium terminateApp failed: "
                                + e.getMessage()
                );
            }
        }

        /*
         * STEP 2:
         * ADB force-stop.
         */
        try {

            String udid =
                    getDeviceUdid();

            System.out.println(
                    "ADB: force stopping application..."
            );

            Process process =
                    new ProcessBuilder(
                            "adb",
                            "-s",
                            udid,
                            "shell",
                            "am",
                            "force-stop",
                            APP_PACKAGE
                    )
                            .redirectErrorStream(true)
                            .start();

            String output =
                    readProcessOutput(process);

            int exitCode =
                    process.waitFor();

            if (exitCode == 0) {

                System.out.println(
                        "ADB: application force-stopped successfully."
                );

            } else {

                System.out.println(
                        "ADB force-stop returned exit code: "
                                + exitCode
                );

                if (!output.isEmpty()) {

                    System.out.println(
                            "ADB output: "
                                    + output
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "ADB force-stop failed: "
                            + e.getMessage()
            );
        }

        /*
         * STEP 3:
         * Verify application process.
         */
        verifyApplicationClosed();

        System.out.println(
                "=========================================="
        );
    }

    /**
     * Gets configured device UDID.
     */
    private String getDeviceUdid() {

        String udid =
                System.getProperty("device.udid");

        if (udid != null
                && !udid.trim().isEmpty()) {

            return udid.trim();
        }

        udid =
                ConfigReader.get("device.udid");

        if (udid != null
                && !udid.trim().isEmpty()) {

            return udid.trim();
        }

        throw new RuntimeException(
                "device.udid is not configured."
        );
    }

    /**
     * Reads process output.
     */
    private String readProcessOutput(
            Process process) {

        StringBuilder output =
                new StringBuilder();

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(
                                     process.getInputStream()
                             )
                     )) {

            String line;

            while ((line =
                    reader.readLine()) != null) {

                output.append(line)
                        .append(System.lineSeparator());
            }

        } catch (Exception e) {

            System.out.println(
                    "Unable to read command output: "
                            + e.getMessage()
            );
        }

        return output.toString().trim();
    }

    /**
     * Verifies Bima Sugam is closed.
     */
    private void verifyApplicationClosed() {

        try {

            String udid =
                    getDeviceUdid();

            Process process =
                    new ProcessBuilder(
                            "adb",
                            "-s",
                            udid,
                            "shell",
                            "pidof",
                            APP_PACKAGE
                    )
                            .redirectErrorStream(true)
                            .start();

            String output =
                    readProcessOutput(process);

            int exitCode =
                    process.waitFor();

            if (output == null
                    || output.trim().isEmpty()
                    || exitCode != 0) {

                System.out.println(
                        "VERIFICATION: Bima Sugam is CLOSED."
                );

            } else {

                System.out.println(
                        "WARNING: Bima Sugam process is still running."
                );

                System.out.println(
                        "Process ID: "
                                + output
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Unable to verify application state: "
                            + e.getMessage()
            );
        }
    }

    /**
     * Closes Appium driver/session.
     */
    private void closeDriver() {

        try {

            if (DriverManager.hasDriver()) {

                System.out.println(
                        "Closing Appium driver..."
                );

                DriverManager.quitDriver();

                System.out.println(
                        "Appium driver closed successfully."
                );

            } else {

                System.out.println(
                        "No active Appium driver."
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Error closing Appium driver: "
                            + e.getMessage()
            );
        }
    }

    /**
     * Captures screenshot on failure.
     */
    private void captureFailureScreenshot(
            String testName) {

        if (!DriverManager.hasDriver()) {

            System.out.println(
                    "Driver unavailable for failure screenshot."
            );

            return;
        }

        try {

            AndroidDriver androidDriver =
                    DriverManager.getDriver();

            byte[] screenshot =
                    androidDriver.getScreenshotAs(
                            org.openqa.selenium.OutputType.BYTES
                    );

            /*
             * Allure attachment.
             */
            Allure.addAttachment(
                    testName
                            + " - Failure Screenshot",
                    "image/png",
                    new ByteArrayInputStream(
                            screenshot
                    ),
                    ".png"
            );

            /*
             * Extent screenshot.
             */
            if (extentTest != null) {

                String screenshotPath =
                        saveScreenshot(
                                testName,
                                screenshot
                        );

                if (screenshotPath != null) {

                    extentTest.addScreenCaptureFromPath(
                            screenshotPath
                    );
                }
            }

            System.out.println(
                    "Failure screenshot captured."
            );

        } catch (Exception e) {

            System.out.println(
                    "Could not capture failure screenshot: "
                            + e.getMessage()
            );
        }
    }

    /**
     * Saves screenshot.
     */
    private String saveScreenshot(
            String testName,
            byte[] screenshot) {

        try {

            Path directory =
                    Path.of(
                            "test-output",
                            "screenshots"
                    );

            Files.createDirectories(
                    directory
            );

            String safeTestName =
                    testName.replaceAll(
                            "[^a-zA-Z0-9._-]",
                            "_"
                    );

            Path screenshotPath =
                    directory.resolve(
                            safeTestName
                                    + "_failure.png"
                    );

            Files.write(
                    screenshotPath,
                    screenshot
            );

            return screenshotPath
                    .toAbsolutePath()
                    .toString();

        } catch (Exception e) {

            System.out.println(
                    "Unable to save screenshot: "
                            + e.getMessage()
            );

            return null;
        }
    }

    /**
     * Final safety cleanup after suite.
     */
    @AfterSuite(alwaysRun = true)
    public void afterSuite() {

        System.out.println();
        System.out.println(
                "========================================"
        );
        System.out.println(
                "Running final suite cleanup..."
        );
        System.out.println(
                "========================================"
        );

        /*
         * Force close application.
         */
        try {

            forceCloseApplication();

        } catch (Exception e) {

            System.out.println(
                    "Final application cleanup failed: "
                            + e.getMessage()
            );
        }

        /*
         * Close remaining driver.
         */
        try {

            if (DriverManager.hasDriver()) {

                DriverManager.quitDriver();
            }

        } catch (Exception e) {

            System.out.println(
                    "Final driver cleanup failed: "
                            + e.getMessage()
            );
        }

        /*
         * Flush Extent Report.
         */
        try {

            EXTENT.flush();

        } catch (Exception e) {

            System.out.println(
                    "Final Extent flush failed: "
                            + e.getMessage()
            );
        }

        System.out.println(
                "Final suite cleanup completed."
        );

        System.out.println(
                "Appium server remains running."
        );

        System.out.println(
                "ADB device remains connected."
        );

        System.out.println(
                "========================================"
        );
    }
}