package org.formation.controller.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Data;

@Data
public class UpdateLineDto {
	private final static Logger log = LoggerFactory.getLogger(UpdateLineDto.class);
	
	private final String SEPARATOR = ";";
	
	
	private String referenceProduit;
	private String referenceFournisseur;
	private int quantite;
	private boolean valid;
	
	public UpdateLineDto(String line) {
		try {
			String[] fields = line.split(SEPARATOR);
			setReferenceFournisseur(fields[0]);
			setReferenceProduit(fields[1]);
			setQuantite(Integer.parseInt(fields[2]));
			valid = true;
		} catch (Exception ex) {
			log.error(ex.toString());
			valid = false;
		}
		log.info("Update LineDto built : " + this);
	}
}
