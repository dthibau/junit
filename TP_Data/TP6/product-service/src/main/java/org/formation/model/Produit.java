package org.formation.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class Produit {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String reference;
	
	private String nom;
	
	private String description;
	
	private Float prixUnitaire;
	
	private Integer availability;
	
	@Embedded
	private Dimension dimension;
	
	@ManyToOne
	private Fournisseur fournisseur;
	
}
