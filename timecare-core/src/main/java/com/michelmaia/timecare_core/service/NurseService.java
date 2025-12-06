package com.michelmaia.timecare_core.service;

import com.michelmaia.timecare_core.model.Nurse;
import com.michelmaia.timecare_core.repository.NurseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NurseService {

    private final NurseRepository nurseRepository;

    public NurseService(NurseRepository nurseRepository) {
        this.nurseRepository = nurseRepository;
    }

    public List<Nurse> getAllNurses() {
        return nurseRepository.findAll();
    }

    public Optional<Nurse> getNurseById(Long id) {
        return nurseRepository.findById(id);
    }

    public Nurse create(Nurse nurse) {
        return nurseRepository.save(nurse);
    }
}
