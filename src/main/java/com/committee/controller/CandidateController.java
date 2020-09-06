package com.committee.controller;

import com.committee.model.Candidate;
import com.committee.service.CandidateService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @GetMapping("/candidates")
    public List<Candidate> getAllCandidates() {
        return candidateService.getAllCandidates();
    }

    @GetMapping("/candidates/{id}")
    public Candidate getCandidate(@PathVariable Long id) {
        return candidateService.getCandidate(id);
    }

    @PostMapping("/candidates")
    public void addCandidate(@RequestBody Candidate candidate) {
        candidateService.addCandidate(candidate);
    }

    @DeleteMapping("/candidates/{id}")
    public void deleteCandidate(@PathVariable Long id) {
        candidateService.deleteCandidate(id);
    }

    @PutMapping("/candidates/{id}")
    public void updateCandidate(@RequestBody Candidate newCandidate, @PathVariable Long id) {
        try {
            Candidate candidate = candidateService.getCandidate(id);
            candidate.setName(newCandidate.getName());
            candidate.setSurname(newCandidate.getSurname());
            candidate.setPesel(newCandidate.getPesel());
            candidate.setPolish(newCandidate.getPolish());
            candidate.setMath(newCandidate.getMath());
            candidate.setEnglish(newCandidate.getEnglish());
            candidate.setEmail(newCandidate.getEmail());
            candidateService.addCandidate(candidate);
        } catch (NoSuchElementException e) {
            newCandidate.setId(id);
            candidateService.addCandidate(newCandidate);
        }
    }

    @GetMapping(
            value = "/candidates/pdf/all",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public @ResponseBody byte[] getAllPdf() throws IOException {
        InputStream in = getClass()
                .getClassLoader()
                .getResourceAsStream("static/raport-zbiorczy.pdf");
        return IOUtils.toByteArray(in);
    }

    @GetMapping(
            value = "/candidates/pdf/{id}",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public @ResponseBody byte[] getOnePdf(@PathVariable Long id) throws IOException {
        Candidate candidate = candidateService.getCandidate(id);
        String fileName = (candidate.getName() + "-" + candidate.getSurname()).toLowerCase();

        InputStream in = getClass()
                .getClassLoader()
                .getResourceAsStream("static/" + fileName + ".pdf");
        return IOUtils.toByteArray(in);
    }

}
