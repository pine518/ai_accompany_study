package com.mdframe.forge.eduai.controller;

import com.mdframe.forge.eduai.model.ScoreComment;
import com.mdframe.forge.eduai.service.MvpDataStore;
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
