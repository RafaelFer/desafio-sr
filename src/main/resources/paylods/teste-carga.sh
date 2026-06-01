#!/bin/bash

URL="http://localhost:8080/api/pedido/gerarNotaFiscal"
PAYLOAD='{"id_pedido": 1, "data": "2022-05-01", "valor_total_itens": 5840.0, "valor_frete": 72.0, "itens": [{"id_item": 1, "descricao": "Monitor LCD SAMSUNG", "valor_unitario": 730, "quantidade": 8}], "destinatario": {"nome": "John Doe", "tipo_pessoa": "JURIDICA", "regime_tributacao": "SIMPLES_NACIONAL", "documentos": [{"tipo": "CNPJ", "numero": "49695613000180"}], "enderecos": [{"logradouro": "Av do estado", "numero": "5533", "complemento": "4 anndar b", "bairro": "Mooca", "cidade": "Sao Paulo", "estado": "SP", "pais": "Brasil", "cep": "03105003", "finalidade": "ENTREGA", "regiao": "SUDESTE"}]}}'
LOG_FILE="curl_results.log"

> "$LOG_FILE"

TPS_ALVO=20
DURACAO_SEGUNDOS=25 # 25 segundos a 20 TPS vai dar as suas 500 requisições totais

TOTAL_REQUISICOES=$((TPS_ALVO * DURACAO_SEGUNDOS))

echo "----------------------------------------"
echo "  Iniciando Teste de Carga Constante..."
echo "  Alvo: $TPS_ALVO TPS (Requisições por segundo)"
echo "  Duração: $DURACAO_SEGUNDOS segundos"
echo "  Total planejado: $TOTAL_REQUISICOES requisições"
echo "----------------------------------------"

START_TIME=$(date +%s%3N)

for ((sec=1; sec<=DURACAO_SEGUNDOS; sec++)); do
    LOOP_START=$(date +%s%3N)

    # Dispara exatamente 20 requisições em background (assíncronas) neste segundo
    for ((req=1; req<=TPS_ALVO; req++)); do
        curl -s -X POST "$URL" \
            -H "Content-Type: application/json" \
            -H "User-Agent: insomnia/12.6.0" \
            -d "$PAYLOAD" \
            -o /dev/null -w "%{http_code} " >> "$LOG_FILE" &
    done

    # Sincroniza o tempo para garantir que o próximo lote só saia quando completar 1 segundo
    LOOP_END=$(date +%s%3N)
    ELAPSED=$((LOOP_END - LOOP_START))
    SLEEP_TIME=$((1000 - ELAPSED))

    if [ $SLEEP_TIME -gt 0 ]; then
        # Converte milissegundos para segundos para o comando sleep do bash
        sleep $(awk "BEGIN {print $SLEEP_TIME/1000}")
    fi
done

# Aguarda os últimos curls em background terminarem de responder antes de calcular o resultado
wait

END_TIME=$(date +%s%3N)
DURATION=$((END_TIME - START_TIME))
DURATION_SEC=$((DURATION / 1000))

if [ $DURATION_SEC -eq 0 ]; then DURATION_SEC=1; fi

# Contagem precisa dos resultados
TOTAL_200_201=$(awk 'BEGIN {RS=" "} $1=="200" || $1=="201" {c++} END {print c+0}' "$LOG_FILE")
TOTAL_500=$(awk 'BEGIN {RS=" "} $1=="500" {c++} END {print c+0}' "$LOG_FILE")
TOTAL_400=$(awk 'BEGIN {RS=" "} $1=="400" {c++} END {print c+0}' "$LOG_FILE")
TOTAL_000=$(awk 'BEGIN {RS=" "} $1=="000" || $1=="" {c++} END {print c+0}' "$LOG_FILE")

rm -f "$LOG_FILE"

echo -e "\n----------------------------------------"
echo "           TESTE CONCLUÍDO!             "
echo "----------------------------------------"
echo "Total de Requisições: $TOTAL_REQUISICOES"
echo "Sucessos (200/201):     $TOTAL_200_201"
echo "Erros do Servidor (500): $TOTAL_500"
echo "Bad Requests (400):     $TOTAL_400"
echo "Falhas de Conexão (000): $TOTAL_000"
echo "----------------------------------------"
echo "Tempo total executado: $DURATION_SEC segundos"
echo "Vazão Real do Teste:    $(($TOTAL_REQUISICOES / DURATION_SEC)) TPS"
echo "----------------------------------------"