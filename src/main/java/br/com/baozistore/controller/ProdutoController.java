package br.com.baozistore.controller;

import br.com.baozistore.model.Produto;
import br.com.baozistore.repository.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoRepository produtoRepository;

    public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    // POST /produtos - criar
    @PostMapping
    public ResponseEntity<Produto> criar(@RequestBody Produto produto) {
        if (produto.getNome() == null || produto.getNome().isBlank() || produto.getPreco() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Os campos nome e preco sao obrigatorios");
        }
        if (produto.getEstoque() == null) {
            produto.setEstoque(Boolean.TRUE);
        }
        produto.setId(null); // garante que sera um INSERT
        Produto salvo = produtoRepository.save(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // GET /produtos - listar todos
    @GetMapping
    public List<Produto> listar() {
        return produtoRepository.findAll();
    }

    // GET /produtos/{id} - consultar por ID
    @GetMapping("/{id}")
    public Produto buscarPorId(@PathVariable Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto nao encontrado: " + id));
    }

    // PUT /produtos/{id} - atualizar (opcional)
    @PutMapping("/{id}")
    public Produto atualizar(@PathVariable Long id, @RequestBody Produto dados) {
        Produto produto = buscarPorId(id);
        if (dados.getNome() != null && !dados.getNome().isBlank()) {
            produto.setNome(dados.getNome());
        }
        if (dados.getPreco() != null) {
            produto.setPreco(dados.getPreco());
        }
        if (dados.getEstoque() != null) {
            produto.setEstoque(dados.getEstoque());
        }
        return produtoRepository.save(produto);
    }

    // DELETE /produtos/{id} - apagar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto nao encontrado: " + id);
        }
        produtoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
