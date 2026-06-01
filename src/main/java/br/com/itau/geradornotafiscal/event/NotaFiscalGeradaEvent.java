package br.com.itau.geradornotafiscal.event;

import br.com.itau.geradornotafiscal.model.NotaFiscal;
import lombok.Getter;

@Getter
public class NotaFiscalGeradaEvent {
    private final NotaFiscal notaFiscal;

    public NotaFiscalGeradaEvent(NotaFiscal notaFiscal) {
        this.notaFiscal = notaFiscal;
    }
}