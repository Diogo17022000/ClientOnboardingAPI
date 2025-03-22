Feature: Buscar Cliente por ID

  Scenario: Buscar cliente pelo ID fixo
    When eu envio uma requisição GET para buscar o cliente pelo ID fixo
    Then o sistema deve retornar o cliente com sucesso
