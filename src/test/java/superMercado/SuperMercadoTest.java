package superMercado;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class SuperMercadoTest {
	SuperMercado superMercado = new SuperMercado();
	MapPriceControler controler = new MapPriceControler();
	Caixa checkout = new Caixa(controler);

	@Before
	public void init() throws SQLException {
		checkout.clear();
		controler = new MapPriceControler();
		checkout.setPriceControler(controler);

		Connection connection = superMercado.mercadoDataBank.createNewConnection();
		PreparedStatement st = connection.prepareStatement(
				"DELETE FROM promocao"
		);
		st.execute();

		st = connection.prepareStatement(
				"DELETE FROM produto"
		);
		st.execute();

		st.close();
		connection.close();

		superMercado = new SuperMercado();
		controler = new MapPriceControler();
		checkout = new Caixa(controler);
	}

	private int getDbQuantidadePromocao() throws SQLException {
		Connection connection = superMercado.mercadoDataBank.createNewConnection();
		PreparedStatement st = connection.prepareStatement(
				"SELECT count(*) AS quantidade FROM promocao"
		);
		ResultSet resultSet = st.executeQuery();
		Assert.assertTrue(resultSet.next());
		int quantidade = resultSet.getInt("quantidade");

		resultSet.close();
		st.close();
		connection.close();
		return quantidade;
	}

	private int getDbQuantidadeProduto() throws SQLException {
		Connection connection = superMercado.mercadoDataBank.createNewConnection();
		PreparedStatement st = connection.prepareStatement(
				"SELECT count(*) AS quantidade FROM produto"
		);
		ResultSet resultSet = st.executeQuery();
		Assert.assertTrue(resultSet.next());
		int quantidade = resultSet.getInt("quantidade");

		resultSet.close();
		st.close();
		connection.close();
		return quantidade;
	}


	private void setConfiguration1() {
		//preços padrões dos produtos padrões.
		controler.registerProduct("A", 50);
		controler.registerProduct("B", 30);
		controler.registerProduct("C", 20);
		controler.registerProduct("D", 15);


		controler.registerPromotion("A", 130, 3);    //3 por 130
		controler.registerPromotion("B", 45, 2);    //2 por 45
		controler.registerPromotion("C", 2 * 20.0, 3);    //leve 3 pague 2
	}

	private void setConfiguration2() {
		controler.registerProduct("A", 50);
		controler.registerProduct("B", 30);
		controler.registerProduct("C", 20);
		controler.registerProduct("D", 15);

		controler.registerPromotion("A", 130, 3); //3 por 130
		controler.registerPromotion("A", 100, 3); //leve 3 pague 2
		controler.registerPromotion("B", 45, 2); //2 por 45
		controler.registerPromotion("C", 25, 2);    //2 por 25
		controler.registerPromotion("C", 40, 3);    //3 por 40
	}

	@Test
	public void caixaTest1() {
		setConfiguration1();
		//teste 1
		checkout.addItem("A");
		assertEquals(50, checkout.getTotalPrice());
		assertEquals(0, checkout.getTotalDiscount());
		checkout.addItem("A");
		assertEquals(100, checkout.getTotalPrice());
		assertEquals(0, checkout.getTotalDiscount());
		checkout.addItem("A");
		assertEquals(130, checkout.getTotalPrice());
		assertEquals(20, checkout.getTotalDiscount());
		checkout.addItem("A");
		assertEquals(180, checkout.getTotalPrice());
		assertEquals(20, checkout.getTotalDiscount());
		checkout.addItem("A");
		assertEquals(230, checkout.getTotalPrice());
		assertEquals(20, checkout.getTotalDiscount());
		checkout.addItem("A");
		assertEquals(260, checkout.getTotalPrice());
		assertEquals(40, checkout.getTotalDiscount());
		checkout.removeItem("A");
		assertEquals(230, checkout.getTotalPrice());
		assertEquals(20, checkout.getTotalDiscount());
	}

	@Test
	public void caixaTest2() {
		setConfiguration1();
		//teste 2
		checkout.addItem("D");
		assertEquals(15, checkout.getTotalPrice());
		assertEquals(0, checkout.getTotalDiscount());
		checkout.addItem("A");
		assertEquals(65, checkout.getTotalPrice());
		assertEquals(0, checkout.getTotalDiscount());
		checkout.addItem("B");
		assertEquals(95, checkout.getTotalPrice());
		assertEquals(0, checkout.getTotalDiscount());
		checkout.addItem("A");
		assertEquals(145, checkout.getTotalPrice());
		assertEquals(0, checkout.getTotalDiscount());
		checkout.addItem("B");
		assertEquals(160, checkout.getTotalPrice());
		assertEquals(15, checkout.getTotalDiscount());
		checkout.addItem("A");
		assertEquals(190, checkout.getTotalPrice());
		assertEquals(35, checkout.getTotalDiscount());
		checkout.removeItem("A");
		assertEquals(160, checkout.getTotalPrice());
		assertEquals(15, checkout.getTotalDiscount());
		checkout.removeItem("B");
		assertEquals(145, checkout.getTotalPrice());
		assertEquals(0, checkout.getTotalDiscount());
	}

	@Test
	public void caixaTest3() {
		setConfiguration1();
		//teste 3
		checkout.addItem("C");
		assertEquals(20, checkout.getTotalPrice());
		assertEquals(0, checkout.getTotalDiscount());
		checkout.addItem("C");
		assertEquals(40, checkout.getTotalPrice());
		assertEquals(0, checkout.getTotalDiscount());
		checkout.addItem("C");
		assertEquals(40, checkout.getTotalPrice());
		assertEquals(20, checkout.getTotalDiscount());
		checkout.addItem("C");
		assertEquals(60, checkout.getTotalPrice());
		assertEquals(20, checkout.getTotalDiscount());
		checkout.removeItem("C");
		assertEquals(40, checkout.getTotalPrice());
		assertEquals(20, checkout.getTotalDiscount());
		checkout.removeItem("C");
		assertEquals(40, checkout.getTotalPrice());
		assertEquals(0, checkout.getTotalDiscount());
	}

	@Test
	public void caixaTest4() {
		setConfiguration1();
		//teste 4
		checkout.addItem("C");
		assertEquals(20, checkout.getTotalPrice());
		assertEquals(0, checkout.getTotalDiscount());
		checkout.addItem("B");
		assertEquals(50, checkout.getTotalPrice());
		assertEquals(0, checkout.getTotalDiscount());
		checkout.addItem("B");
		assertEquals(65, checkout.getTotalPrice());
		assertEquals(15, checkout.getTotalDiscount());
		checkout.removeItem("B");
		assertEquals(50, checkout.getTotalPrice());
		assertEquals(0, checkout.getTotalDiscount());
	}

	@Test
	public void multiplePromotionsTest1() {
		setConfiguration2();

		checkout.addItem("A");
		assertEquals(50, checkout.getTotalPrice());
		assertEquals(0, checkout.getTotalDiscount());
		checkout.addItem("A");
		assertEquals(100, checkout.getTotalPrice());
		assertEquals(0, checkout.getTotalDiscount());
		checkout.addItem("A");
		assertEquals(100, checkout.getTotalPrice());
		assertEquals(50, checkout.getTotalDiscount());
		checkout.removeItem("A");
		assertEquals(100, checkout.getTotalPrice());
		assertEquals(0, checkout.getTotalDiscount());

	}

	@Test
	public void multiplePromotionsTest2() {
		setConfiguration2();

		checkout.addItem("B");
		assertEquals(30, checkout.getTotalPrice());
		assertEquals(0, checkout.getTotalDiscount());
		checkout.addItem("B");
		assertEquals(45, checkout.getTotalPrice());
		assertEquals(15, checkout.getTotalDiscount());
		checkout.addItem("B");
		assertEquals(75, checkout.getTotalPrice());
		assertEquals(15, checkout.getTotalDiscount());
		checkout.addItem("C");
		assertEquals(95, checkout.getTotalPrice());
		assertEquals(15, checkout.getTotalDiscount());
		checkout.addItem("C");
		assertEquals(100, checkout.getTotalPrice());
		assertEquals(30, checkout.getTotalDiscount());
		checkout.addItem("C");
		assertEquals(115, checkout.getTotalPrice());
		assertEquals(35, checkout.getTotalDiscount());

	}

	@Test
	public void dataBankTest1() throws SQLException {
		superMercado.registerProduct("A", 50);
		superMercado.registerProduct("B", 30);
		superMercado.registerProduct("C", 20);
		superMercado.registerProduct("D", 15);

		assertEquals(4, getDbQuantidadeProduto());

		superMercado.registerPromotion("A", 130, 3);    //3 por 130
		superMercado.registerPromotion("B", 45, 2);    //2 por 45
		superMercado.registerPromotion("C", 2 * 20.0, 3);        //leve 3 pague 2

		assertEquals(3, getDbQuantidadePromocao());
	}

	@Test
	public void dataBankTest2() throws SQLException {
		superMercado.registerProduct("A", 50);
		superMercado.registerProduct("B", 30);
		superMercado.registerProduct("C", 20);
		superMercado.registerProduct("D", 15);
		superMercado.registerProduct("E", 15);

		assertEquals(5, getDbQuantidadeProduto());

		superMercado.registerPromotion("A", 130, 3); //3 por 130
		superMercado.registerPromotion("A", 100, 3); //leve 3 pague 2
		superMercado.registerPromotion("B", 45, 2); //2 por 45
		superMercado.registerPromotion("C", 25, 2);    //2 por 25
		superMercado.registerPromotion("C", 40, 3);    //3 por 40

		assertEquals(5, getDbQuantidadePromocao());


		SuperMercado superMercado2 = new SuperMercado();

		superMercado2.addCaixa();
		Caixa caixa = superMercado2.getCaixa(0);

		caixa.addItem("B");
		assertEquals(30, caixa.getTotalPrice());
		assertEquals(0, caixa.getTotalDiscount());
		caixa.addItem("B");
		assertEquals(45, caixa.getTotalPrice());
		assertEquals(15, caixa.getTotalDiscount());
		caixa.addItem("B");
		assertEquals(75, caixa.getTotalPrice());
		assertEquals(15, caixa.getTotalDiscount());
		caixa.addItem("C");
		assertEquals(95, caixa.getTotalPrice());
		assertEquals(15, caixa.getTotalDiscount());
		caixa.addItem("C");
		assertEquals(100, caixa.getTotalPrice());
		assertEquals(30, caixa.getTotalDiscount());
		caixa.addItem("C");
		assertEquals(115, caixa.getTotalPrice());
		assertEquals(35, caixa.getTotalDiscount());
	}
}