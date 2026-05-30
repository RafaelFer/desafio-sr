package br.com.itau.geradornotafiscal.service.integracoes;

import br.com.itau.geradornotafiscal.model.NotaFiscal;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class RegistroService {

    @Async
    public void registrarNotaFiscal(NotaFiscal notaFiscal) {

        try {
            //Simula o registro da nota fiscal
            Thread.sleep(500);
            System.out.println("Registra nota executada por: " + Thread.currentThread());
        } catch (InterruptedException e) {
            this.enviarParaFilaDeReprocessamento("fila-estoque-retry", notaFiscal, e);
            throw new RuntimeException(e);
        }
    }

    private void enviarParaFilaDeReprocessamento(String nomeFila, NotaFiscal notaFiscal, Exception erro) {
        System.out.println("[SQS PREPARADO] Salvando NF " + notaFiscal.getIdNotaFiscal() + " na fila [" + nomeFila + "] para reprocessamento futuro.");
    }
}
