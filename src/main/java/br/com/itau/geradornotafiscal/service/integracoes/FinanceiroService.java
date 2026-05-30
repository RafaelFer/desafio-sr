package br.com.itau.geradornotafiscal.service.integracoes;

import br.com.itau.geradornotafiscal.model.NotaFiscal;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class FinanceiroService {

    @Async
    public void enviarNotaFiscalParaContasReceber(NotaFiscal notaFiscal) {

        try {
            //Simula o envio da nota fiscal para o contas a receber
            Thread.sleep(250);
            System.out.println("Envia nota para contas a receber executada por: " + Thread.currentThread());
        } catch (InterruptedException e) {
            this.enviarParaFilaDeReprocessamento("fila-estoque-retry", notaFiscal, e);
            throw new RuntimeException(e);
        }

    }

    private void enviarParaFilaDeReprocessamento(String nomeFila, NotaFiscal notaFiscal, Exception erro) {
        System.out.println("[SQS PREPARADO] Salvando NF " + notaFiscal.getIdNotaFiscal() + " na fila [" + nomeFila + "] para reprocessamento futuro.");
    }
}
