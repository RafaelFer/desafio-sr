package br.com.itau.geradornotafiscal.service.strategy;

import br.com.itau.geradornotafiscal.model.Destinatario;
import br.com.itau.geradornotafiscal.model.RegimeTributacaoPJ;
import br.com.itau.geradornotafiscal.model.TipoPessoa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AliquotaStrategyFactoryTest {

    @Mock
    private AliquotaStrategy pessoaFisicaService;
    @Mock
    private AliquotaStrategy simplesNacionalService;

    private AliquotaStrategyFactory factory;

    @BeforeEach
    void setUp() {
        // Injetando o mapa com os mocks
        Map<String, AliquotaStrategy> strategies = Map.of(
                "calcularAliquotaPessoaFisicaService", pessoaFisicaService,
                "calcularAliquotaPJSimplesNacionalService", simplesNacionalService
        );
        factory = new AliquotaStrategyFactory(strategies);
    }

    @Test
    void deveRetornarEstrategiaPessoaFisica() {
        // Precisamos preencher os campos @NonNull
        Destinatario destinatario = Destinatario.builder()
                .nome("Teste")
                .tipoPessoa(TipoPessoa.FISICA)
                .documentos(Collections.emptyList()) // Adicionado
                .enderecos(Collections.emptyList())  // Adicionado
                .build();

        AliquotaStrategy strategy = factory.obterEstrategia(destinatario);

        assertEquals(pessoaFisicaService, strategy);
    }

    @Test
    void deveRetornarEstrategiaPJSimplesNacional() {
        Destinatario destinatario = Destinatario.builder()
                .nome("Teste PJ")
                .tipoPessoa(TipoPessoa.JURIDICA)
                .regimeTributacao(RegimeTributacaoPJ.SIMPLES_NACIONAL)
                .documentos(Collections.emptyList()) // Adicionado
                .enderecos(Collections.emptyList())  // Adicionado
                .build();

        AliquotaStrategy strategy = factory.obterEstrategia(destinatario);

        assertEquals(simplesNacionalService, strategy);
    }

    @Test
    void deveLancarExcecaoQuandoDestinatarioNulo() {
        assertThrows(IllegalArgumentException.class, () -> factory.obterEstrategia(null));
    }

    @Test
    void deveLancarExcecaoQuandoEstrategiaNaoEncontrada() {
        // Agora usamos o método auxiliar que garante que o objeto seja criado
        Destinatario destinatario = criarDestinatarioValido(TipoPessoa.JURIDICA, RegimeTributacaoPJ.LUCRO_REAL);

        // Se o seu MAP no setUp não contiver "calcularAliquotaPJLucroRealService",
        // a exceção ocorrerá corretamente sem o erro de NullPointerException do Lombok
        assertThrows(IllegalArgumentException.class, () -> factory.obterEstrategia(destinatario));
    }

    private Destinatario criarDestinatarioValido(TipoPessoa tipo, RegimeTributacaoPJ regime) {
        return Destinatario.builder()
                .nome("Nome Teste") // Preenchido para atender @NonNull
                .tipoPessoa(tipo)    // Preenchido para atender @NonNull
                .regimeTributacao(regime)
                .documentos(java.util.Collections.emptyList()) // Preenchido para atender @NonNull
                .enderecos(java.util.Collections.emptyList())  // Preenchido para atender @NonNull
                .build();
    }
}