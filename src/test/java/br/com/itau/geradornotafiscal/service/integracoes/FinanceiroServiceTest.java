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
class FinanceiroServiceTest {

    @InjectMocks
    private FinanceiroService financeiroService;

    private NotaFiscal notaFiscal;

    @BeforeEach
    void setUp() {
        notaFiscal = NotaFiscal.builder()
                .idNotaFiscal("NF-9999")
                .data(LocalDateTime.now())
                .build();
    }

    @Test
    void deveExecutarEnvioParaContasReceberSemErros() {
        assertDoesNotThrow(() -> financeiroService.enviarNotaFiscalParaContasReceber(notaFiscal));
    }

    @Test
    void deveExecutarFallbackFinanceiroCorretamente() {
        Exception ex = new RuntimeException("Erro simulado no financeiro");
        assertDoesNotThrow(() -> financeiroService.fallbackFinanceiro(notaFiscal, ex));
    }
}