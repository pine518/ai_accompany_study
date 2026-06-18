package com.mdframe.forge.eduai.controller;

import com.mdframe.forge.eduai.model.RewardRule;
import com.mdframe.forge.eduai.service.MvpDataStore;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {
    private final MvpDataStore dataStore;

    public RewardController(MvpDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @GetMapping
    public List<RewardRule> list(@RequestParam String studentId) {
        return dataStore.rewards(studentId);
    }

    @PostMapping
    public RewardRule create(@RequestBody RewardRule rewardRule) {
        return dataStore.addReward(rewardRule);
    }
}
