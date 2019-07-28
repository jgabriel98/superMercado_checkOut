package superMercado;

import java.util.List;
import java.util.Map;

public interface PriceControler {
	/**
	 * @param Id id do produto (item).
	 * @param promotionPrice preço total da promoção (não por unidade).
	 * @param itemAmount quantidade necessária para a promoção.
	 */
	 //ex: item "A", 3 por 130 centavos --> Id="A", promotionPrice=130, itemAmount=3
	public void registerPromotion(String Id, double promotionPrice, int itemAmount);

	public void registerProduct(String Id, int price);
	//public void updateProduct(String id, int price);

	/**
	 * @return todos os produtos;
	 */
	//retorna apenas o Id para permitir uma interface mais flexivel, isto é, que acomode qualquer estrutura/definição de produto
	public List<Object> getAllProducts();
	public Object getProduct(String Id);

	public List<Promotion> getProductPromotions(String Id);

	public int getProductPrice(String Id);

	/**
	 *
	 * @param Id id do produto
	 * @param quantidade quantidade disponível do produto
	 * @return lista de pares de quais e quantas vezes promoções devem ser aplicadas
	 */
	public List<Map.Entry<Promotion, Integer>> getBestProductPromotions(String Id, Integer quantidade);

	public void removeProduct(String Id);

	//não temos 'removePromotion' na interface pelo fato que de que não um identificador de promoção na especificação do projeto
}
