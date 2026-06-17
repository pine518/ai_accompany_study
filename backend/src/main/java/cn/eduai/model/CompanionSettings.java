package cn.eduai.model;

public record CompanionSettings(
        String studentId,
        boolean pushEnabled,
        String pushRule,
        int memoryRetentionDays,
        int maxMemoryRetentionDays
) {
}
