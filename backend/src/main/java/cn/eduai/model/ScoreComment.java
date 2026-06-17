package cn.eduai.model;

import java.time.Instant;

public record ScoreComment(
        String id,
        String studentId,
        String subject,
        String authorRole,
        String authorName,
        String content,
        Instant createdAt
) {
}
