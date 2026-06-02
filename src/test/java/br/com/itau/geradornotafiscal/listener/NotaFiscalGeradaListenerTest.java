package br.com.itau.geradornotafiscal.listener;

import br.com.itau.geradornotafiscal.event.NotaFiscalGeradaEvent;
import br.com.itau.geradornotafiscal.model.NotaFiscal;
import br.com.itau.geradornotafiscal.service.integracoes.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotaFiscalGeradaListenerTest {

    @Mock
    private EstoqueService estoqueService;
    @Mock
    private RegistroService registroService;
    @Mock
    private EntregaService entregaService;
    @Mock
    private FinanceiroService financeiroService;

    @InjectMocks
    private NotaFiscalGeradaListener listener;

    @Test
    void deveChamarTodosOsServicosAoReceberEvento() {
        // Arrange
        NotaFiscal notaFiscal = NotaFiscal.builder().idNotaFiscal("NF-123").build();
        NotaFiscalGeradaEvent event = new NotaFiscalGeradaEvent(notaFiscal);

        // Act
        listener.processarPosVenda(event);

        // Assert
        verify(estoqueService).enviarNotaFiscalParaBaixaEstoque(notaFiscal);
        verify(registroService).registrarNotaFiscal(notaFiscal);
        verify(entregaService).agendarEntrega(notaFiscal);
        verify(financeiroService).enviarNotaFiscalParaContasReceber(notaFiscal);
    }
}