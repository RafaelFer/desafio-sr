package br.com.itau.geradornotafiscal.entrypoint.controller;

import br.com.itau.geradornotafiscal.model.NotaFiscal;
import br.com.itau.geradornotafiscal.model.Pedido;
import br.com.itau.geradornotafiscal.service.GeradorNotaFiscalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GeradorNFController.class)
class GeradorNFControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeradorNotaFiscalService notaFiscalService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveRetornar201AoGerarNotaFiscalComSucesso() throws Exception {
        Pedido pedido = Pedido.builder().idPedido(Integer.parseInt("123")).build();
        NotaFiscal notaFiscal = NotaFiscal.builder().idNotaFiscal("NF-1").build();

        when(notaFiscalService.gerarNotaFiscal(any(Pedido.class))).thenReturn(notaFiscal);

        mockMvc.perform(post("/api/pedido/gerarNotaFiscal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedido)))
                .andExpect(status().isCreated());
    }
}