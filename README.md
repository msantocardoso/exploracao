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
> * Exibir Área de Exploraçãoa
	- GET: https://exploracao.herokuapp.com/exploracao/exibir/area

> * Consultar Sonda Por ID
	- GET: https://exploracao.herokuapp.com/exploracao/sondas/{id}

> * Lista Sondas
	- GET: https://exploracao.herokuapp.com/exploracao/sondas

> * Cadastrar Sonda
	- POST: https://exploracao.herokuapp.com/exploracao/sondas
```
{
"posicao":{"x": 0,"y": 1},
"direcao": "E"
}
```

> * Remover Sonda
	- DELETE: https://exploracao.herokuapp.com/exploracao/sondas/{id}

> * Executar Instrução Individual
	- PUT: https://exploracao.herokuapp.com/exploracao/sondas/{id}/executar/instrucoes/{instrucoes} -> "LMLMLMLMM"

> * Executar Instruções
	- PUT: https://exploracao.herokuapp.com/exploracao/sondas/executar/instrucoes
```
[
{"sondaId": 1, "instrucoes": "LMLMLMLMM"},
{"sondaId": 2, "instrucoes": "MMRMMRMRRM"}
]
```