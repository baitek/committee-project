package com.committee.service;

import com.committee.model.Candidate;
import com.committee.repository.CandidateRepository;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    private final String url = "jdbc:postgresql://localhost:5432/committeedb";
    private final String user = "postgres";
    private final String password = "admin";

    public void addCandidate(Candidate candidate) {
        candidateRepository.save(candidate);
    }

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public Candidate getCandidate(Long id) {
        return candidateRepository.findById(id).get();
    }

    public void deleteCandidate(Long id) {
        candidateRepository.deleteById(id);
    }

    public void oneCandidateReport(String name, String surname, Long id) {
        String reportPath = "src/main/resources/static/candidateReport.jrxml";
        String fileName = name + "." + surname;
        String destPath = "src/main/resources/static/" + fileName + ".pdf";
        File file = new File(destPath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace(); // TODO HANDLE THE EXCEPTION
        }

        try (Connection conn = connect()) {
            Map<String, Object> map = new HashMap<>();
            map.put("A", id);
            JasperReport jr = JasperCompileManager.compileReport(reportPath);
            JasperPrint jp = JasperFillManager.fillReport(jr, map, conn);
            JasperExportManager.exportReportToPdfFile(jp, destPath);
        } catch (Exception ex) {
            ex.printStackTrace(); // TODO HANDLE THE EXCEPTION
        }
    }

    public void allCandidatesReport() {
        String reportPath = "src/main/resources/static/allCandidatesReport.jrxml";
        String fileName = "Raport.zbiorczy";
        String destPath = "src/main/resources/static/" + fileName + ".pdf";
        File file = new File(destPath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace(); // TODO HANDLE THE EXCEPTION
        }

        try (Connection conn = connect()) {
            Map<String, Object> map = new HashMap<>();
            JasperReport jr = JasperCompileManager.compileReport(reportPath);
            JasperPrint jp = JasperFillManager.fillReport(jr, map, conn);
            JasperExportManager.exportReportToPdfFile(jp, destPath);
        } catch (Exception ex) {
            ex.printStackTrace(); // TODO HANDLE THE EXCEPTION
        }
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

}
