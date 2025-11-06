package org.formation.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {

	Optional<Fournisseur> findByReference(String reference);
	
	
}
