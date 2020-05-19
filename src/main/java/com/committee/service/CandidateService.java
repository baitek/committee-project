package com.committee.service;

import com.committee.model.Candidate;

import java.util.List;

public interface CandidateService {
    void addCandidate(Candidate candidate);
    List<Candidate> getAllCandidates();
    Candidate getCandidate(Long id);
    void deleteCandidate(Long id);
    void generateReport(String name, String surname, String pesel);
}