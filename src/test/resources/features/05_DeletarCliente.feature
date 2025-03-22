Feature: Deletar Cliente

  Scenario: Deletar um cliente existente
    When eu consulto a lista de clientes
    And eu seleciono um ID de cliente existente
    And eu envio uma requisição DELETE para deletar o cliente
    Then o sistema deve retornar a mensagem de sucesso "Cliente com ID {id} foi excluído com sucesso!"

  Scenario: Tentar deletar um cliente inexistente
    When eu seleciono o cliente com ID 9999
    And eu envio uma requisição DELETE para deletar o cliente
    Then o sistema deve retornar a mensagem de erro "Cliente com ID 9999 não encontrado. Nada foi excluído."
