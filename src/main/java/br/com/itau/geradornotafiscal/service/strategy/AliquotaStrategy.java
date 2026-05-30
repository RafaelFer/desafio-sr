package br.com.itau.geradornotafiscal.service.strategy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.itau.geradornotafiscal.model.Item;
import br.com.itau.geradornotafiscal.model.ItemNotaFiscal;
import br.com.itau.geradornotafiscal.model.Pedido;

public interface AliquotaStrategy {

    List<ItemNotaFiscal> calcularAlicotaProduto(Pedido pedido);

    default List<ItemNotaFiscal> calcularAliquota(List<Item> items, BigDecimal aliquotaPercentual) {
        List<ItemNotaFiscal> itemNotaFiscalList = new ArrayList<>();
        for (Item item : items) {
            BigDecimal valorTributo = item.getValorUnitario().multiply(aliquotaPercentual);

            ItemNotaFiscal itemNotaFiscal = ItemNotaFiscal.builder()
                    .idItem(item.getIdItem())
                    .descricao(item.getDescricao())
                    .valorUnitario(item.getValorUnitario())
                    .quantidade(item.getQuantidade())
                    .valorTributoItem(valorTributo)
                    .build();
            itemNotaFiscalList.add(itemNotaFiscal);
        }
        return itemNotaFiscalList;
    }
}