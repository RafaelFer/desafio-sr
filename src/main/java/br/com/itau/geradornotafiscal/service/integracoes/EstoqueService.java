package br.com.itau.geradornotafiscal.service.integracoes;

import br.com.itau.geradornotafiscal.model.NotaFiscal;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EstoqueService {

    private static final Logger log = LoggerFactory.getLogger(EstoqueService.class);
    private static final String RETRY_INSTANCE = "integracoesPosVenda";


    @Async
    @Retry(name = RETRY_INSTANCE, fallbackMethod = "fallbackEstoque")
    public void enviarNotaFiscalParaBaixaEstoque(NotaFiscal notaFiscal) {
        log.info("Iniciando baixa de estoque para a NF: {} na thread: {}",
                notaFiscal.getIdNotaFiscal(), Thread.currentThread().getName());
        try {
            Thread.sleep(200);
            System.out.println("Baixa de estoque executada com sucesso por: " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            // Boa prática: Restaura o estado de interrupção da thread
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread de integração do estoque foi interrompida", e);
        }
    }


    public void fallbackEstoque(NotaFiscal notaFiscal, Exception ex) {
        log.error("[FALLBACK ESTOQUE] - Esgotadas todas as tentativas de retry para a NF: {}. Motivo original: {}",
                notaFiscal.getIdNotaFiscal(), ex.getMessage());
        enviarParaFilaDeContingencia("fila-estoque-dlq", notaFiscal, ex);
    }

    private void enviarParaFilaDeContingencia(String nomeFila, NotaFiscal notaFiscal, Exception erro) {
        log.info("[SQS CONTINGÊNCIA] Salvando NF {} na fila [{}] para processamento assíncrono tardio.",
                notaFiscal.getIdNotaFiscal(), nomeFila);
    }
}