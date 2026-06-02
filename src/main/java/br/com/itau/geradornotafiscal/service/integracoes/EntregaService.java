package br.com.itau.geradornotafiscal.service.integracoes;

import br.com.itau.geradornotafiscal.model.NotaFiscal;
import br.com.itau.geradornotafiscal.port.out.EntregaIntegrationPort;
import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EntregaService {

    private static final Logger log = LoggerFactory.getLogger(EntregaService.class);
    private static final String RETRY_INSTANCE = "integracoesPosVenda";

    @Async
    @Timed(value = "entrega.agendamento.time", description = "Tempo total de agendamento de entrega (incluindo retries)")
    @Retry(name = RETRY_INSTANCE, fallbackMethod = "fallbackEntrega")
    public void agendarEntrega(NotaFiscal notaFiscal) {
        try {
            Thread.sleep(150);
            new EntregaIntegrationPort().criarAgendamentoEntrega(notaFiscal);
            System.out.println("Agendamento de entrega por: " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    public void fallbackEntrega(NotaFiscal notaFiscal, Exception ex) {
        log.error("[FALLBACK ENTREGA] - Esgotadas todas as tentativas de retry para a NF: {}. Motivo original: {}",
                notaFiscal.getIdNotaFiscal(), ex.getMessage());
        enviarParaFilaDeContingencia("fila-entrega-dlq", notaFiscal, ex);
    }

    private void enviarParaFilaDeContingencia(String nomeFila, NotaFiscal notaFiscal, Exception erro) {
        log.info("[SQS CONTINGÊNCIA] Salvando NF {} na fila [{}] para processamento assíncrono tardio.",
                notaFiscal.getIdNotaFiscal(), nomeFila);
    }
}