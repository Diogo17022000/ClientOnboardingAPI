package com.ClientOnboardingAPI.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    private Map<String, Object> cliente; // Dados do cliente atual
    private ResponseEntity<String> response; // Resposta da requisição
    private Long clienteId; // ID do cliente consultado ou selecionado

    // Cenário de criação de cliente com dados dinâmicos
    @Given("um cliente válido com dados dinâmicos")
    public void umClienteValidoComDadosDinamicos() {
        Random random = new Random();
        cliente = new HashMap<>();
        cliente.put("cpf", String.format("%03d.%03d.%03d-%02d", random.nextInt(1000), random.nextInt(1000), random.nextInt(1000), random.nextInt(100)));
        cliente.put("nome", "Cliente Agi " + UUID.randomUUID().toString().substring(0, 5));
        cliente.put("dataNascimento", "1990-01-01");
        cliente.put("telefone", String.format("(%02d) %05d-%04d", random.nextInt(100), random.nextInt(100000), random.nextInt(10000)));
        cliente.put("endereco", "Rua Aleatória, " + random.nextInt(999));
    }

    // Cenário de criação de cliente com dados estáticos
    @Given("um cliente válido com os seguintes dados:")
    public void umClienteValidoComOsSeguintesDados(Map<String, String> dados) {
        cliente = new HashMap<>();
        cliente.put("cpf", dados.get("cpf"));
        cliente.put("nome", dados.get("nome"));
        cliente.put("dataNascimento", dados.get("dataNascimento"));
        cliente.put("telefone", dados.get("telefone"));
        cliente.put("endereco", dados.get("endereco"));
    }

    // Requisição POST para criar cliente
    @When("eu envio uma requisição POST para {string} com este cliente")
    public void euEnvioUmaRequisicaoPOSTParaComEsteCliente(String endpoint) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(cliente, headers);
        response = restTemplate.postForEntity(endpoint, request, String.class);
    }

    // Requisição GET para listar clientes
    @When("eu envio uma requisição GET para {string}")
    public void euEnvioUmaRequisicaoGETPara(String endpoint) {
        response = restTemplate.getForEntity(endpoint, String.class);
    }

    // Requisição GET para buscar cliente por ID fixo
    @When("eu envio uma requisição GET para buscar o cliente pelo ID fixo")
    public void euEnvioUmaRequisicaoGETParaBuscarOClientePeloIdFixo() {
        clienteId = 23L; // ID fixo
        response = restTemplate.getForEntity("/api/clientes/" + clienteId, String.class);
    }

    // Seleção do cliente com ID fixo para atualização
    @When("eu seleciono o cliente com ID 17")
    public void euSelecionoOClienteComID17() {
        clienteId = 17L; // Define o ID fixo para o cliente
    }

    // Requisição PUT para atualizar o nome do cliente
    @When("eu atualizo o nome do cliente adicionando {string}")
    public void euAtualizoONomeDoClienteAdicionando(String novoNome) {
        if (clienteId == null) {
            throw new IllegalStateException("Nenhum ID de cliente foi selecionado para atualização.");
        }

        // Atualiza o nome do cliente
        cliente = new HashMap<>();
        cliente.put("nome", novoNome + " atualizado");
        cliente.put("cpf", "123.456.789-00"); // CPF fictício
        cliente.put("dataNascimento", "1990-01-01");
        cliente.put("telefone", "(11) 98765-4321");
        cliente.put("endereco", "Rua Atualizada, 456");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(cliente, headers);
        response = restTemplate.exchange("/api/clientes/" + clienteId, org.springframework.http.HttpMethod.PUT, request, String.class);
    }

    // Validação do status de resposta
    @Then("o sistema deve retornar o código de status {int}")
    public void oSistemaDeveRetornarOCodigoDeStatus(int statusCode) {
        assertThat(response.getStatusCodeValue()).isEqualTo(statusCode);
    }

    // Validação do conteúdo da mensagem na resposta
    @Then("a resposta deve conter a mensagem {string}")
    public void aRespostaDeveConterAMensagem(String mensagemEsperada) {
        try {
            // Verifica se a resposta é JSON ou uma mensagem simples
            if (response.getBody().startsWith("{")) {
                // Trata como JSON
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());

                // Verifica se a chave "Clientes cadastrados" existe
                if (mensagemEsperada.equals("Clientes cadastrados:")) {
                    assertThat(rootNode.has("Clientes cadastrados")).isTrue();
                } else {
                    assertThat(response.getBody()).contains(mensagemEsperada);
                }
            } else {
                // Trata como mensagem simples
                assertThat(response.getBody()).contains(mensagemEsperada);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar a resposta JSON ou mensagem.", e);
        }
    }

    // Validação do conteúdo genérico da resposta
    @Then("a resposta deve conter {string}")
    public void aRespostaDeveConter(String mensagemEsperada) {
        assertThat(response.getBody()).contains(mensagemEsperada);
    }

    // Validação para cliente encontrado com sucesso
    @Then("o sistema deve retornar o cliente com sucesso")
    public void oSistemaDeveRetornarOClienteComSucesso() {
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).contains("Cliente encontrado:");
    }

    // Validação de sucesso para atualização
    @Then("o sistema deve retornar a mensagem de sucesso {string}")
    public void oSistemaDeveRetornarAMensagemDeSucesso(String mensagemEsperada) {
        assertThat(response.getBody()).contains(mensagemEsperada);
    }
}
