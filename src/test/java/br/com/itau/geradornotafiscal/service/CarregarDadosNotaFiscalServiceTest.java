package br.com.itau.geradornotafiscal.service;

import br.com.itau.geradornotafiscal.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarregarDadosNotaFiscalServiceTest {

    private final CarregarDadosNotaFiscalService service = new CarregarDadosNotaFiscalService();

    @Test
    void deveEmitirNotaFiscalCorretamente() {
        // Arrange
        Pedido pedido = Pedido.builder()
                .valorTotalItens(new BigDecimal("500.00"))
                .destinatario(Destinatario.builder()
                        .nome("Cliente Teste")
                        .tipoPessoa(TipoPessoa.FISICA)
                        .documentos(List.of())
                        .enderecos(List.of())
                        .build())
                .build();

        BigDecimal valorFrete = new BigDecimal("50.00");
        List<ItemNotaFiscal> itens = List.of(new ItemNotaFiscal());

        // Act
        NotaFiscal notaFiscal = service.emitirNotaFiscal(pedido, valorFrete, itens);

        // Assert
        assertNotNull(notaFiscal.getIdNotaFiscal()); // Valida se o UUID foi gerado
        assertNotNull(notaFiscal.getData());         // Valida se a data foi preenchida
        assertEquals(new BigDecimal("500.00"), notaFiscal.getValorTotalItens());
        assertEquals(valorFrete, notaFiscal.getValorFrete());
        assertEquals(itens, notaFiscal.getItens());
        assertEquals(pedido.getDestinatario(), notaFiscal.getDestinatario());
    }
}