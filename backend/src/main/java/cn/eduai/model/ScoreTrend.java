package cn.eduai.model;

import java.util.List;
import java.util.Map;

public record ScoreTrend(String studentId, Map<String, List<ScoreRecord>> subjects) {
}
