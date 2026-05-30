package br.com.itau.geradornotafiscal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync // Permite que o Spring processe o @Async
public class GeradorNotaFiscalApplication {
	public static void main(String[] args) {
		SpringApplication.run(GeradorNotaFiscalApplication.class, args);
	}
}