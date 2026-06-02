package br.com.itau.geradornotafiscal.service;

import br.com.itau.geradornotafiscal.event.NotaFiscalGeradaEvent;
import br.com.itau.geradornotafiscal.model.*;
import br.com.itau.geradornotafiscal.service.strategy.AliquotaStrategy;
import br.com.itau.geradornotafiscal.service.strategy.AliquotaStrategyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeradorNotaFiscalServiceTest {

    @Mock
    private AliquotaStrategyFactory strategyFactory;

    @Mock
    private CarregarDadosNotaFiscalService emitirNotaFiscalService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private AliquotaStrategy aliquotaStrategy;

    @InjectMocks
    private GeradorNotaFiscalService service;

    private Pedido pedido;

    @BeforeEach
    void setUp() {
        // Precisamos definir o tipoPessoa para satisfazer a validação @NonNull do Lombok
        pedido = Pedido.builder()
                .valorTotalItens(new BigDecimal("1000.00")) // Adicionado caso precise
                .valorFrete(new BigDecimal("100.00"))
                .destinatario(Destinatario.builder()
                        .nome("Cliente Teste")
                        .tipoPessoa(TipoPessoa.FISICA) // <--- ADICIONE ESTA LINHA
                        .documentos(Collections.emptyList()) // <--- Adicionado pois é @NonNull
                        .enderecos(List.of(Endereco.builder()
                                .finalidade(Finalidade.ENTREGA)
                                .regiao(Regiao.NORTE)
                                .build()))
                        .build())
                .build();
    }

    @Test
    void deveGerarNotaFiscalComSucesso() {
        // Arrange
        List<ItemNotaFiscal> itens = Collections.emptyList();
        NotaFiscal notaEsperada = new NotaFiscal();

        when(strategyFactory.obterEstrategia(any())).thenReturn(aliquotaStrategy);
        when(aliquotaStrategy.calcularAlicotaProduto(pedido)).thenReturn(itens);
        when(emitirNotaFiscalService.emitirNotaFiscal(eq(pedido), any(BigDecimal.class), eq(itens)))
                .thenReturn(notaEsperada);

        // Act
        NotaFiscal resultado = service.gerarNotaFiscal(pedido);

        // Assert
        assertNotNull(resultado);
        verify(eventPublisher, times(1)).publishEvent(any(NotaFiscalGeradaEvent.class));
        verify(emitirNotaFiscalService).emitirNotaFiscal(eq(pedido), any(BigDecimal.class), eq(itens));
    }
}