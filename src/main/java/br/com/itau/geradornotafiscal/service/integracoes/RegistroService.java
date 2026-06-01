package br.com.itau.geradornotafiscal.service.integracoes;

import br.com.itau.geradornotafiscal.model.NotaFiscal;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class RegistroService {

    private static final Logger log = LoggerFactory.getLogger(RegistroService.class);
    private static final String RETRY_INSTANCE = "integracoesPosVenda";

    @Async
    @Retry(name = RETRY_INSTANCE, fallbackMethod = "fallbackRegistro")
    public void registrarNotaFiscal(NotaFiscal notaFiscal) {
        log.info("Iniciando registro oficial para a NF: {} na thread: {}",
                notaFiscal.getIdNotaFiscal(), Thread.currentThread().getName());

        try {
            Thread.sleep(500);
            System.out.println("Registra nota executada com sucesso por: " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread de registro da nota fiscal foi interrompida", e);
        }
    }

    public void fallbackRegistro(NotaFiscal notaFiscal, Exception ex) {
        log.error("[FALLBACK REGISTRO] - Esgotadas todas as tentativas de retry para a NF: {}. Motivo original: {}",
                notaFiscal.getIdNotaFiscal(), ex.getMessage());

        enviarParaFilaDeContingencia("fila-registro-dlq", notaFiscal, ex);
    }

    private void enviarParaFilaDeContingencia(String nomeFila, NotaFiscal notaFiscal, Exception erro) {
        log.info("[SQS CONTINGÊNCIA] Salvando NF {} na fila [{}] para processamento assíncrono tardio.",
                notaFiscal.getIdNotaFiscal(), nomeFila);
    }
}