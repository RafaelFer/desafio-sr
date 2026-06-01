package br.com.itau.geradornotafiscal.service.integracoes;

import br.com.itau.geradornotafiscal.model.NotaFiscal;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class FinanceiroService {

    private static final Logger log = LoggerFactory.getLogger(FinanceiroService.class);
    private static final String RETRY_INSTANCE = "integracoesPosVenda";

    @Async
    @Retry(name = RETRY_INSTANCE, fallbackMethod = "fallbackFinanceiro")
    public void enviarNotaFiscalParaContasReceber(NotaFiscal notaFiscal) {
        try {
            Thread.sleep(250);
            System.out.println("Envia nota para contas a receber executada por: " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    public void fallbackFinanceiro(NotaFiscal notaFiscal, Exception ex) {
        log.error("[FALLBACK FINANCEIRO] - Esgotadas todas as tentativas de retry para a NF: {}. Motivo original: {}",
                notaFiscal.getIdNotaFiscal(), ex.getMessage());
        enviarParaFilaDeContingencia("fila-financeiro-dlq", notaFiscal, ex);
    }

    private void enviarParaFilaDeContingencia(String nomeFila, NotaFiscal notaFiscal, Exception erro) {
        log.info("[SQS CONTINGÊNCIA] Salvando NF {} na fila [{}] para processamento assíncrono tardio.",
                notaFiscal.getIdNotaFiscal(), nomeFila);
    }
}