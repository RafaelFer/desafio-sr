package br.com.itau.geradornotafiscal.service;

import br.com.itau.geradornotafiscal.model.Item;
import br.com.itau.geradornotafiscal.model.ItemNotaFiscal;
import br.com.itau.geradornotafiscal.model.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CalcularAliquotaPessoaFisicaServiceTest {

    private CalcularAliquotaPessoaFisicaService service;

    @BeforeEach
    void setUp() {
        service = new CalcularAliquotaPessoaFisicaService();
    }

    @ParameterizedTest
    @CsvSource({
            "400.00, 0.00",    // <= 499
            "1000.00, 0.12",   // > 499 e <= 2000
            "3000.00, 0.15",   // > 2000 e <= 3500
            "4000.00, 0.17"    // > 3500
    })
    void deveCalcularAliquotaCorretaParaPessoaFisica(String valorPedido, String aliquotaEsperada) {
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

        // Usando AssertJ para ignorar a escala do BigDecimal
        assertThat(resultado.get(0).getValorTributoItem())
                .isEqualByComparingTo(valorTributoEsperado);
    }
}