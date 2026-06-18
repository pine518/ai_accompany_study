package com.mdframe.forge.eduai.controller;

import com.mdframe.forge.eduai.model.CompanionSettings;
import com.mdframe.forge.eduai.model.CompanionSummary;
import com.mdframe.forge.eduai.model.Student;
import com.mdframe.forge.eduai.service.MvpDataStore;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai/companion")
public class CompanionController {
    private final MvpDataStore dataStore;

    public CompanionController(MvpDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @GetMapping("/summary")
    public CompanionSummary summary(@RequestParam String studentId) {
        Student student = dataStore.student(studentId).orElseThrow();
        return new CompanionSummary(
                student.id(),
                student.name(),
                List.of("数学已达成 100 分，建议保持错题复盘节奏。", "英语稳步提升，本周建议补强阅读理解。"),
                List.of("英语阅读", "物理受力分析"),
                "本周学习状态稳定，数学进步明显；建议下周保持数学复盘，同时增加英语阅读训练。",
                dataStore.settings(studentId)
        );
    }

    @GetMapping("/push-settings")
    public CompanionSettings getSettings(@RequestParam String studentId) {
        return dataStore.settings(studentId);
    }

    @PutMapping("/push-settings")
    public CompanionSettings updateSettings(@RequestBody CompanionSettings settings) {
        return dataStore.updateSettings(settings);
    }
}
