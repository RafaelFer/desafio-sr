package br.com.itau.geradornotafiscal.service.integracoes;

import br.com.itau.geradornotafiscal.model.NotaFiscal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class EstoqueServiceTest {

    @InjectMocks
    private EstoqueService estoqueService;

    private NotaFiscal notaFiscal;

    @BeforeEach
    void setUp() {
        notaFiscal = NotaFiscal.builder()
                .idNotaFiscal("NF-ESTOQUE-001")
                .data(LocalDateTime.now())
                .build();
    }

    @Test
    void deveExecutarBaixaDeEstoqueSemErros() {
        // Valida que o método principal não lança exceções
        assertDoesNotThrow(() -> estoqueService.enviarNotaFiscalParaBaixaEstoque(notaFiscal));
    }

    @Test
    void deveExecutarFallbackEstoqueCorretamente() {
        // Valida que o método de fallback trata a exceção sem quebrar a execução
        Exception ex = new RuntimeException("Erro de conexão com o sistema de estoque");
        assertDoesNotThrow(() -> estoqueService.fallbackEstoque(notaFiscal, ex));
    }
}