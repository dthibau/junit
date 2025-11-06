package org.formation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import jakarta.persistence.EntityNotFoundException;
import org.formation.controller.rest.ResultImportDto;
import org.formation.model.Produit;
import org.formation.model.ProduitRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ImportProduitTest {

	@Autowired
	ImportProduitService importProduitService;
	
	@Autowired
	ProduitRepository produitRepository;

	@Test
	public void testSample() throws IOException, URISyntaxException {
		Path path = Paths.get(getClass().getClassLoader().getResource("sample.csv").toURI());

		Stream<String> lines = Files.lines(path);
		String csvFile = lines.collect(Collectors.joining("\n"));
		lines.close();

		ResultImportDto result = importProduitService.importLines(csvFile);

		assertEquals(5, result.countProcessed);
		assertEquals(3, result.countErrors);
		
		Produit p = produitRepository.findByReferenceAndFournisseur_Reference("TAN78","BELLE").orElseThrow(
				() -> new EntityNotFoundException() );

		assertEquals(14, p.getAvailability());
	}
}
