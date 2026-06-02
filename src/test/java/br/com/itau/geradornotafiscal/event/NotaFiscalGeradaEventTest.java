package br.com.itau.geradornotafiscal.event;

import br.com.itau.geradornotafiscal.model.NotaFiscal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NotaFiscalGeradaEventTest {

    @Test
    void deveCriarEventoComNotaFiscal() {
        // Arrange
        NotaFiscal notaFiscal = NotaFiscal.builder()
                .idNotaFiscal("NF-123")
                .build();

        // Act
        NotaFiscalGeradaEvent event = new NotaFiscalGeradaEvent(notaFiscal);

        // Assert
        assertNotNull(event);
        assertEquals(notaFiscal, event.getNotaFiscal());
    }
}