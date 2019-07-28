package superMercado;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Caixa {
	private MapPriceControler priceControler;
	private HashMap<String, Integer> items;


	//flag para saber se houve alteração na quantidade de itens, e consequentemente, se há necessidade de recalcular os totais
	//uso dessa flag para evitar o recalculo desnecessário
	private boolean priceNeedUpdate = false;

	private int totalPrice = 0;
	private int totalDiscount = 0;

	public Caixa(MapPriceControler priceControler) {
		this.priceControler = priceControler;
		this.items = new HashMap<String, Integer>();
	}

	public void clear() {
		totalPrice = 0;
		totalDiscount = 0;
		items.clear();
	}

	//não utilizado no projeto (ainda), porem método implementado pensando na manutenibilidade e escalabilidade (replicação do mesmo controlador para varios caixas, etc)
	public MapPriceControler getPriceControler() {
		try {
			return (MapPriceControler)this.priceControler.clone();
		}catch(CloneNotSupportedException ex) {
			System.out.print(ex.getMessage());
		}

		return null;
	}

	public void setPriceControler(MapPriceControler newControler) {
		this.priceControler = newControler;
	}


	protected void calculateShop() {
		double acumulateDiscount = 0.0;
		double finalPrice = 0.0;

		for(Map.Entry<String, Integer> item : items.entrySet()){
			String id = item.getKey();
			int quantidade = item.getValue();
			List<Map.Entry<Promotion, Integer>> bestProms;

			double itemFinalPrice = 0.0;
			//sempre pega o melhor desconto possivel para a quantidade de itens disponíveis (ainda não processados)
			//e continua fazendo isso até que não haja mais promoções para a quantidade de itens disponíveis.
			bestProms = priceControler.getBestProductPromotions(id, quantidade);
			if(!bestProms.isEmpty()) {
				double discountedPrice = 0.0;
				int leftItens = quantidade;
				for(Map.Entry<Promotion, Integer> prom : bestProms){
					discountedPrice += prom.getValue() * (prom.getKey().getUnitPrice() * prom.getKey().getMinItens());
					leftItens -= prom.getValue() * prom.getKey().getMinItens();
				}
				//double totalFinalPrice = discountedPrice + leftItens * priceControler.getProductPrice(id);
				itemFinalPrice += discountedPrice + leftItens * priceControler.getProductPrice(id);
			}else {
				itemFinalPrice += quantidade * priceControler.getProductPrice(id);
			}
			double itemNormalPrice = quantidade * priceControler.getProductPrice(id);
			acumulateDiscount += itemNormalPrice - itemFinalPrice;
			finalPrice += itemFinalPrice;

		}

		totalDiscount = (int)acumulateDiscount;
		totalPrice = (int)finalPrice;
	}


	public int getTotalPrice() {
		if(priceNeedUpdate) {
			calculateShop();
			priceNeedUpdate = false;
		}

		return totalPrice;
	}

	public int getTotalDiscount() {
		if(priceNeedUpdate) {
			calculateShop();
			priceNeedUpdate = false;
		}

		return totalDiscount;
	}

	public void addItem(String id) {
		//atualizando a contagem de item do tipo 'id'
		Integer count = items.getOrDefault(id, 0);
		count++;
		items.put(id, count);
		priceNeedUpdate = true;
	}

	public void removeItem(String Id) {
		Integer count = items.getOrDefault(Id, 0);

		if(count <= 0) {
			//throw nao_tem_esse_item_no_caixa
			throw new RuntimeException("Não existe o item" + Id + " no caixa");
		}
		count--;
		items.put(Id, count);
		priceNeedUpdate = true;
	}


}
