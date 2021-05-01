package org.coeg.routine.backend;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesStorage {
    private SharedPreferences preferences;
    private String sharedPreferencesFile;

    private final String USER_KEY = "user-id";
    private final String USER_FULLNAME = "user-fullname";
    private final String USER_PROFILE_PICTURE_PATH = "profpic-path";
    private final String COUNTER_ONTIME = "counter-ontime";
    private final String COUNTER_LATE = "counter-late";
    private final String OPT_TELEMETRY = "enable-telemetry";
    private final String ENABLE_PUSH_NOTIFICATIONS = "enable-push";
    private final String ENABLE_PREREMINDER = "enable-pre-reminder";

    private int userId;
    private String userFullName;
    private String profilePicturePath;
    private int onTimeCounter;
    private int lateCounter;
    private boolean enableTelemetry;
    private boolean enablePushNotifications;
    private boolean enablePreReminder;

    static boolean loaded = false;

    private static PreferencesStorage instance;
    public static PreferencesStorage getInstance() {
        if(instance == null) instance = new PreferencesStorage();
        return instance;
    }

    private PreferencesStorage() {}

    // run in onSaveInstanceState(s)
    public void savePreferences() throws IllegalStateException {
        if(!loaded) throw new IllegalStateException("Preferences has not been loaded.");

        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putInt(USER_KEY, userId);
        preferencesEditor.putString(USER_FULLNAME, userFullName);
        preferencesEditor.putString(USER_PROFILE_PICTURE_PATH, profilePicturePath);
        preferencesEditor.putInt(COUNTER_ONTIME, onTimeCounter);
        preferencesEditor.putInt(COUNTER_LATE, lateCounter);
        preferencesEditor.putBoolean(OPT_TELEMETRY, enableTelemetry);
        preferencesEditor.putBoolean(ENABLE_PUSH_NOTIFICATIONS, enablePushNotifications);
        preferencesEditor.putBoolean(ENABLE_PREREMINDER, enablePreReminder);
        preferencesEditor.apply();
    }

    // run on startup (splash) or fragments
    public void loadPreferences(Context context) {
        if(!loaded) getInstance();

        sharedPreferencesFile = context.getPackageName();
        preferences = context.getSharedPreferences(sharedPreferencesFile, Context.MODE_PRIVATE);

        userId = preferences.getInt(USER_KEY, -1); // -1 if not yet created
        userFullName = preferences.getString(USER_FULLNAME, "");
        profilePicturePath = preferences.getString(USER_FULLNAME, ""); // TODO: Add found path for profile picture here
        // profilePicturePath = preferences.getString(USER_PROFILE_PICTURE_PATH, "/data/data/" + sharedPreferencesFile + "/" + userId + ".jpg");
        onTimeCounter = preferences.getInt(COUNTER_ONTIME, 0);
        lateCounter = preferences.getInt(COUNTER_LATE, 0);
        enableTelemetry = preferences.getBoolean(OPT_TELEMETRY, true);
        enablePushNotifications = preferences.getBoolean(ENABLE_PUSH_NOTIFICATIONS, true);
        enablePreReminder = preferences.getBoolean(ENABLE_PREREMINDER, true);

        loaded = true;
    }

    public int getUserId() { return userId; }
    public String getFullName() { return userFullName; }

    /**
     * Get image path stored in internal storage
     * @return profile picture path
     * @deprecated Use internal storage class instead
     */
    @Deprecated
    public String getProfilePicturePath() { return profilePicturePath; }
    public int getOnTimeCounter() { return onTimeCounter; }
    public int getLateCounter() { return lateCounter; }
    public boolean isTelemetryEnabled() { return enableTelemetry; }
    public boolean isPushNotificationsEnabled() { return enablePushNotifications; }
    public boolean isPreReminderEnabled() { return enablePreReminder; }

    // only set private variables when new profile is created
    public void setUserId(int id) { userId = id; } // TODO: if telemetry is about to be uploaded to server, keep track of this ID.
    public void setFullName(String fullName) { userFullName = fullName; }
    public void incrementOnTimeCounter() { onTimeCounter++; }
    public void incrementLateCounter() { lateCounter++; }
    public void setEnableTelemetry(boolean target) { enableTelemetry = target; }
    public void setEnablePushNotifications(boolean target) { enablePushNotifications = target; }
    public void setEnablePreReminder(boolean target) { enablePreReminder = target; }

    public static int getRandomUserId(int min, int max) {
        return (int)((Math.random() * (max - min)) + min);
    }
}
