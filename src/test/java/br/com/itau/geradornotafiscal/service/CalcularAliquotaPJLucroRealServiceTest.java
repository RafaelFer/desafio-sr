package br.com.itau.geradornotafiscal.service;

import br.com.itau.geradornotafiscal.model.Item;
import br.com.itau.geradornotafiscal.model.ItemNotaFiscal;
import br.com.itau.geradornotafiscal.model.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CalcularAliquotaPJLucroRealServiceTest {

    private CalcularAliquotaPJLucroRealService service;

    @BeforeEach
    void setUp() {
        service = new CalcularAliquotaPJLucroRealService();
    }

    @ParameterizedTest
    @CsvSource({
            "999.00, 0.03",    // Limite da primeira faixa (<= 999)
            "1500.00, 0.09",   // Segunda faixa (> 999 e <= 2000)
            "4000.00, 0.15",   // Terceira faixa (> 2000 e <= 5000)
            "5001.00, 0.20"    // Quarta faixa (> 5000)
    })
    void deveCalcularAliquotaCorretaParaLucroReal(String valorPedido, String aliquotaEsperada) {
        // Arrange
        Item item = Item.builder()
                .idItem("1")
                .descricao("Produto Teste")
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

        assertThat(resultado.get(0).getValorTributoItem())
                .isEqualByComparingTo(valorTributoEsperado);
    }
}