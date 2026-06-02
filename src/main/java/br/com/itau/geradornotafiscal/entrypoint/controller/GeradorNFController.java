package br.com.itau.geradornotafiscal.entrypoint.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.itau.geradornotafiscal.model.NotaFiscal;
import br.com.itau.geradornotafiscal.model.Pedido;
import br.com.itau.geradornotafiscal.service.GeradorNotaFiscalService;

@RestController
@RequestMapping("/api/pedido")
public class GeradorNFController {

	private final GeradorNotaFiscalService notaFiscalService;

	public GeradorNFController(GeradorNotaFiscalService notaFiscalService) {
		this.notaFiscalService = notaFiscalService;
	}

	@PostMapping("/gerarNotaFiscal")
	public ResponseEntity<NotaFiscal> gerarNotaFiscal(@RequestBody Pedido pedido) {
		NotaFiscal notaFiscal = notaFiscalService.gerarNotaFiscal(pedido);
		return ResponseEntity.status(HttpStatus.CREATED).body(notaFiscal);
	}
}