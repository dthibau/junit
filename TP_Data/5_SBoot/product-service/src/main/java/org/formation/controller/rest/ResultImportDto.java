package org.formation.controller.rest;

import java.util.ArrayList;
import java.util.List;


public class ResultImportDto {


	public int countProcessed=0;
	public int countUpdated=0;
	public int countErrors=0;
	public List<String> errorString = new ArrayList<>();
	public List<String> updateString = new ArrayList<>();
	
}
