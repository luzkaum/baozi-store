package br.com.baozistore.dto;

/**
 * Corpo JSON recebido no POST/PUT de pedidos:
 * { "clienteId": 1, "produtoId": 1, "quantidade": 5 }
 */
public record PedidoRequest(Long clienteId, Long produtoId, Integer quantidade) {
}
