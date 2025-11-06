package org.formation.model;



import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Dimension {

	private float hauteur,longueur,largeur;

	@Override
	public String toString() {
	
		return largeur+" x "+longueur+" x "+hauteur;
	}
	
	
}
