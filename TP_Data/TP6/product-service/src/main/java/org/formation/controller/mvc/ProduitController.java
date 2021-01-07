package org.formation.controller.mvc;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.formation.model.Fournisseur;
import org.formation.model.FournisseurRepository;
import org.formation.model.Produit;
import org.formation.model.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/produits")
public class ProduitController {

	@Autowired
	ProduitRepository produitRepository;
	
	@Autowired
	FournisseurRepository fournisseurRepository;
	
	@RequestMapping
	public String findProduits(@RequestParam(required = false, value = "fournisseurId") Long fournisseurId, Model model) {
		
		
		model.addAttribute("fournisseurs",fournisseurRepository.findAll());
		
		if ( fournisseurId != null ) {
			Fournisseur fournisseur = fournisseurRepository.findById(fournisseurId).orElseThrow(() -> new EntityNotFoundException("Fournisseur inconnu"));
			model.addAttribute("produits", produitRepository.findByFournisseur(fournisseur));
			model.addAttribute("selectedFournisseur", fournisseurId);
		} else {
			model.addAttribute("produits", produitRepository.findAll());
		} 
	
		return "produits";
	}
	@GetMapping("/edit")
	public String editProduit(@RequestParam("produitId") long produitId, Model model) {
		model.addAttribute("produit", produitRepository.findById(produitId).orElseThrow(() -> new EntityNotFoundException()));
		return "produitEdit";
	}
	
	@PostMapping(path = "/save",  consumes="application/x-www-form-urlencoded;charset=UTF-8")
	public String save(@Valid Produit produit, Model model) {
		
		Produit oldProduit = produitRepository.findById(produit.getId()).orElseThrow(() -> new EntityNotFoundException("No such product"));

		produit.setFournisseur(oldProduit.getFournisseur());
		produit.setAvailability(oldProduit.getAvailability());
		produit = produitRepository.save(produit);
		return "redirect:/produits";
		
	}
}
