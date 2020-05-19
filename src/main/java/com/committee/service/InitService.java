package com.committee.service;

import com.committee.model.Candidate;
import com.committee.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class InitService {

    @Autowired
    private CandidateRepository candidateRepository;

    @PostConstruct
    public void InitService() {
        Candidate c1 = new Candidate(0l, "Maciej", "Kowalski", "123456789", 100, 100, 100);
        Candidate c2 = new Candidate(0l, "Andrzej", "Nowak", "987654321", 80, 70, 80);
        Candidate c3 = new Candidate(0l, "Kasia", "Mrotek", "456123789", 30, 46, 32);

        c1 = candidateRepository.save(c1);
        c2 = candidateRepository.save(c2);
        c3 = candidateRepository.save(c3);
    }

}
