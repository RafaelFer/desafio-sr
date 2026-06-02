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

class CalcularAliquotaPJSimplesNacionalServiceTest {

    private CalcularAliquotaPJSimplesNacionalService service;

    @BeforeEach
    void setUp() {
        service = new CalcularAliquotaPJSimplesNacionalService();
    }

    @ParameterizedTest
    @CsvSource({
            "500.00, 0.03",   // Menor que 999
            "1500.00, 0.07",  // Entre 999 e 1999
            "3000.00, 0.13",  // Entre 2000 e 4999
            "6000.00, 0.19"   // Maior que 5000
    })
    void deveCalcularAliquotaCorretaPorFaixaDeValor(String valorPedido, String aliquotaEsperada) {
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

        assertEquals(valorTributoEsperado, resultado.get(0).getValorTributoItem());
    }
}