package cn.eduai.model;

import java.time.Instant;

public record RewardRule(
        String id,
        String studentId,
        String subject,
        String rewardType,
        String rewardLevel,
        String conditionText,
        String rewardText,
        boolean fulfilled,
        Instant createdAt
) {
}
