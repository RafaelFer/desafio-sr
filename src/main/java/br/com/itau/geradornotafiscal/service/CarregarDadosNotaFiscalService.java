package br.com.itau.geradornotafiscal.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import br.com.itau.geradornotafiscal.model.ItemNotaFiscal;
import br.com.itau.geradornotafiscal.model.NotaFiscal;
import br.com.itau.geradornotafiscal.model.Pedido;
import org.springframework.stereotype.Component;

@Component
public class CarregarDadosNotaFiscalService {
	
public NotaFiscal emitirNotaFiscal(Pedido pedido, BigDecimal valorFreteComPercentual, List<ItemNotaFiscal> itemNotaFiscalList ) {
		
		String idNotaFiscal = UUID.randomUUID().toString();
		NotaFiscal notaFiscal = NotaFiscal.builder()
				.idNotaFiscal(idNotaFiscal)
				.data(LocalDateTime.now())
				.valorTotalItens(pedido.getValorTotalItens())
				.valorFrete(valorFreteComPercentual)
				.itens(itemNotaFiscalList)
				.destinatario(pedido.getDestinatario())				
				.build();
		return notaFiscal;
	}
}
