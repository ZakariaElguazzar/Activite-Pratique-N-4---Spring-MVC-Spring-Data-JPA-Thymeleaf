package com.example.activity3.Web;

import com.example.activity3.Repositories.PatientRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class PatientController {
    private PatientRepository patientRepository;

    @GetMapping("/patients")
    public String index() {
        return "patients";
    }
}
