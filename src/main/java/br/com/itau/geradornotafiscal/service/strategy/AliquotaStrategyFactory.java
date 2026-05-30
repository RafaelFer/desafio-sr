package br.com.itau.geradornotafiscal.service.strategy;

import java.util.Map;
import org.springframework.stereotype.Component;
import br.com.itau.geradornotafiscal.model.Destinatario;
import br.com.itau.geradornotafiscal.model.RegimeTributacaoPJ;

@Component
public class AliquotaStrategyFactory {

    private final Map<String, AliquotaStrategy> strategies;

    public AliquotaStrategyFactory(Map<String, AliquotaStrategy> strategies) {
        this.strategies = strategies;
    }

    public AliquotaStrategy obterEstrategia(Destinatario destinatario) {
        // Se o destinatário ou o tipo de pessoa vier nulo, já corta o erro no início
        if (destinatario == null || destinatario.getTipoPessoa() == null) {
            throw new IllegalArgumentException("Dados do destinatário ou Tipo de Pessoa não podem ser nulos.");
        }

        // Adicionado proteção de exaustividade aqui também
        String beanName = switch (destinatario.getTipoPessoa()) {
            case FISICA -> "calcularAliquotaPessoaFisicaService";
            case JURIDICA -> obterRegimePJ(destinatario.getRegimeTributacao());
            default -> throw new IllegalArgumentException("Tipo de pessoa não suportado: " + destinatario.getTipoPessoa());
        };

        AliquotaStrategy strategy = strategies.get(beanName);
        if (strategy == null) {
            throw new IllegalArgumentException("Estratégia fiscal não encontrada para o componente: " + beanName);
        }
        return strategy;
    }

    private String obterRegimePJ(RegimeTributacaoPJ regime) {
        if (regime == null) {
            throw new IllegalArgumentException("Regime de tributação PJ não pode ser nulo.");
        }

        return switch (regime) {
            case SIMPLES_NACIONAL -> "calcularAliquotaPJSimplesNacionalService";
            case LUCRO_REAL -> "calcularAliquotaPJLucroRealService";
            case LUCRO_PRESUMIDO -> "calcularAliquotaPJLucroPresumidoService";
            default -> throw new IllegalArgumentException("Regime de tributação não suportado ou inválido: " + regime);
        };
    }
}