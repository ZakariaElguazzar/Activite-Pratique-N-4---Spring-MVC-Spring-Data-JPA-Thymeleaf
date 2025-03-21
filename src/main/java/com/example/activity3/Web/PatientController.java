package com.example.activity3.Web;

import com.example.activity3.Entities.Patient;
import com.example.activity3.Repositories.PatientRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@AllArgsConstructor
public class PatientController {
    private PatientRepository patientRepository;

    @GetMapping("/patients")
    public String index(Model model,
                        @RequestParam(name = "page",defaultValue = "0") int page,
                        @RequestParam(name = "size",defaultValue = "4") int size,
                        @RequestParam(name = "keyword",defaultValue = "") String kw) {
        Page<Patient> pagePatients = patientRepository.findByNomContains(kw,PageRequest.of(page, size));
        model.addAttribute("listPatients", pagePatients.getContent());
        model.addAttribute("pages", new int[pagePatients.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", kw);
        return "Patients";
    }
    @GetMapping("/delete")
    public String delete(@RequestParam(name = "id") Long id,
                         @RequestParam(name = "page",defaultValue = "0") int page,
                         @RequestParam(name = "keyword",defaultValue = "") String kw) {
        patientRepository.deleteById(id);
        return "redirect:/patients?page="+page+"&keyword="+kw;
    }
    @GetMapping("/")
    public String home() {
        return "redirect:/patients";
    }
    @GetMapping("/formPatients")
    public String formPatients(Model model) {
        model.addAttribute("patient", new Patient());
        return "formPatients";
    }
    @GetMapping("/editPatient")
    public String editPatient(@RequestParam(name = "id") Long id,Model model,@RequestParam(name = "keyword")String keyword,@RequestParam(name = "page")int page) {
        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient == null) throw new RuntimeException("Patient not found");
        model.addAttribute("patient", patient);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        return "editPatient";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute @Valid Patient Patient, BindingResult bindingResult, Model model,@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "") String keyword) {
        if(bindingResult.hasErrors()) return "formPatients";
        patientRepository.save(Patient);
        return "redirect:/patients?page="+page+"&keyword="+keyword;
    }
}
