package br.com.itau.geradornotafiscal.model;

import java.math.BigDecimal;

import br.com.itau.geradornotafiscal.service.CalcularFreteService;

public enum Regiao implements CalcularFreteService{
    NORTE {
    	@Override
    	public BigDecimal calcularFreteComPercentual(Regiao regiao, Pedido pedido) {    		
    		return  pedido.getValorFrete().multiply(new BigDecimal(1.08)) ;
    	}
    },
    NORDESTE{
    	@Override
    	public BigDecimal calcularFreteComPercentual(Regiao regiao, Pedido pedido) {
    		
    		return  pedido.getValorFrete().multiply(new BigDecimal(1.085));
    	}
    },
    CENTRO_OESTE{
    	@Override
    	public BigDecimal calcularFreteComPercentual(Regiao regiao, Pedido pedido) {
    		return  pedido.getValorFrete().multiply(new BigDecimal(1.07));
    	}
    },
    SUDESTE{
    	@Override
    	public BigDecimal calcularFreteComPercentual(Regiao regiao, Pedido pedido) {
    		return pedido.getValorFrete().multiply(new BigDecimal(1.048));
    	}
    },
    SUL{
    	@Override
		public BigDecimal calcularFreteComPercentual(Regiao regiao, Pedido pedido) {
			return pedido.getValorFrete().multiply(new BigDecimal(1.06));
		}
    };

	
}
