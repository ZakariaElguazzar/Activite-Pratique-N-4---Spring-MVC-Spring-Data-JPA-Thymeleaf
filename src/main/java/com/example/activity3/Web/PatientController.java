package com.example.activity3.Web;

import com.example.activity3.Entities.Patient;
import com.example.activity3.Repositories.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class PatientController {
    private PatientRepository patientRepository;

    @GetMapping("/patients")
    public String index(Model model) {
        List<Patient> patientsList = patientRepository.findAll();
        model.addAttribute("listPatients", patientsList);
        return "Patients";
    }
}
