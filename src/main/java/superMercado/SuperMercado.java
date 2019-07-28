package superMercado;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

import static superMercado.SuperMercadoDataBank.queryProdutos;

public class SuperMercado {

	SuperMercadoDataBank mercadoDataBank;

	private ArrayList<Caixa> caixas = new ArrayList<Caixa>();
	private MapPriceControler gerenciadorDePrecos = new MapPriceControler();

	public SuperMercado() {
		try {
			for(Map.Entry<String, Integer> entry: mercadoDataBank.queryProdutos()){
				gerenciadorDePrecos.registerProduct(entry.getKey(), entry.getValue());
			}
			for(Map.Entry<Promotion, String> entry: mercadoDataBank.queryPromotions()){
				gerenciadorDePrecos.registerPromotion(entry.getValue(), entry.getKey());
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}


	public void registerProduct(String Id, int price) {
		gerenciadorDePrecos.registerProduct(Id, price);
		try {
			mercadoDataBank.registerProduto(Id, price);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public void registerPromotion(String productId, double promotionPrice, int itemAmount) {
		gerenciadorDePrecos.registerPromotion(productId, promotionPrice, itemAmount);
		try {
			Promotion prom = new Promotion(promotionPrice / (double)itemAmount, itemAmount);
			mercadoDataBank.registerPromotion(prom, productId);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}


	public void addCaixa() {
		caixas.add(new Caixa(gerenciadorDePrecos));
	}

	public void addCaixa(int index) {
		caixas.add(index, new Caixa(gerenciadorDePrecos));
	}

	public Caixa getCaixa(int index) {
		return caixas.get(index);
	}

	public void eraseCaixa(int index) {
		caixas.remove(index);
	}

	public void setPriceControler(MapPriceControler gerenciadorDePrecos) {
		this.gerenciadorDePrecos = gerenciadorDePrecos;
	}

	public MapPriceControler getPriceControler() {
		return gerenciadorDePrecos;
	}
}
