package br.com.baozistore.controller;

import br.com.baozistore.dto.PedidoRequest;
import br.com.baozistore.model.Cliente;
import br.com.baozistore.model.Pedido;
import br.com.baozistore.model.Produto;
import br.com.baozistore.repository.ClienteRepository;
import br.com.baozistore.repository.PedidoRepository;
import br.com.baozistore.repository.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoController(PedidoRepository pedidoRepository,
                            ClienteRepository clienteRepository,
                            ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    // POST /pedidos - criar (body: clienteId, produtoId, quantidade)
    @PostMapping
    public ResponseEntity<Pedido> criar(@RequestBody PedidoRequest req) {
        Pedido pedido = preencher(new Pedido(), req);
        Pedido salvo = pedidoRepository.save(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // GET /pedidos - listar todos
    @GetMapping
    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    // GET /pedidos/{id} - consultar por ID
    @GetMapping("/{id}")
    public Pedido buscarPorId(@PathVariable Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido nao encontrado: " + id));
    }

    // PUT /pedidos/{id} - atualizar (opcional)
    @PutMapping("/{id}")
    public Pedido atualizar(@PathVariable Long id, @RequestBody PedidoRequest req) {
        Pedido pedido = buscarPorId(id);
        return pedidoRepository.save(preencher(pedido, req));
    }

    // DELETE /pedidos/{id} - apagar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido nao encontrado: " + id);
        }
        pedidoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Valida o corpo da requisicao e busca o cliente e o produto no banco
    private Pedido preencher(Pedido pedido, PedidoRequest req) {
        if (req.clienteId() == null || req.produtoId() == null
                || req.quantidade() == null || req.quantidade() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Informe clienteId, produtoId e quantidade (maior que zero)");
        }
        Cliente cliente = clienteRepository.findById(req.clienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente nao encontrado: " + req.clienteId()));
        Produto produto = produtoRepository.findById(req.produtoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Produto nao encontrado: " + req.produtoId()));
        if (!Boolean.TRUE.equals(produto.getEstoque())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Produto sem estoque: " + produto.getNome());
        }
        pedido.setCliente(cliente);
        pedido.setProduto(produto);
        pedido.setQuantidade(req.quantidade());
        return pedido;
    }
}
