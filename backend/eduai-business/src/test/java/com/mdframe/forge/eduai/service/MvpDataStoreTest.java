package com.mdframe.forge.eduai.service;

import com.mdframe.forge.eduai.model.CompanionSettings;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MvpDataStoreTest {

    @Test
    void companionSettingsUseSafeDefaults() {
        MvpDataStore store = new MvpDataStore();

        CompanionSettings settings = store.settings("new-student");

        assertThat(settings.pushEnabled()).isFalse();
        assertThat(settings.memoryRetentionDays()).isEqualTo(14);
        assertThat(settings.maxMemoryRetentionDays()).isEqualTo(90);
    }

    @Test
    void memoryRetentionCannotExceedNinetyDays() {
        MvpDataStore store = new MvpDataStore();
        CompanionSettings input = new CompanionSettings(
                "stu-001",
                true,
                "每周日晚提醒",
                120,
                90
        );

        CompanionSettings saved = store.updateSettings(input);

        assertThat(saved.memoryRetentionDays()).isEqualTo(90);
        assertThat(saved.maxMemoryRetentionDays()).isEqualTo(90);
    }
}
