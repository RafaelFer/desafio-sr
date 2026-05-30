package br.com.itau.geradornotafiscal.service;

import java.math.BigDecimal;
import java.util.List;

import br.com.itau.geradornotafiscal.model.ItemNotaFiscal;
import br.com.itau.geradornotafiscal.model.Pedido;
import br.com.itau.geradornotafiscal.service.strategy.AliquotaStrategy;
import org.springframework.stereotype.Component;


@Component("calcularAliquotaPJSimplesNacionalService")
public class CalcularAliquotaPJSimplesNacionalService implements AliquotaStrategy {

	@Override
	public List<ItemNotaFiscal> calcularAlicotaProduto(Pedido pedido) {
		BigDecimal valorTotalItens = pedido.getValorTotalItens();
		BigDecimal aliquota;

		if (valorTotalItens.compareTo(BigDecimal.valueOf(999)) == -1 ) {
			aliquota = BigDecimal.valueOf(0.03);
		} else if (valorTotalItens.compareTo(BigDecimal.valueOf(2000)) == -1 ) {
			aliquota = BigDecimal.valueOf(0.07);
		} else if (valorTotalItens.compareTo(BigDecimal.valueOf(5000)) == -1 )  {
			aliquota = BigDecimal.valueOf(0.13);
		} else {
			aliquota = BigDecimal.valueOf(0.19);
		}
		return calcularAliquota(pedido.getItens(), aliquota);

	}
	

}


