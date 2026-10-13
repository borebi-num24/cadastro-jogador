# Cadastro de Jogadores do Santos

CRUD com Spring Boot + JPA/Hibernate + H2.

    ./mvnw spring-boot:run

- API: http://localhost:8080/jogador
- Console H2: http://localhost:8080/h2-console  (JDBC URL `jdbc:h2:mem:santosdb`, user `sa`, senha vazia)
- Exemplos de chamadas: `requests.http` (IntelliJ) ou use o Postman/curl.

## Camadas
| Pacote | Papel |
|---|---|
| `controller` | Controlador REST: recebe/responde requisições HTTP (JSON) |
| `business` | Service: regras de negócio e transação (`@Transactional`) |
| `business.converter` | Converte DTO <-> entidade (inclui a mesclagem da atualização parcial) |
| `business.dto` | DTOs (`record`) entre controller e service |
| `infraestructure.entitys` | Entidades JPA (`Jogador`, `Posicao`) |
| `infraestructure.repository` | Acesso a dados (Spring Data JPA) |
| `infraestructure.exceptions` | Exceções e tratamento global de erros |

## Endpoints
| Método | URL | Resposta |
|---|---|---|
| POST | /jogador | 201 + Location |
| GET | /jogador?nome=&posicao= | 200 (lista) |
| GET | /jogador/{id} | 200 / 404 |
| GET | /jogador/camisa/{numero} | 200 / 404 |
| PUT | /jogador/{id} | 200 / 404 / 409 (atualização completa) |
| PATCH | /jogador/{id} | 200 / 404 / 409 (só os campos enviados) |
| DELETE | /jogador/{id} | 204 / 404 |
| DELETE | /jogador/camisa/{numero} | 204 / 404 |

Posições: GOLEIRO, ZAGUEIRO, LATERAL, VOLANTE, MEIA, ATACANTE.
Regra de negócio: o número da camisa não pode se repetir (409).
