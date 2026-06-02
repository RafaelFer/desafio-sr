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
class RegistroServiceTest {

    @InjectMocks
    private RegistroService registroService;

    private NotaFiscal notaFiscal;

    @BeforeEach
    void setUp() {
        notaFiscal = NotaFiscal.builder()
                .idNotaFiscal("NF-12345")
                .data(LocalDateTime.now())
                .build();
    }

    @Test
    void deveExecutarFluxoPrincipalSemErros() {
        // Como o método é @Async e tem @Retry, o teste unitário foca
        // em verificar se a chamada do método principal não quebra a execução.
        assertDoesNotThrow(() -> registroService.registrarNotaFiscal(notaFiscal));
    }

    @Test
    void deveExecutarFallbackCorretamente() {
        // Testando o método de fallback diretamente
        Exception ex = new RuntimeException("Erro simulado na integração");

        assertDoesNotThrow(() -> registroService.fallbackRegistro(notaFiscal, ex));
    }
}