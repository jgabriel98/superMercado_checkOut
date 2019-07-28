package superMercado;

import java.util.*;


public class MapPriceControler implements Cloneable, PriceControler {
	/**
	 * um HashMap onde: key = Id_do_produto, value = preco_unitario_do_produto
	 */
	protected HashMap<String, Integer> itemPrices = new HashMap<String, Integer>();

	/**
	 * um HashMap onde: key=Id_do_produto, value=Promotions_TreeMap.
	 * tal  TreeMap terá um lista ordenada de promoções (por menor preço por unidade),
	 * onde: key=preco_promocional_por_item, value=numero_de_itens_necessarios.
	 */
	protected HashMap<String, TreeSet<Promotion>> itemPromotions = new HashMap<String, TreeSet<Promotion>>();
	static private Comparator<Promotion> promotionComparator = (Promotion p1, Promotion p2) -> (Double.compare(p1.getUnitPrice(), p2.getUnitPrice()));


	public void registerProduct(String Id, int price) {
		itemPrices.put(Id, price);
	}

	public int getProductPrice(String Id) {
		return itemPrices.get(Id);
	}

	public List<Object> getAllProducts() {
		Set<Map.Entry<String, Integer>> pairSet = itemPrices.entrySet();
		return new ArrayList<Object>(pairSet);
	}

	public Object getProduct(String Id) {
		return itemPrices.get(Id);
	}


	public List<Promotion> getProductPromotions(String Id) {
		Set<Promotion> promotionSet = itemPromotions.get(Id);
		return new ArrayList<Promotion>(promotionSet);
	}

	public void registerPromotion(String Id, double promotionPrice, int itemAmount) {
		registerPromotion(Id, new Promotion(promotionPrice / (double)itemAmount, itemAmount));
	}
	public void registerPromotion(String Id, Promotion prom) {
		TreeSet<Promotion> newPromo = itemPromotions.getOrDefault(Id, new TreeSet<Promotion>(promotionComparator));
		newPromo.add(prom);
		itemPromotions.put(Id, newPromo);
	}


	/**
	 * @return lista de Promotion das promoções que devem ser aplicadas para se obter o maior desconto. Lista vazia quando não existem promoções para essa quantidade de itens
	 */
	public List<Map.Entry<Promotion, Integer>> getBestProductPromotions(String Id, Integer quantidade) {
		TreeSet<Promotion> promotions = itemPromotions.get(Id);
		ArrayList<Map.Entry<Promotion, Integer>> promotionsToUse = new ArrayList<Map.Entry<Promotion, Integer>>();

		//se não houver promoções para esse produto
		if(promotions == null) return promotionsToUse;

		//percorre pela "lista" de promoções, ordenadas por menor preço unitário, e ao achar a primeira
		//que satisfazer a quantidade de itens, seleciona-a;
		for(Promotion prom : promotions){
			if(quantidade >= prom.getMinItens()) {
				int vezesAplicada = quantidade/prom.getMinItens();
				vezesAplicada--;		//aplica o maximo de vezes possiveis, menos a ultima
				if(vezesAplicada > 0){
					promotionsToUse.add(new AbstractMap.SimpleEntry<Promotion, Integer>(prom, vezesAplicada));
					quantidade -= vezesAplicada * prom.getMinItens();
				}
				break;
			}
		}

		//agora procura pela promoção que terá a maior economia ao ser aplicada uma unica vez ( pode ou não ser a mesma previamente selecionada (com menor preço unitário))
		Promotion promocaoMaiorDesconto = null;
		double maiorDesconto = -1.0;
		for(Promotion prom: promotions){
			double economiaPromocao = (itemPrices.get(Id) - prom.getUnitPrice()) * prom.getMinItens();	//desconto obtido ao aplicar uma única vez essa promoção
			if( quantidade >= prom.getMinItens() && maiorDesconto < economiaPromocao){
				maiorDesconto = economiaPromocao;
				promocaoMaiorDesconto = prom;
			}
		}

		if(promocaoMaiorDesconto != null)
			promotionsToUse.add(new AbstractMap.SimpleEntry<Promotion, Integer>(promocaoMaiorDesconto, 1));


		return promotionsToUse;
	}

	public void removeProduct(String Id) {
		itemPromotions.remove((Id));
		itemPrices.remove((Id));
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
