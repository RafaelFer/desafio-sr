package br.com.itau.geradornotafiscal.service.integracoes;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import br.com.itau.geradornotafiscal.model.NotaFiscal;

@Service
public class EstoqueService {

    @Async
    public void enviarNotaFiscalParaBaixaEstoque(NotaFiscal notaFiscal) {
        try{
            System.out.println("Baixa de estoque executada por: " + Thread.currentThread());
        }catch (Exception e){
            System.err.println("[ERRO CRÍTICO] Falha ao baixar estoque para a NF: " + notaFiscal.getIdNotaFiscal() + ". Motivo: " + e.getMessage());
            this.enviarParaFilaDeReprocessamento("fila-estoque-retry", notaFiscal, e);
        }
    }

    private void enviarParaFilaDeReprocessamento(String nomeFila, NotaFiscal notaFiscal, Exception erro) {
        System.out.println("[SQS PREPARADO] Salvando NF " + notaFiscal.getIdNotaFiscal() + " na fila [" + nomeFila + "] para reprocessamento futuro.");
    }
}