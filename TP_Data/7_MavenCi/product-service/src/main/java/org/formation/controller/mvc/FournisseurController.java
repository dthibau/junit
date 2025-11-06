package org.formation.controller.mvc;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.formation.model.Fournisseur;
import org.formation.model.FournisseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/fournisseurs")
public class FournisseurController {

	@Autowired
	FournisseurRepository fournisseurRepository;
	
	@GetMapping
	public String getAll(Model model) {
		model.addAttribute("fournisseurs", fournisseurRepository.findAll());
		return "fournisseurs";
	}
	
	@GetMapping("/edit")
	public String editFournisseur(@RequestParam("fournisseurId") long fournisseurId, Model model) {
		model.addAttribute("fournisseur", fournisseurRepository.findById(fournisseurId).orElseThrow(() -> new EntityNotFoundException()));
		return "fournisseurEdit";
	}
	
	@PostMapping(path = "/save",  consumes="application/x-www-form-urlencoded;charset=UTF-8")
	public String save(@Valid Fournisseur fournisseur, Model model) {

		fournisseur = fournisseurRepository.save(fournisseur);
		return "redirect:/fournisseurs";
		
	}
}
