package com.ClientOnboardingAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ClientOnboardingAPI.model.Cliente;
import com.ClientOnboardingAPI.service.ClienteService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<String> criarCliente(@RequestBody Cliente cliente) {
        try {
            Cliente novoCliente = clienteService.criarCliente(cliente);
            return ResponseEntity.ok("Cadastro realizado com sucesso! ID do cliente: " + novoCliente.getId());
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage()); // Retorna a mensagem "CPF já cadastrado!"
        }
    }

    @GetMapping
    public ResponseEntity<String> listarClientes() {
        List<Cliente> clientes = clienteService.listarClientes();
        if (clientes.isEmpty()) {
            return ResponseEntity.ok("Nenhum cliente cadastrado até o momento.");
        }
        return ResponseEntity.ok("Clientes cadastrados: " + clientes.toString());
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> buscarClientePorId(@PathVariable Long id) {
        return clienteService.buscarClientePorId(id)
                .map(cliente -> ResponseEntity.ok("Cliente encontrado: " + cliente.toString()))
                .orElse(ResponseEntity.status(404).body("Cliente com ID " + id + " não encontrado!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarCliente(@PathVariable Long id, @RequestBody Cliente clienteAtualizado) {
        try {
            Cliente cliente = clienteService.atualizarCliente(id, clienteAtualizado);
            return ResponseEntity.ok("Cliente atualizado com sucesso! Novos dados: " + cliente.toString());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Cliente com ID " + id + " não encontrado. Não foi possível atualizar.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarCliente(@PathVariable Long id) {
        try {
            clienteService.deletarCliente(id);
            return ResponseEntity.ok("Cliente com ID " + id + " foi excluído com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Cliente com ID " + id + " não encontrado. Nada foi excluído.");
        }
    }
}
