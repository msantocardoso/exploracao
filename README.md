# Projeto Exploracao

Para iniciar a aplicação basta executar a classe:

```
br.com.cespec.exploracao.application.ExploracaoApplication
```

API do Serviço de Exploração
----------------------------------------
http://localhost:8080/exploracao/iniciar/area

> * Serviço de inicialização da área de exploração.
	- POST: http://localhost:8080/exploracao/iniciar/area
```
{
"x": 5,
"y": 5
}
```
> * Exibir Área de Exploraçãoa
	- GET: http://localhost:8080/exploracao/exibir/area

> * Consultar Sonda Por ID
	- GET: http://localhost:8080/exploracao/sondas/{id}

> * Lista Sondas
	- GET: http://localhost:8080/exploracao/sondas

> * Cadastrar Sonda
	- POST: http://localhost:8080/exploracao/sondas
```
{
"posicao":{"x": 0,"y": 1},
"direcao": "E"
}
```

> * Remover Sonda
	- DELETE: http://localhost:8080/exploracao/sondas/{id}

> * Executar Instrução Individual
	- PUT: http://localhost:8080/exploracao/sondas/{id}/executar/instrucoes/{instrucoes} -> "LMLMLMLMM"

> * Executar Instruções
	- PUT: http://localhost:8080/exploracao/sondas/executar/instrucoes
```
[
{"sondaId": 1, "instrucoes": "LMLMLMLMM"},
{"sondaId": 2, "instrucoes": "MMRMMRMRRM"}
]
```