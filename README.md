# Projeto Exploracao

Para iniciar a aplicação basta executar a classe:

```
br.com.cespec.exploracao.application.ExploracaoApplication
```

API do Serviço de Exploração
----------------------------------------
https://exploracao.herokuapp.com/exploracao/iniciar/area

> * Serviço de inicialização da área de exploração.
	- POST: https://exploracao.herokuapp.com/exploracao/iniciar/area
```
{
"x": 5,
"y": 5
}
```

curl -X POST -u 'admin:123' -H 'Content-Type: application/json' -d '{"x": 5,"y": 5}' https://exploracao.herokuapp.com/exploracao/iniciar/area

> * Exibir Área de Exploração
	- GET: https://exploracao.herokuapp.com/exploracao/exibir/area

curl -i -u 'admin:123' https://exploracao.herokuapp.com/exploracao/exibir/area

> * Cadastrar Sonda
	- POST: https://exploracao.herokuapp.com/exploracao/sondas
```
{
"posicao":{"x": 1,"y": 2},
"direcao": "N"
}
```

curl -X POST -u 'admin:123' -H 'Content-Type: application/json' -d '{"posicao":{"x": 0,"y": 1},"direcao": "E"}' http://localhost:8080/exploracao/sondas

curl -X POST -u 'admin:123' -H 'Content-Type: application/json' -d '{"posicao":{"x": 1,"y": 2},"direcao": "N"}' https://exploracao.herokuapp.com/exploracao/sondas

> * Lista Sondas
	- GET: https://exploracao.herokuapp.com/exploracao/sondas

curl -i -u 'admin:123' http://localhost:8080/exploracao/sondas

curl -i -u 'admin:123' https://exploracao.herokuapp.com/exploracao/sondas

> * Consultar Sonda Por ID
	- GET: https://exploracao.herokuapp.com/exploracao/sondas/{id}

curl -i -u 'admin:123' http://localhost:8080/exploracao/sondas/{id}

curl -i -u 'admin:123' https://exploracao.herokuapp.com/exploracao/sondas/{id}


> * Remover Sonda
	- DELETE: https://exploracao.herokuapp.com/exploracao/sondas/{id}

curl -X DELETE -u 'admin:123' http://localhost:8080/exploracao/sondas/{id}

curl -X DELETE -u 'admin:123' https://exploracao.herokuapp.com/exploracao/sondas/{id}

> * Executar Instrução Individual
	- PUT: https://exploracao.herokuapp.com/exploracao/sondas/{id}/executar/instrucoes/{instrucoes} -> "LMLMLMLM"

curl -X PUT -u 'admin:123' http://localhost:8080/exploracao/sondas/{id}/executar/instrucoes/{instrucoes} -> "LMLMLMLM"

curl -X PUT -u 'admin:123' https://exploracao.herokuapp.com/exploracao/sondas/{id}/executar/instrucoes/{instrucoes} -> "LMLMLMLMM"

> * Executar Instruções
	- PUT: https://exploracao.herokuapp.com/exploracao/sondas/executar/instrucoes
```
[
{"sondaId": 1, "instrucoes": "LMLMLMLMM"},
{"sondaId": 2, "instrucoes": "MMRMMRMRRM"}
]
```

curl -X PUT -u 'admin:123' -d '[{"sondaId": 1, "instrucoes": "LMLMLMLMM"},{"sondaId": 2, "instrucoes": "MMRMMRMRRM"}]' http://localhost:8080/exploracao/sondas/executar/instrucoes

curl -X PUT -u 'admin:123' -d '[{"sondaId": 1, "instrucoes": "LMLMLMLMM"}, {"sondaId": 2, "instrucoes": "MMRMMRMRRM"}]' https://exploracao.herokuapp.com/exploracao/sondas/executar/instrucoes


LINKS:
===

http://www.baeldung.com/2011/11/20/basic-and-digest-authentication-for-a-restful-service-with-spring-security-3-1/

https://dzone.com/articles/basic-and-digest

http://cadocruz.com.br/seguran%C3%A7a/2015/02/28/seguranca-autenticacao-digest-spring-security-servicos-restful.html
