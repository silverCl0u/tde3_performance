# Trabalho de Sistemas Operacionais: Concorrência e Deadlock

**Instituição:** PUCPR  
**Curso:** Ciência da Computação 
**Disciplina:** Performance em sistemas ciberfisicos  

**Integrantes do Grupo:**
* [Rafael Gomes]
* [Erick Meister]

---

## 🎥 Apresentação em Vídeo
**Link para o vídeo explicativo:** []

---

## 🧠 Parte 1: O Jantar dos Filósofos

### O Problema
O Jantar dos Filósofos é um cenário clássico que ilustra problemas de sincronização. Cinco filósofos sentam-se ao redor de uma mesa circular. Cada um precisa de dois garfos (um à esquerda e um à direita) para comer.
No protocolo ingênuo, se todos os filósofos pegarem o garfo da esquerda simultaneamente, ocorre um **Deadlock** (impasse), pois todos ficarão esperando o garfo da direita, que está ocupado pelo vizinho. Ninguém come, ninguém solta o garfo.

### Solução Adotada: Hierarquia de Recursos
Para resolver o problema e evitar o Deadlock, utilizamos a estratégia de **Hierarquia de Recursos**.
* **Lógica:** Numeramos os garfos de 0 a 4. A regra imposta é que todo filósofo deve, obrigatoriamente, tentar pegar primeiro o garfo de **menor índice** e depois o de **maior índice**.
* **Por que funciona?** Isso quebra a condição de "Espera Circular" (uma das 4 condições de Coffman). Matematicamente, impede que o ciclo de dependência se feche, garantindo que pelo menos um filósofo consiga comer e liberar os recursos.

### Pseudocódigo / 
Abaixo está a lógica implementada para garantir que não exista espera circular:


DADOS:
    N = 5 (número de filósofos)
    Garfos = Array indexado de 0 a 4

PARA CADA FILÓSOFO (p):
    1. Identificar garfos vizinhos:
       - garfo_esq = p
       - garfo_dir = (p + 1) % N

    2. Definir ordem de aquisição (A REGRA DE OURO):
       - primeiro_garfo = min(garfo_esq, garfo_dir)  (O de menor índice)
       - segundo_garfo  = max(garfo_esq, garfo_dir)  (O de maior índice)

    3. Loop Principal:
       REPITA:
           PENSAR()
           estado[p] <- "COM FOME"
           
           ADQUIRIR(primeiro_garfo)  // Bloqueia se ocupado
           ADQUIRIR(segundo_garfo)   // Bloqueia se ocupado
           
           estado[p] <- "COMENDO"
           COMER()
           
           LIBERAR(segundo_garfo)
           LIBERAR(primeiro_garfo)
           
           estado[p] <- "PENSANDO"

---

## 🚦 Parte 2: Threads e Semáforos (Condição de Corrida)

### O Desafio
Demonstrar uma **Condição de Corrida** (Race Condition) onde múltiplas threads incrementam um contador compartilhado sem sincronização, resultando em perda de dados. Em seguida, corrigir o problema.

### Implementação
1.  **CorridaSemControle.java:** Múltiplas threads incrementam uma variável estática. Como a operação não é atômica, o resultado final é inconsistente (menor que o esperado).
2.  **CorridaComSemaphore.java:** Utilizamos a classe `java.util.concurrent.Semaphore` inicializada com 1 permissão (`new Semaphore(1, true)`).
    * Isso atua como um **Mutex** (Exclusão Mútua).
    * O parâmetro `true` garante **Justiça (Fairness)**, ou seja, as threads são atendidas em ordem de chegada (FIFO), evitando *Starvation*.

**Resultado:** A versão com Semáforo atinge o valor exato esperado, ao custo de um leve aumento no tempo de execução devido ao overhead de sincronização.

---

## 🔒 Parte 3: Deadlock e Correção

### O Cenário
Criamos um cenário intencional de travamento com duas threads e dois recursos (`LOCK_A` e `LOCK_B`).
* **Thread 1:** Pega A -> Espera B.
* **Thread 2:** Pega B -> Espera A.

Isso satisfaz as 4 Condições de Coffman para Deadlock:
1.  Exclusão Mútua.
2.  Manter e Esperar (Hold and Wait).
3.  Não Preempção.
4.  Espera Circular.

### A Correção
No arquivo `DeadlockFixed.java`, aplicamos a solução por **Ordenação de Recursos** (similar aos filósofos).
* Alteramos a lógica para que **ambas** as threads requisitem os recursos na mesma ordem global (sempre pegar A antes de B).
* Isso elimina a possibilidade de Espera Circular, permitindo que o programa execute até o fim com sucesso.

---


