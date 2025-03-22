Feature: Criação de Cliente

  Scenario: Criar um cliente com sucesso
    Given um cliente válido com dados dinâmicos
    When eu envio uma requisição POST para "/api/clientes" com este cliente
    Then o sistema deve retornar o código de status 200
    And a resposta deve conter a mensagem "Cadastro realizado com sucesso!"

  Scenario: Tentar criar um cliente com CPF já cadastrado
    Given um cliente válido com os seguintes dados:
      | cpf           | 173.455.001-04   |
      | nome          | Diogo Silva 2    |
      | dataNascimento| 1990-01-01       |
      | telefone      | (51) 99999-7777  |
      | endereco      | Rua das Palmeiras, 456 |
    When eu envio uma requisição POST para "/api/clientes" com este cliente
    Then o sistema deve retornar o código de status 400
    And a resposta deve conter a mensagem "CPF já cadastrado!"
