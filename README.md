# Cálculo Numérico — Atividade Complementar (UNIVASF)

Aplicação interativa de console em **Java** (sem dependências externas) que implementa métodos numéricos clássicos para resolução de **Sistemas Lineares (Métodos Diretos e Iterativos)**, **Ajuste de Curvas (Mínimos Quadrados)** e **Integração Numérica**.

- **Instituição:** Fundação Universidade Federal do Vale do São Francisco (UNIVASF)
- **Disciplina:** Cálculo Numérico — Atividade Complementar (2025.2)
- **Docente:** Prof. Jorge Cavalcanti

---

## Sumário

1. [Visão Geral e Requisitos Funcionais](#1-visão-geral-e-requisitos-funcionais)
2. [Especificação Matemática dos Problemas](#2-especificação-matemática-dos-problemas)
3. [Arquitetura e Estrutura de Pastas](#3-arquitetura-e-estrutura-de-pastas)
4. [Como Compilar e Executar](#4-como-compilar-e-executar)
5. [Equipe](#5-equipe)

---

## 1. Visão Geral e Requisitos Funcionais

O sistema é centralizado em um menu interativo de linha de comando (`CLI`) onde o usuário pode:
- Selecionar qualquer um dos **4 módulos temáticos** da atividade;
- Escolher entre:
  1. **Executar com os dados padrão do exercício sorteado** (para validação imediata dos resultados); ou
  2. **Inserir novos dados manualmente** (permitindo resolver qualquer instância análoga do problema);
- Visualizar matrizes, tabelas de iteração e resultados numéricos formatados no terminal;
- Realizar múltiplos cálculos consecutivos em loop até optar por encerrar o programa.

---

## 2. Especificação Matemática dos Problemas

### Tópico 01 — Sistemas Lineares (Métodos Diretos): Problema 01.3 (Mineração)
- **Método:** Eliminação de Gauss com Pivoteamento Parcial e Substituição Regressiva.
- **Descrição:** Um engenheiro precisa extrair **4.800 m³** de areia, **5.800 m³** de cascalho fino e **5.700 m³** de cascalho grosso a partir de três minas com as seguintes composições percentuais:

| Material | Mina 1 (`x1`) | Mina 2 (`x2`) | Mina 3 (`x3`) | Demanda Total (m³) |
| :--- | :---: | :---: | :---: | :---: |
| **Areia** | 55% | 25% | 25% | 4.800 |
| **Cascalho Fino** | 30% | 45% | 20% | 5.800 |
| **Cascalho Grosso** | 15% | 30% | 55% | 5.700 |

- **Modelo Linear (`A * x = b`):**

```text
| 0.55   0.25   0.25 |   | x1 |   | 4800 |
| 0.30   0.45   0.20 | * | x2 | = | 5800 |
| 0.15   0.30   0.55 |   | x3 |   | 5700 |
```

Equações do sistema:
- `0,55 * x1 + 0,25 * x2 + 0,25 * x3 = 4800` (Areia)
- `0,30 * x1 + 0,45 * x2 + 0,20 * x3 = 5800` (Cascalho Fino)
- `0,15 * x1 + 0,30 * x2 + 0,55 * x3 = 5700` (Cascalho Grosso)

- **Saída esperada:** Volume (`x1, x2, x3`) em m³ a ser minerado de cada mina e matriz escalonada resultante.

---

### Tópico 02 — Sistemas Lineares (Métodos Iterativos): Problema 02.2 (Treliça)
- **Método:** Método Iterativo de Gauss-Seidel com verificação de convergência (Critério das Linhas / Critério de Sassenfeld).
- **Tolerância (Precisão):** `ε = 0,0001` (`10^-4`), utilizando o erro relativo máximo como critério de parada:

```text
Erro Relativo = max |x_i^(k) - x_i^(k-1)| / max |x_i^(k)| < 0.0001
```

- **Descrição:** Cálculo das forças internas nas barras e reações de apoio de uma treliça estaticamente determinada com juntas articuladas a partir das equações de equilíbrio estático (`ΣFx = 0` e `ΣFy = 0`), considerando ângulos de **45°**, **45°**, **60°** e **30°** e cargas externas verticais de **500** e **100**.
- **Saída esperada:** Análise de convergência, vetor solução das forças/tensões nas barras e número total de iterações até atingir a precisão `0,0001`.

---

### Tópico 03 — Interpolação e Ajuste de Curvas: Problema 03.1 (Lei de Moore)
- **Método:** Regressão Linear pelo Método dos Mínimos Quadrados (MMQ) com linearização logarítmica na base 10.
- **Modelo Matemático:**
  Como o crescimento do número de transistores `N` em função do ano `a` é exponencial, aplica-se a transformação `y = log10(N)` para ajustar a reta:

```text
y = b0 + b1 * a   ===>   log10(N) = b0 + b1 * a   ===>   N(a) = 10^(b0 + b1 * a)
```

- **Dados Padrão (Histórico de Processadores Intel):**

| Chip | Ano (`a`) | Número de Transistores (`N`) |
| :--- | :---: | :---: |
| **4004** | 1971 | 2.250 |
| **8008** | 1972 | 3.300 |
| **8080** | 1974 | 6.000 |
| **8086** | 1978 | 29.000 |
| **80286** | 1982 | 134.000 |
| **80386** | 1986 | 275.000 |
| **80486** | 1989 | 1.200.000 |
| **Pentium** | 1993 | 3.100.000 |
| **Pentium II** | 1997 | 7.500.000 |
| **Pentium III** | 1999 | 9.500.000 |
| **Pentium 4** | 2000 | 42.000.000 |

- **Saída esperada:**
  - Coeficientes da equação linearizada (`b0, b1`), coeficiente de determinação (`R²`) e erro quadrático residual;
  - Previsão do número de transistores para os anos de **2010** e **2020** (revertendo a transformação via `N = 10^y`).

---

### Tópico 04 — Integração Numérica: Problema 04.2 (Área da Superfície de um Rio)
- **Métodos:** Regra dos Trapézios Repetida e Regra 1/3 de Simpson Repetida.
- **Descrição:** Determinar a área superficial de um trecho de rio entre `x = 0 m` e `x = 40 m` com passo `h = 10 m` (`n = 4` subintervalos), conhecendo-se as distâncias de duas margens `M1(x)` e `M2(x)` em relação a uma linha de referência:

| Posição `x_i` (m) | 0 | 10 | 20 | 30 | 40 |
| :--- | :---: | :---: | :---: | :---: | :---: |
| **Margem 1 — `M1(x_i)` (m)** | 50,8 | 86,2 | 136,0 | 72,8 | 51,0 |
| **Margem 2 — `M2(x_i)` (m)** | 113,6 | 144,5 | 185,0 | 171,2 | 95,3 |
| **Largura — `f(x_i) = M2(x_i) - M1(x_i)` (m)** | 62,8 | 58,3 | 49,0 | 98,4 | 44,3 |

- **Fórmulas Compostas:**
  - **Regra dos Trapézios Repetida:**
    ```text
    I_T ≈ (h / 2) * [ f(x0) + 2 * (f(x1) + f(x2) + ... + f(x_{n-1})) + f(xn) ]
    ```
  - **Regra 1/3 de Simpson Repetida** (`n` par):
    ```text
    I_S ≈ (h / 3) * [ f(x0) + 4 * Σ f(x_ímpar) + 2 * Σ f(x_par) + f(xn) ]
    ```
- **Saída esperada:** Tabela de larguras `f(x_i)`, área estimada por cada método (em m²) e análise comparativa entre os resultados.

---

## 3. Arquitetura e Estrutura de Pastas

```text
calculo-numerico-atv2/
├── src/
│   ├── Main.java                 # Ponto de entrada, menu principal em loop e utilitários de I/O
│   ├── MetodosDiretos.java       # Tópico 01: Eliminação de Gauss (Problema 01.3 - Mineração)
│   ├── MetodosIterativos.java    # Tópico 02: Método de Gauss-Seidel (Problema 02.2 - Treliça)
│   ├── MinimosQuadrados.java     # Tópico 03: Regressão Linear MMQ (Problema 03.1 - Lei de Moore)
│   └── IntegracaoNumerica.java   # Tópico 04: Trapézios e Simpson 1/3 (Problema 04.2 - Área do Rio)
├── .gitignore
└── README.md
```

---

## 4. Como Compilar e Executar

### Pré-requisitos
- **Java Development Kit (JDK)** 11 ou superior instalado e configurado no `PATH`.

### Passo a passo no Terminal

1. Acesse a pasta raiz do repositório:
   ```bash
   cd calculo-numerico-atv2
   ```

2. Compile todos os arquivos-fonte gerando os `.class` em um diretório `bin/`:
   ```bash
   javac -encoding UTF-8 -d bin src/*.java
   ```

3. Execute a aplicação principal:
   ```bash
   java -cp bin Main
   ```

*(Alternativamente, dentro da pasta `src/`, basta rodar `javac *.java` seguido de `java Main`.)*

---

## 5. Equipe

| Integrante | Papel / Responsabilidade |
| :--- | :--- |
| **Vitor Santos** | Arquitetura base, integração e implementação de módulos numéricos |
| **Laedson Araújo** | Implementação e validação de módulos numéricos |
| **Myguel Luiz** | Implementação, testes de casos de borda e documentação |
