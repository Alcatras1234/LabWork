package org.example.serverjava.Services;

import org.example.serverjava.entities.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.serverjava.repository.ActionRepository;

import java.util.List;

@Service
public class ActionService {

    @Autowired
    private ActionRepository actionRepository;


    public List<Action> getAllActions() {
        return actionRepository.findAll();
    }

    public int saveAction(Action action) {
        return actionRepository.save(action);
    }
}
