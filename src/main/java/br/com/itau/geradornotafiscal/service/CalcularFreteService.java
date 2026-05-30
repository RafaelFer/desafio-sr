package br.com.itau.geradornotafiscal.service;

import java.math.BigDecimal;

import br.com.itau.geradornotafiscal.model.Pedido;
import br.com.itau.geradornotafiscal.model.Regiao;

public interface CalcularFreteService {

	public BigDecimal calcularFreteComPercentual(Regiao regiao, Pedido pedido);
}
