package br.com.itau.geradornotafiscal.listener;

import br.com.itau.geradornotafiscal.event.NotaFiscalGeradaEvent;
import br.com.itau.geradornotafiscal.model.NotaFiscal;
import br.com.itau.geradornotafiscal.service.integracoes.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NotaFiscalGeradaListener {

    private final EstoqueService estoqueService;
    private final RegistroService registroService;
    private final EntregaService entregaService;
    private final FinanceiroService financeiroService;

    public NotaFiscalGeradaListener(EstoqueService estoqueService, RegistroService registroService,
                                    EntregaService entregaService, FinanceiroService financeiroService) {
        this.estoqueService = estoqueService;
        this.registroService = registroService;
        this.entregaService = entregaService;
        this.financeiroService = financeiroService;
    }

    @EventListener
    public void processarPosVenda(NotaFiscalGeradaEvent event) {
        NotaFiscal notaFiscal = event.getNotaFiscal();

        estoqueService.enviarNotaFiscalParaBaixaEstoque(notaFiscal);
        registroService.registrarNotaFiscal(notaFiscal);
        entregaService.agendarEntrega(notaFiscal);
        financeiroService.enviarNotaFiscalParaContasReceber(notaFiscal);
    }
}