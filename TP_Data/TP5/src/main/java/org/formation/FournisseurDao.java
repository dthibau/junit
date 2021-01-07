package org.formation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FournisseurDao {

	public void createFournisseur(String nom, String reference) throws SQLException {
		Connection conn = DBUtil.geConnection();
		
		PreparedStatement ps = conn.prepareStatement("insert into fournisseur (nom,reference) values (?, ? )");
		ps.setString(1, nom);
		ps.setString(2, reference);
		ps.executeUpdate();
		
		ps.close();
		conn.close();
	}
}
