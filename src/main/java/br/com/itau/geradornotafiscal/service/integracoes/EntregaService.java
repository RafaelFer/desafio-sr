package br.com.itau.geradornotafiscal.service.integracoes;

import br.com.itau.geradornotafiscal.model.NotaFiscal;
import br.com.itau.geradornotafiscal.port.out.EntregaIntegrationPort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EntregaService {

    @Async
    public void agendarEntrega(NotaFiscal notaFiscal) {

        try {
            //Simula o agendamento da entrega
            Thread.sleep(150);
            new EntregaIntegrationPort().criarAgendamentoEntrega(notaFiscal);
            System.out.println("Agendamento de entrega por: " + Thread.currentThread());
        } catch (Exception e) {
            this.enviarParaFilaDeReprocessamento("fila-estoque-retry", notaFiscal, e);
            throw new RuntimeException(e);
        }
    }

    private void enviarParaFilaDeReprocessamento(String nomeFila, NotaFiscal notaFiscal, Exception erro) {
        System.out.println("[SQS PREPARADO] Salvando NF " + notaFiscal.getIdNotaFiscal() + " na fila [" + nomeFila + "] para reprocessamento futuro.");
    }
}
