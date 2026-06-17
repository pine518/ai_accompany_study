package cn.eduai.controller;

import cn.eduai.model.Subject;
import cn.eduai.service.MvpDataStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {
    private final MvpDataStore dataStore;

    public SubjectController(MvpDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @GetMapping
    public List<Subject> list() {
        return dataStore.subjects();
    }

    @PostMapping
    public Subject create(@RequestBody Subject subject) {
        return dataStore.addSubject(subject);
    }
}
