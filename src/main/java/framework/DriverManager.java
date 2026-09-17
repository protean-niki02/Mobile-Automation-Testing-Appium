package framework;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public final class DriverManager {

    private DriverManager() {
        // Utility class
    }

    private static final ThreadLocal<AndroidDriver> DRIVER =
            new ThreadLocal<>();

    /**
     * Starts a new Android Appium driver.
     */
    public static void startAndroidDriver() {

        /*
         * Prevent duplicate driver/session on the same thread.
         */
        if (hasDriver()) {

            System.out.println(
                    "Existing Appium driver found. "
                            + "Closing it before starting a new one."
            );

            quitDriver();
        }

        String udid = resolveDeviceUdid();
        String appPath = resolveAppPath();

        String appiumServer =
                ConfigReader.get("appium.server");

        System.out.println("========================================");
        System.out.println("Starting Android Appium Driver");
        System.out.println("Device UDID   : " + udid);
        System.out.println("Appium Server : " + appiumServer);
        System.out.println("App Path      : " + appPath);
        System.out.println("========================================");

        try {

            UiAutomator2Options options =
                    new UiAutomator2Options();

            options.setPlatformName("Android");

            options.setAutomationName("UiAutomator2");

            /*
             * Get device name from config.
             * If not present, use Android Device.
             */
            String deviceName =
                    ConfigReader.get("device.name");

            if (deviceName == null
                    || deviceName.trim().isEmpty()) {

                deviceName = "Android Device";
            }

            options.setDeviceName(deviceName);

            /*
             * Device UDID.
             */
            options.setUdid(udid);

            /*
             * Application APK.
             */
            options.setApp(appPath);

            /*
             * Appium command timeout.
             */
            options.setNewCommandTimeout(
                    Duration.ofSeconds(120)
            );

            /*
             * Optional Android platform version.
             */
            String platformVersion =
                    ConfigReader.get("platform.version");

            if (platformVersion != null
                    && !platformVersion.trim().isEmpty()) {

                options.setPlatformVersion(
                        platformVersion
                );
            }

            /*
             * Create Android Appium driver.
             */
            AndroidDriver driver =
                    new AndroidDriver(
                            URI.create(appiumServer).toURL(),
                            options
                    );

            /*
             * Store driver in ThreadLocal.
             */
            DRIVER.set(driver);

            System.out.println(
                    "Android Appium Driver started successfully."
            );

            System.out.println(
                    "Session ID: "
                            + driver.getSessionId()
            );

            System.out.println(
                    "========================================"
            );

        } catch (Exception e) {

            /*
             * Make sure ThreadLocal is clean if
             * driver creation fails.
             */
            DRIVER.remove();

            System.err.println(
                    "Failed to start Android Appium Driver."
            );

            System.err.println(
                    "Reason: " + e.getMessage()
            );

            throw new RuntimeException(
                    "Unable to start Android Appium Driver.",
                    e
            );
        }
    }

    /**
     * Returns the current thread's AndroidDriver.
     */
    public static AndroidDriver getDriver() {

        AndroidDriver driver =
                DRIVER.get();

        if (driver == null) {

            throw new IllegalStateException(
                    "Driver is not initialized."
            );
        }

        return driver;
    }

    /**
     * Checks whether an Appium driver exists
     * for the current thread.
     */
    public static boolean hasDriver() {

        return DRIVER.get() != null;
    }

    /**
     * Quits the current Appium driver/session.
     *
     * This does NOT disconnect the physical
     * Android device from ADB.
     *
     * This does NOT stop the Appium server.
     */
    public static void quitDriver() {

        AndroidDriver driver =
                DRIVER.get();

        try {

            if (driver != null) {

                System.out.println(
                        "Closing Android Appium Driver..."
                );

                try {

                    driver.quit();

                    System.out.println(
                            "Android Appium Driver "
                                    + "closed successfully."
                    );

                } catch (Exception e) {

                    System.out.println(
                            "Android Appium Driver was "
                                    + "already closed or could "
                                    + "not be closed: "
                                    + e.getMessage()
                    );
                }

            } else {

                System.out.println(
                        "No Appium driver exists "
                                + "for this thread."
                );
            }

        } finally {

            /*
             * Very important for TestNG.
             * Removes the driver from ThreadLocal
             * so the next test can create a fresh session.
             */
            DRIVER.remove();

            System.out.println(
                    "Driver reference removed from ThreadLocal."
            );
        }
    }

    /**
     * Resolves Android device UDID.
     *
     * Priority:
     *
     * 1. JVM system property
     * 2. config.properties
     * 3. Automatically detected ADB device
     */
    private static String resolveDeviceUdid() {

        /*
         * 1. JVM system property.
         *
         * Example:
         * -Ddevice.udid=963861480000066
         */
        String udid =
                System.getProperty("device.udid");

        if (udid != null
                && !udid.trim().isEmpty()) {

            validateDevice(udid.trim());

            return udid.trim();
        }

        /*
         * 2. config.properties.
         */
        udid =
                ConfigReader.get("device.udid");

        if (udid != null
                && !udid.trim().isEmpty()) {

            validateDevice(udid.trim());

            return udid.trim();
        }

        /*
         * 3. Automatically detect connected device.
         */
        List<String> devices =
                getConnectedDevices();

        if (devices.isEmpty()) {

            throw new RuntimeException(
                    "No Android device/emulator is connected. "
                            + "Run 'adb devices' and verify "
                            + "the device."
            );
        }

        if (devices.size() > 1) {

            System.out.println(
                    "Multiple Android devices detected:"
            );

            for (String device : devices) {

                System.out.println(
                        " - " + device
                );
            }

            System.out.println(
                    "Using first available device: "
                            + devices.get(0)
            );
        }

        return devices.get(0);
    }

    /**
     * Gets connected Android devices from ADB.
     */
    private static List<String> getConnectedDevices() {

        List<String> devices =
                new ArrayList<>();

        try {

            Process process =
                    new ProcessBuilder(
                            "adb",
                            "devices"
                    )
                            .redirectErrorStream(true)
                            .start();

            try (BufferedReader reader =
                         new BufferedReader(
                                 new InputStreamReader(
                                         process.getInputStream()
                                 )
                         )) {

                String line;

                while ((line =
                        reader.readLine()) != null) {

                    line = line.trim();

                    if (line.isEmpty()
                            || line.startsWith(
                                    "List of devices")) {

                        continue;
                    }

                    String[] parts =
                            line.split("\\s+");

                    if (parts.length >= 2
                            && "device".equals(parts[1])) {

                        devices.add(parts[0]);
                    }
                }
            }

            process.waitFor();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to detect Android devices "
                            + "using ADB.",
                    e
            );
        }

        return devices;
    }

    /**
     * Resolves APK path.
     *
     * Priority:
     *
     * 1. app.path from config.properties
     * 2. apps/InsuranceApp.apk
     */
    private static String resolveAppPath() {

        /*
         * 1. Configured APK path.
         */
        String configuredPath =
                ConfigReader.get("app.path");

        if (configuredPath != null
                && !configuredPath.trim().isEmpty()) {

            Path path =
                    Path.of(
                            configuredPath.trim()
                    );

            if (Files.exists(path)) {

                return path
                        .toAbsolutePath()
                        .toString();
            }

            System.out.println(
                    "Configured app path does not exist: "
                            + configuredPath
            );
        }

        /*
         * 2. Default project APK location.
         */
        Path workspaceApp =
                Path.of(
                        System.getProperty("user.dir"),
                        "apps",
                        "InsuranceApp.apk"
                );

        if (Files.exists(workspaceApp)) {

            return workspaceApp
                    .toAbsolutePath()
                    .toString();
        }

        throw new RuntimeException(
                "APK not found.\n"
                        + "Checked configured path: "
                        + configuredPath
                        + "\n"
                        + "Checked workspace path: "
                        + workspaceApp.toAbsolutePath()
        );
    }

    /**
     * Validates that the configured device
     * is connected and in device state.
     */
    private static void validateDevice(
            String udid) {

        try {

            Process process =
                    new ProcessBuilder(
                            "adb",
                            "-s",
                            udid,
                            "get-state"
                    )
                            .redirectErrorStream(true)
                            .start();

            String output;

            try (BufferedReader reader =
                         new BufferedReader(
                                 new InputStreamReader(
                                         process.getInputStream()
                                 )
                         )) {

                output = reader.readLine();
            }

            process.waitFor();

            if (output == null
                    || !"device".equalsIgnoreCase(
                            output.trim())) {

                throw new RuntimeException(
                        "Android device '"
                                + udid
                                + "' is not in 'device' state. "
                                + "Run 'adb devices' to check."
                );
            }

        } catch (Exception e) {

            if (e instanceof RuntimeException) {

                throw (RuntimeException) e;
            }

            throw new RuntimeException(
                    "Unable to validate Android device: "
                            + udid,
                    e
            );
        }
    }
}