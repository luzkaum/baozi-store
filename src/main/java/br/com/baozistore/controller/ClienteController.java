package br.com.baozistore.controller;

import br.com.baozistore.model.Cliente;
import br.com.baozistore.repository.ClienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteRepository clienteRepository;

    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // POST /clientes - criar
    @PostMapping
    public ResponseEntity<Cliente> criar(@RequestBody Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo nome e obrigatorio");
        }
        cliente.setId(null); // garante que sera um INSERT
        Cliente salvo = clienteRepository.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // GET /clientes - listar todos
    @GetMapping
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    // GET /clientes/{id} - consultar por ID
    @GetMapping("/{id}")
    public Cliente buscarPorId(@PathVariable Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente nao encontrado: " + id));
    }

    // PUT /clientes/{id} - atualizar (opcional)
    @PutMapping("/{id}")
    public Cliente atualizar(@PathVariable Long id, @RequestBody Cliente dados) {
        Cliente cliente = buscarPorId(id);
        if (dados.getNome() != null && !dados.getNome().isBlank()) {
            cliente.setNome(dados.getNome());
        }
        if (dados.getClienteDesde() != null) {
            cliente.setClienteDesde(dados.getClienteDesde());
        }
        return clienteRepository.save(cliente);
    }

    // DELETE /clientes/{id} - apagar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente nao encontrado: " + id);
        }
        clienteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
