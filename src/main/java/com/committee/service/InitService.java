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
        Candidate c1 = new Candidate(0l, "Maciej", "Kowalski", "12345678921", 100, 100, 100, "maciej@o2.pl");
        Candidate c2 = new Candidate(0l, "Andrzej", "Nowak", "98765432134", 80, 70, 80, "maciej@o2.pl");
        Candidate c3 = new Candidate(0l, "Kasia", "Mrotek", "45612378954", 30, 46, 32, "maciej@o2.pl");
        Candidate c4 = new Candidate(0l, "Szczepan", "Mamrotek", "54678378954", 70, 46, 58, "labda@o2.pl");

        c1 = candidateRepository.save(c1);
        c2 = candidateRepository.save(c2);
        c3 = candidateRepository.save(c3);
        c4 = candidateRepository.save(c4);
    }

}
