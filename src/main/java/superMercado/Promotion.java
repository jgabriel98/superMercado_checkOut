package superMercado;

public class Promotion {
	private double unitPrice;
	private Integer minItens;

	public Promotion(){}

	public Promotion(double unitPrice, Integer minItens){
		setUnitPrice(unitPrice);
		setMinItens(minItens);
	}



	public double getUnitPrice(){
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice){
		if(unitPrice < 0.0){
			throw new RuntimeException("Uma promoção nao pode ter preço negativo");
		}
		this.unitPrice = unitPrice;
	}

	public Integer getMinItens(){
		return minItens;
	}
	public void setMinItens(Integer minItens){
		if(minItens < 0.0){
			throw new RuntimeException("Uma promoção nao pode ter min_itens negativo");
		}
		this.minItens = minItens;
	}

}
