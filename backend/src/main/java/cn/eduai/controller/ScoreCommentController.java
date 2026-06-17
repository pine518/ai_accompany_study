package cn.eduai.controller;

import cn.eduai.model.ScoreComment;
import cn.eduai.service.MvpDataStore;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/score-comments")
public class ScoreCommentController {
    private final MvpDataStore dataStore;

    public ScoreCommentController(MvpDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @GetMapping
    public List<ScoreComment> list(@RequestParam String studentId) {
        return dataStore.comments(studentId);
    }

    @PostMapping
    public ScoreComment create(@RequestBody ScoreComment comment) {
        return dataStore.addComment(comment);
    }
}
