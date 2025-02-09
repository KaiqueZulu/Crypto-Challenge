# Desafio de Criptografia

Este projeto é a solução para um desafio de criptografia focado em implementar a criptografia para dados sensíveis de forma transparente para a API e para as camadas de serviço. O objetivo é garantir que os campos sensíveis dos objetos de entidade sejam criptografados em tempo de execução, durante a conversão da entidade para a coluna correspondente no banco de dados, e vice-versa.

## Descrição do Desafio

O desafio consiste em implementar a criptografia para campos sensíveis, como `userDocument` e `creditCardToken`, garantindo que esses campos sejam armazenados e transmitidos de forma segura. A aplicação deve criptografar e descriptografar esses campos de maneira transparente ao interagir com o banco de dados, mantendo-os ocultos de acessos não autorizados.

### Exemplo

Considere os seguintes campos como sensíveis:

- `userDocument`
- `creditCardToken`

Os dados sensíveis podem aparecer da seguinte forma no banco de dados (criptografados):

| id  | userDocument     | creditCardToken | value |
|-----|------------------|-----------------|-------|
| 1   | MzYxNDA3ODE4MzM= | YWJjMTIz        | 5999  |
| 2   | MzI5NDU0MTA1ODM= | eHl6NDU2         | 1000  |
| 3   | NzYwNzc0NTIzODY= | Nzg5eHB0bw==     | 1500  |

A estrutura da entidade correspondente seria a seguinte:

| Campo            | Tipo   |
|------------------|--------|
| id               | Long   |
| userDocument     | String |
| creditCardToken  | String |
| value            | Long   |

## Requisitos

- Implemente um sistema simples de CRUD considerando os campos mencionados como sensíveis.
- Utilize um algoritmo de criptografia simétrica ou assimétrica de sua escolha (AES para criptografia simétrica ou RSA para criptografia assimétrica).

## Tecnologias Utilizadas

- Java
- AES ou RSA (dependendo da escolha de implementação)

## Licença

Este projeto é licenciado sob a **MIT License** - veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## Créditos

Este desafio foi baseado no repositório [backend-br/desafios](https://github.com/backend-br/desafios/blob/master/cryptography/PROBLEM.md), e você pode encontrar mais detalhes sobre o desafio no link fornecido.
