package br.com.itau.geradornotafiscal.service;

import java.math.BigDecimal;
import java.util.List;

import br.com.itau.geradornotafiscal.service.strategy.AliquotaStrategy;
import org.springframework.stereotype.Service;
import br.com.itau.geradornotafiscal.model.*;
import br.com.itau.geradornotafiscal.service.strategy.AliquotaStrategyFactory;
import br.com.itau.geradornotafiscal.service.integracoes.*;

@Service
public class GeradorNotaFiscalService {

	private final AliquotaStrategyFactory strategyFactory;
	private final CarregarDadosNotaFiscalService emitirNotaFiscalService;
	private final EstoqueService estoqueService;
	private final RegistroService registroService;
	private final EntregaService entregaService;
	private final FinanceiroService financeiroService;

	public GeradorNotaFiscalService(AliquotaStrategyFactory strategyFactory,
	                                CarregarDadosNotaFiscalService emitirNotaFiscalService,
	                                EstoqueService estoqueService, RegistroService registroService,
	                                EntregaService entregaService, FinanceiroService financeiroService) {
		this.strategyFactory = strategyFactory;
		this.emitirNotaFiscalService = emitirNotaFiscalService;
		this.estoqueService = estoqueService;
		this.registroService = registroService;
		this.entregaService = entregaService;
		this.financeiroService = financeiroService;
	}

	public NotaFiscal gerarNotaFiscal(Pedido pedido) {
		Destinatario destinatario = pedido.getDestinatario();

		AliquotaStrategy strategy = strategyFactory.obterEstrategia(destinatario);
		List<ItemNotaFiscal> itemNotaFiscalList = strategy.calcularAlicotaProduto(pedido);

		Regiao regiao = buscarRegiao(pedido);
		BigDecimal valorFreteComPercentual = regiao.calcularFreteComPercentual(regiao, pedido);

		NotaFiscal notaFiscal = emitirNotaFiscalService.emitirNotaFiscal(pedido, valorFreteComPercentual, itemNotaFiscalList);

		estoqueService.enviarNotaFiscalParaBaixaEstoque(notaFiscal);
		registroService.registrarNotaFiscal(notaFiscal);
		entregaService.agendarEntrega(notaFiscal);
		financeiroService.enviarNotaFiscalParaContasReceber(notaFiscal);

		return notaFiscal;
	}

	private Regiao buscarRegiao(Pedido pedido) {
		return pedido.getDestinatario().getEnderecos().stream()
				.filter(endereco -> endereco.getFinalidade() == Finalidade.ENTREGA
						|| endereco.getFinalidade() == Finalidade.COBRANCA_ENTREGA)
				.map(Endereco::getRegiao)
				.findFirst()
				.orElse(null);
	}
}