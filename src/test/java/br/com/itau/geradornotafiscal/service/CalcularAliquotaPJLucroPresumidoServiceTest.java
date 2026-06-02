package br.com.itau.geradornotafiscal.service;

import br.com.itau.geradornotafiscal.model.Item;
import br.com.itau.geradornotafiscal.model.ItemNotaFiscal;
import br.com.itau.geradornotafiscal.model.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalcularAliquotaPJLucroPresumidoServiceTest {

    private CalcularAliquotaPJLucroPresumidoService service;

    @BeforeEach
    void setUp() {
        service = new CalcularAliquotaPJLucroPresumidoService();
    }

    @ParameterizedTest
    @CsvSource({
            "500.00, 0.03",    // <= 999
            "1200.00, 0.09",   // > 999 e <= 2000
            "3500.00, 0.16",   // > 2000 e <= 5000 (Note a diferença: 0.16 nesta classe)
            "6000.00, 0.20"    // > 5000
    })
    void deveCalcularAliquotaCorretaParaLucroPresumido(String valorPedido, String aliquotaEsperada) {
        // Arrange
        Item item = Item.builder()
                .idItem("1")
                .valorUnitario(new BigDecimal("100.00"))
                .quantidade(1)
                .build();

        Pedido pedido = Pedido.builder()
                .valorTotalItens(new BigDecimal(valorPedido))
                .itens(List.of(item))
                .build();

        // Act
        List<ItemNotaFiscal> resultado = service.calcularAlicotaProduto(pedido);

        // Assert
        BigDecimal aliquotaEsperadaBD = new BigDecimal(aliquotaEsperada);
        BigDecimal valorTributoEsperado = new BigDecimal("100.00").multiply(aliquotaEsperadaBD);

        assertEquals(0, valorTributoEsperado.compareTo(resultado.get(0).getValorTributoItem()),
                "O valor do tributo calculado está incorreto");
    }
}