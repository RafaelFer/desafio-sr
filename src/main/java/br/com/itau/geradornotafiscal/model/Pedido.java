package br.com.itau.geradornotafiscal.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Pedido {
	
		@JsonProperty("id_pedido")
	    private int idPedido;

		@JsonProperty("data")
	    private LocalDate data;

		@JsonProperty("valor_total_itens")
	    private BigDecimal valorTotalItens;

		@JsonProperty("valor_frete")
	    private BigDecimal valorFrete;

		@JsonProperty("itens")
	    private List<Item> itens;

		@JsonProperty("destinatario")
	    private Destinatario destinatario;

}
