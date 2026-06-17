package cn.eduai.service;

import cn.eduai.model.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MvpDataStore {
    private final List<Subject> subjects = new ArrayList<>();
    private final Map<String, Student> students = new ConcurrentHashMap<>();
    private final List<ScoreRecord> scores = Collections.synchronizedList(new ArrayList<>());
    private final List<ScoreComment> comments = Collections.synchronizedList(new ArrayList<>());
    private final List<RewardRule> rewards = Collections.synchronizedList(new ArrayList<>());
    private final Map<String, CompanionSettings> settings = new ConcurrentHashMap<>();

    public MvpDataStore() {
        subjects.addAll(List.of(
                new Subject("math", "主修学科", "数学", true),
                new Subject("physics", "主修学科", "物理", true),
                new Subject("chemistry", "主修学科", "化学", true),
                new Subject("english", "主修学科", "英语", true),
                new Subject("computer", "非学科", "计算机应用", false),
                new Subject("java", "非学科", "Java 开发", false),
                new Subject("python", "非学科", "Python 开发", false)
        ));

        Student student = new Student("stu-001", "S20260001", "张三", "高一", "高一1班");
        students.put(student.id(), student);

        scores.add(new ScoreRecord(UUID.randomUUID().toString(), student.id(), "数学", "第一次月考", LocalDate.of(2026, 9, 30), 92, 100, "月考", ""));
        scores.add(new ScoreRecord(UUID.randomUUID().toString(), student.id(), "数学", "期中考试", LocalDate.of(2026, 11, 15), 100, 100, "期中", ""));
        scores.add(new ScoreRecord(UUID.randomUUID().toString(), student.id(), "英语", "第一次月考", LocalDate.of(2026, 9, 30), 86, 100, "月考", ""));
        scores.add(new ScoreRecord(UUID.randomUUID().toString(), student.id(), "英语", "期中考试", LocalDate.of(2026, 11, 15), 91, 100, "期中", ""));

        settings.put(student.id(), new CompanionSettings(student.id(), false, "每周日晚 19:00 生成周计划提醒", 14, 90));
    }

    public List<Subject> subjects() {
        return List.copyOf(subjects);
    }

    public Subject addSubject(Subject input) {
        Subject subject = new Subject(
                input.id() == null || input.id().isBlank() ? UUID.randomUUID().toString() : input.id(),
                input.category(),
                input.name(),
                input.core()
        );
        subjects.add(subject);
        return subject;
    }

    public List<Student> students() {
        return List.copyOf(students.values());
    }

    public Optional<Student> student(String id) {
        return Optional.ofNullable(students.get(id));
    }

    public CompanionSettings settings(String studentId) {
        return settings.computeIfAbsent(studentId, id -> new CompanionSettings(id, false, "未设置", 14, 90));
    }

    public CompanionSettings updateSettings(CompanionSettings input) {
        int retentionDays = Math.max(1, Math.min(input.memoryRetentionDays(), 90));
        CompanionSettings normalized = new CompanionSettings(
                input.studentId(),
                input.pushEnabled(),
                input.pushRule(),
                retentionDays,
                90
        );
        settings.put(input.studentId(), normalized);
        return normalized;
    }

    public List<ScoreRecord> scores(String studentId) {
        synchronized (scores) {
            return scores.stream().filter(score -> score.studentId().equals(studentId)).toList();
        }
    }

    public ScoreRecord addScore(ScoreRecord input) {
        ScoreRecord score = new ScoreRecord(
                UUID.randomUUID().toString(),
                input.studentId(),
                input.subject(),
                input.examName(),
                input.examDate(),
                input.score(),
                input.fullScore(),
                input.examType(),
                input.remark()
        );
        scores.add(score);
        return score;
    }

    public ScoreComment addComment(ScoreComment input) {
        ScoreComment comment = new ScoreComment(
                UUID.randomUUID().toString(),
                input.studentId(),
                input.subject(),
                input.authorRole(),
                input.authorName(),
                input.content(),
                Instant.now()
        );
        comments.add(comment);
        return comment;
    }

    public List<ScoreComment> comments(String studentId) {
        synchronized (comments) {
            return comments.stream().filter(comment -> comment.studentId().equals(studentId)).toList();
        }
    }

    public RewardRule addReward(RewardRule input) {
        RewardRule reward = new RewardRule(
                UUID.randomUUID().toString(),
                input.studentId(),
                input.subject(),
                input.rewardType(),
                input.rewardLevel(),
                input.conditionText(),
                input.rewardText(),
                false,
                Instant.now()
        );
        rewards.add(reward);
        return reward;
    }

    public List<RewardRule> rewards(String studentId) {
        synchronized (rewards) {
            return rewards.stream().filter(reward -> reward.studentId().equals(studentId)).toList();
        }
    }
}
