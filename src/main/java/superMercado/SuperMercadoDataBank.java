package superMercado;

import java.sql.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SuperMercadoDataBank {
	private final static String dbUrl = "jdbc:postgresql://localhost:5432/super_mercado";
	private final static String dbUser = "dbuser";
	private final static String dbPassword = "dbuser";

	public static Connection createNewConnection() throws SQLException {
		//Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
	}

	public static ArrayList<Map.Entry<String, Integer>> queryProdutos() throws SQLException {
		ArrayList<Map.Entry<String, Integer>> produtos = new ArrayList<Map.Entry<String, Integer>>();
		Connection connection = createNewConnection();
		//Cria a consulta
		PreparedStatement st = connection.prepareStatement(
				"SELECT * FROM produto"
		);
		ResultSet resultSet = st.executeQuery();

		while(resultSet.next()){
			produtos.add(new AbstractMap.SimpleEntry<String, Integer>(
					resultSet.getString("id"),
					resultSet.getInt("preco"))
			);
		}
		st.close();
		resultSet.close();
		connection.close();

		return produtos;
	}

	public static ArrayList<Map.Entry<Promotion, String>> queryPromotions() throws SQLException {
		ArrayList<Map.Entry<Promotion, String>> promocoes = new ArrayList<Map.Entry<Promotion, String>>();
		Connection connection = createNewConnection();
		//Cria a consulta
		PreparedStatement st = connection.prepareStatement(
				"SELECT * FROM promocao"
		);
		ResultSet resultSet = st.executeQuery();

		while(resultSet.next()){
			double preco = resultSet.getInt("preco") / (double)resultSet.getInt("min_itens");

			Promotion prom = new Promotion(preco, resultSet.getInt("min_itens"));
			promocoes.add(new AbstractMap.SimpleEntry<Promotion, String>(
					prom, resultSet.getString("produto_id"))
			);
		}
		st.close();
		resultSet.close();
		connection.close();

		return promocoes;
	}

	public static void registerProduto(String id, Integer price) throws SQLException {
		Connection connection = createNewConnection();
		PreparedStatement st = connection.prepareStatement(
				"INSERT INTO produto(id, preco)\n" +
					"VALUES (?, ?)"
		);

		st.setString(1, id);
		st.setInt(2, price);

		//Executa o insert
		st.executeUpdate();
		st.close();
		connection.close();
	}

	public static void registerPromotion(Promotion promotion, String itemId) throws SQLException {
		Connection connection = createNewConnection();
		PreparedStatement st = connection.prepareStatement(
				"INSERT INTO promocao(preco, min_itens, produto_id)\n" +
					"VALUES (?, ?, ?)"
		);

		st.setInt(1, (int)Math.round(promotion.getUnitPrice()*promotion.getMinItens()));
		st.setInt(2, promotion.getMinItens());
		st.setString(3, itemId);

		//Executa o insert
		st.executeUpdate();
		st.close();
		connection.close();
	}



}
