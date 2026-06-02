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
class EntregaServiceTest {

    @InjectMocks
    private EntregaService entregaService;

    private NotaFiscal notaFiscal;

    @BeforeEach
    void setUp() {
        notaFiscal = NotaFiscal.builder()
                .idNotaFiscal("NF-ENTREGA-999")
                .data(LocalDateTime.now())
                .build();
    }

    @Test
    void deveExecutarFallbackEntregaCorretamente() {
        // Valida o fallback
        Exception ex = new RuntimeException("Erro na integração de entrega");
        assertDoesNotThrow(() -> entregaService.fallbackEntrega(notaFiscal, ex));
    }

}