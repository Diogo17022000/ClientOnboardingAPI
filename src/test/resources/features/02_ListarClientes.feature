Feature: Listar Clientes

  Scenario: Listar clientes com sucesso
    When eu envio uma requisição GET para "/api/clientes"
    Then o sistema deve retornar o código de status 200
    And a resposta deve conter "Clientes cadastrados:"
