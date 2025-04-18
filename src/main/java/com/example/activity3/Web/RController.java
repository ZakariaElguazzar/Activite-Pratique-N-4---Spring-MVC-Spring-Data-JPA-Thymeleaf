package com.example.activity3.Web;

import com.example.activity3.Entities.Patient;
import com.example.activity3.Repositories.PatientRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RController {
    @Autowired
    private PatientRepository patientRepository;

    @GetMapping("/csrf")
    public CsrfToken getcsrf(HttpServletRequest httpServletRequest) {
        return (CsrfToken) httpServletRequest.getAttribute("_csrf");
    }
    @PostMapping("/addPatient")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Patient addPatient(@RequestBody Patient patient) {
        return patientRepository.save(patient);
    }
    @GetMapping("/allPatient")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
}
