package org.formation.controller.rest;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.formation.model.Fournisseur;
import org.formation.model.FournisseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fournisseurs")
public class FournisseurRestController {

	@Autowired
	FournisseurRepository fournisseurRepository;
	
	@GetMapping("{reference}")
	public Fournisseur findByReference(@PathVariable("fournisseur") String reference) {
		return fournisseurRepository.findByReference(reference).orElseThrow(() -> new EntityNotFoundException("Fournissseur inconnu :"+reference));
	}
	
	@PostMapping
	ResponseEntity<Fournisseur> createProduct(@Valid @RequestBody Fournisseur fournisseur) {
		
		fournisseur = fournisseurRepository.save(fournisseur);
		
		return new ResponseEntity<Fournisseur>(fournisseur,HttpStatus.CREATED);
		
	}
}
