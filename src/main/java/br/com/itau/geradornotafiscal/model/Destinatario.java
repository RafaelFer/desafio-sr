package br.com.itau.geradornotafiscal.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Destinatario {
	@NonNull
	@JsonProperty("nome")
	private String nome;

	@NonNull
	@JsonProperty("tipo_pessoa")
	private TipoPessoa tipoPessoa;

	@JsonProperty("regime_tributacao")
	private RegimeTributacaoPJ regimeTributacao;

	@NonNull
	@JsonProperty("documentos")
	private List<Documento> documentos;

	@NonNull
	@JsonProperty("enderecos")
	private List<Endereco> enderecos;

}




