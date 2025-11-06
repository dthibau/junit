package org.formation.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import jakarta.persistence.EntityNotFoundException;
import org.formation.controller.rest.ResultImportDto;
import org.formation.controller.rest.UpdateLineDto;
import org.formation.model.FournisseurRepository;
import org.formation.model.Produit;
import org.formation.model.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImportProduitService {

	@Autowired
	ProduitRepository produitRepository;
	
	@Autowired
	FournisseurRepository fournisseurRepository;
	
	
	public ResultImportDto importLines(List<UpdateLineDto> updateLines) {
		
		ResultImportDto result = new ResultImportDto();
		
		for (UpdateLineDto updateLine : updateLines ) {
			result.countProcessed++;
			if ( !updateLine.isValid() ) {
				result.countErrors++;
				result.errorString.add("Ligne non Valide " + result.countProcessed + " : " + updateLine);
				continue;
			}
			try {
				Produit produit = produitRepository.findByReferenceAndFournisseur_Reference(updateLine.getReferenceProduit(), updateLine.getReferenceFournisseur()).orElseThrow(() -> new EntityNotFoundException());
				produit.setAvailability(produit.getAvailability() + updateLine.getQuantite());
				produitRepository.save(produit);
				result.countUpdated++;
				result.updateString.add("Ajout de " + updateLine.getQuantite() + " : Produit "+updateLine.getReferenceProduit() + " du fournisseur " + updateLine.getReferenceFournisseur());
			} catch (EntityNotFoundException e) {
				result.countErrors++;
				result.errorString.add("Ligne non Valide " + result.countProcessed + " : " + updateLine);
				continue;
			}
			
		}
		
		return result;
		
	}
	public ResultImportDto importLines(String csvFile) {
		return importLines(_parseCSV(csvFile));
	}
	private List<UpdateLineDto> _parseCSV(String csvFile) {

		return Stream.of(csvFile.split("\n")).map(line -> new UpdateLineDto(line)).collect(Collectors.toList());
	}
}
