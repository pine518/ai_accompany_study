package com.mdframe.forge.eduai.model;

import java.time.LocalDate;

public record ScoreRecord(
        String id,
        String studentId,
        String subject,
        String examName,
        LocalDate examDate,
        double score,
        double fullScore,
        String examType,
        String remark
) {
}
