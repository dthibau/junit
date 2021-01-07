package org.formation.controller.rest;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class ResultImportDto {


	@ApiModelProperty(value = "Nombre de lignes traitées",example="1")
	public int countProcessed=0;
	@ApiModelProperty(value = "Nombre de lignes ayant provoqué une mise à jour dans la base",example="1")
	public int countUpdated=0;
	@ApiModelProperty(value = "Nombre de lignes n'ayant pas pu être traitées",example="1")
	public int countErrors=0;
	@ApiModelProperty(value = "Détail pour chaque ligne en erreur")
	public List<String> errorString = new ArrayList<>();
	@ApiModelProperty(value = "Détail pour chaque produit mise à jour : Valeur avant/Valeur après")
	public List<String> updateString = new ArrayList<>();
	
}
