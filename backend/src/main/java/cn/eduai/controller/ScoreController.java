package cn.eduai.controller;

import cn.eduai.model.ScoreRecord;
import cn.eduai.model.ScoreTrend;
import cn.eduai.service.MvpDataStore;
import jakarta.validation.Valid;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/scores")
public class ScoreController {
    private final MvpDataStore dataStore;

    public ScoreController(MvpDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @GetMapping("/trends")
    public ScoreTrend trends(@RequestParam String studentId) {
        Map<String, List<ScoreRecord>> grouped = dataStore.scores(studentId)
                .stream()
                .collect(Collectors.groupingBy(ScoreRecord::subject));
        return new ScoreTrend(studentId, grouped);
    }

    @PostMapping("/manual")
    public ScoreRecord manual(@Valid @RequestBody ScoreRecord score) {
        return dataStore.addScore(score);
    }

    @GetMapping("/import-template")
    public ResponseEntity<String> template() throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/score_import_template.csv");
        String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=score_import_template.csv")
                .body(content);
    }
}
