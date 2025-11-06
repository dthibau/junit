package org.formation.controller.rest;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.formation.model.Fournisseur;
import org.formation.model.FournisseurRepository;
import org.formation.model.Produit;
import org.formation.model.ProduitRepository;
import org.formation.service.ImportProduitService;
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
@RequestMapping("/api/produits")
public class ProduitRestController {

	@Autowired
	ProduitRepository produitRepository;

	@Autowired
	FournisseurRepository fournisseurRepository;

	@Autowired
	ImportProduitService importProduitService;

	@GetMapping
	List<Produit> findAll() {
		return produitRepository.findAll();
	}

	@GetMapping("/fournisseur/{fournisseurId}")
	List<Produit> findByFournisseur(@PathVariable("fournisseurId") long fournisseurId) {

		Fournisseur fournisseur = fournisseurRepository.findById(fournisseurId)
				.orElseThrow(() -> new EntityNotFoundException("Fournisseur inconnu :" + fournisseurId));
		return produitRepository.findByFournisseur(fournisseur);
	}

	@PostMapping
	ResponseEntity<Produit> createProduct(@Valid @RequestBody Produit produit) {
		
		produit = produitRepository.save(produit);
		
		return new ResponseEntity<Produit>(produit,HttpStatus.CREATED);
		
	}
	
	@PostMapping("/addCSV")
	ResponseEntity<ResultImportDto> addProducts(@RequestBody String csvFile) {

		return new ResponseEntity<>(importProduitService.importLines(csvFile), HttpStatus.ACCEPTED);

	}

}
