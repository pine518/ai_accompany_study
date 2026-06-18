package com.mdframe.forge.eduai.model;

import java.util.List;

public record CompanionSummary(
        String studentId,
        String studentName,
        List<String> todaySuggestions,
        List<String> weakSubjects,
        String weeklySummary,
        CompanionSettings settings
) {
}
