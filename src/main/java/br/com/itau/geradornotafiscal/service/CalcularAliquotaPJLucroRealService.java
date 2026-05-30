package br.com.itau.geradornotafiscal.service;

import java.math.BigDecimal;
import java.util.List;

import br.com.itau.geradornotafiscal.model.ItemNotaFiscal;
import br.com.itau.geradornotafiscal.model.Pedido;
import br.com.itau.geradornotafiscal.service.strategy.AliquotaStrategy;
import org.springframework.stereotype.Component;

@Component("calcularAliquotaPJLucroRealService")
public class CalcularAliquotaPJLucroRealService implements AliquotaStrategy {

	@Override
	public List<ItemNotaFiscal> calcularAlicotaProduto(Pedido pedido) {
		
		BigDecimal valorTotalItens = pedido.getValorTotalItens();
		BigDecimal aliquota;

		if (valorTotalItens.compareTo(new BigDecimal(999))  != 1) {
			aliquota = BigDecimal.valueOf(0.03);
		} else if (valorTotalItens.compareTo(new BigDecimal(2000))  != 1) {
			aliquota = BigDecimal.valueOf(0.09);
		} else if (valorTotalItens.compareTo(new BigDecimal(5000))  != 1) {
			aliquota = BigDecimal.valueOf(0.15);
		} else {
			aliquota = BigDecimal.valueOf(0.20);
		}
		
		return calcularAliquota(pedido.getItens(),aliquota);
	}
}


