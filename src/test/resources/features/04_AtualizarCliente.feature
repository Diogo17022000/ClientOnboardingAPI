Feature: Atualizar Cliente

  Scenario: Atualizar os dados de um cliente com ID fixo
    When eu seleciono o cliente com ID 17
    And eu atualizo o nome do cliente adicionando "Cliente Teste"
    Then o sistema deve retornar a mensagem de sucesso "Cliente atualizado com sucesso!"
