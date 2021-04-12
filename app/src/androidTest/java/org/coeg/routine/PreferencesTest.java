package org.coeg.routine;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.coeg.routine.backend.PreferencesStorage;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class PreferencesTest {
    @Test
    public void testPreferences() {
        PreferencesStorage preferences = PreferencesStorage.getInstance();
        preferences.loadPreferences(ApplicationProvider.getApplicationContext());

        preferences.setUserId(PreferencesStorage.getRandomUserId(1, 100000));
        preferences.setFullName("Sunaokami Shiroko");
        preferences.setEnableTelemetry(false);
        preferences.incrementOnTimeCounter();
        preferences.incrementOnTimeCounter();
        preferences.incrementOnTimeCounter();
        preferences.incrementLateCounter();
        preferences.incrementLateCounter();

        assertEquals("Sunaokami Shiroko", preferences.getFullName());
        assertEquals(3, preferences.getOnTimeCounter());
        assertEquals(2, preferences.getLateCounter());
        assertEquals(true, preferences.isPushNotificationsEnabled());
        assertEquals(true, preferences.isPreReminderEnabled());
    }
}