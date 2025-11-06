package org.formation.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

	public List<Produit> findByFournisseur(Fournisseur fournisseur);
	
	Optional<Produit> findByReferenceAndFournisseur_Reference(String produitReference, String fournisseurReference);

}
