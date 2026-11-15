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
- **Descrição:** Um engenheiro precisa extrair $4.800\text{ m}^3$ de areia, $5.800\text{ m}^3$ de cascalho fino e $5.700\text{ m}^3$ de cascalho grosso a partir de três minas com as seguintes composições percentuais:

| Material | Mina 1 ($x_1$) | Mina 2 ($x_2$) | Mina 3 ($x_3$) | Demanda Total ($\text{m}^3$) |
| :--- | :---: | :---: | :---: | :---: |
| **Areia** | $55\%$ | $25\%$ | $25\%$ | $4.800$ |
| **Cascalho Fino** | $30\%$ | $45\%$ | $20\%$ | $5.800$ |
| **Cascalho Grosso** | $15\%$ | $30\%$ | $55\%$ | $5.700$ |

- **Modelo Linear ($Ax = b$):**
  $$
  \begin{bmatrix}
  0{,}55 & 0{,}25 & 0{,}25 \\
  0{,}30 & 0{,}45 & 0{,}20 \\
  0{,}15 & 0{,}30 & 0{,}55
  \end{bmatrix}
  \begin{bmatrix}
  x_1 \\ x_2 \\ x_3
  \end{bmatrix}
  =
  \begin{bmatrix}
  4800 \\ 5800 \\ 5700
  \end{bmatrix}
  $$
- **Saída esperada:** Volume ($x_1, x_2, x_3$) em $\text{m}^3$ a ser minerado de cada mina e matriz escalonada resultante.

---

### Tópico 02 — Sistemas Lineares (Métodos Iterativos): Problema 02.2 (Treliça)
- **Método:** Método Iterativo de Gauss-Seidel com verificação de convergência (Critério das Linhas / Critério de Sassenfeld).
- **Tolerância (Precisão):** $\epsilon = 0{,}0001$ ($10^{-4}$), utilizando erro relativo máximo:
  $$
  \frac{\max_i |x_i^{(k)} - x_i^{(k-1)}|}{\max_i |x_i^{(k)}|} < \epsilon
  $$
- **Descrição:** Cálculo das forças internas nas barras e reações de apoio de uma treliça estaticamente determinada com juntas articuladas a partir das equações de equilíbrio estático ($\sum F_x = 0$ e $\sum F_y = 0$), considerando ângulos de $45^\circ$, $45^\circ$, $60^\circ$ e $30^\circ$ e cargas externas verticais de $500$ e $100$.
- **Saída esperada:** Análise de convergência, vetor solução das forças/tensões nas barras e número total de iterações até atingir $\epsilon = 0{,}0001$.

---

### Tópico 03 — Interpolação e Ajuste de Curvas: Problema 03.1 (Lei de Moore)
- **Método:** Regressão Linear pelo Método dos Mínimos Quadrados (MMQ) com linearização logarítmica na base 10.
- **Modelo Matemático:**
  Como o crescimento do número de transistores $N$ em função do ano $a$ é exponencial, aplica-se a transformação $y = \log_{10}(N)$ para ajustar a reta:
  $$
  y = b_0 + b_1 \cdot a \implies \log_{10}(N) = b_0 + b_1 \cdot a \implies N(a) = 10^{(b_0 + b_1 \cdot a)}
  $$
- **Dados Padrão (Histórico de Processadores Intel):**

| Chip | 4004 | 8008 | 8080 | 8086 | 80286 | 80386 | 80486 | Pentium | Pentium II | Pentium III | Pentium 4 |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| **Ano ($a$)** | 1971 | 1972 | 1974 | 1978 | 1982 | 1986 | 1989 | 1993 | 1997 | 1999 | 2000 |
| **Transistores ($N$)** | $2.250$ | $3.300$ | $6.000$ | $29.000$ | $134.000$ | $275.000$ | $1{,}2\times 10^6$ | $3{,}1\times 10^6$ | $7{,}5\times 10^6$ | $9{,}5\times 10^6$ | $42\times 10^6$ |

- **Saída esperada:**
  - Coeficientes da equação linearizada ($b_0, b_1$) e coeficiente de determinação ($R^2$) / erro quadrático residual;
  - Previsão do número de transistores para os anos de **2010** e **2020** (revertendo a transformação via $N = 10^y$).

---

### Tópico 04 — Integração Numérica: Problema 04.2 (Área da Superfície de um Rio)
- **Métodos:** Regra dos Trapézios Repetida e Regra $1/3$ de Simpson Repetida.
- **Descrição:** Determinar a área superficial de um trecho de rio entre $x = 0\text{ m}$ e $x = 40\text{ m}$ com passo $h = 10\text{ m}$ ($n = 4$ subintervalos), conhecendo-se as distâncias de duas margens $M_1(x)$ e $M_2(x)$ em relação a uma linha de referência:

| Posição $x_i$ ($\text{m}$) | $0$ | $10$ | $20$ | $30$ | $40$ |
| :--- | :---: | :---: | :---: | :---: | :---: |
| **Margem 1 — $M_1(x_i)$ ($\text{m}$)** | $50{,}8$ | $86{,}2$ | $136{,}0$ | $72{,}8$ | $51{,}0$ |
| **Margem 2 — $M_2(x_i)$ ($\text{m}$)** | $113{,}6$ | $144{,}5$ | $185{,}0$ | $171{,}2$ | $95{,}3$ |
| **Largura — $f(x_i) = M_2(x_i) - M_1(x_i)$ ($\text{m}$)** | $62{,}8$ | $58{,}3$ | $49{,}0$ | $98{,}4$ | $44{,}3$ |

- **Fórmulas Compostas:**
  - **Trapézios Repetida:**
    $$
    I_T \approx \frac{h}{2} \left[ f(x_0) + 2\sum_{i=1}^{n-1} f(x_i) + f(x_n) \right]
    $$
  - **1/3 de Simpson Repetida** ($n$ par):
    $$
    I_S \approx \frac{h}{3} \left[ f(x_0) + 4\sum_{i\text{ ímpar}}^{n-1} f(x_i) + 2\sum_{i\text{ par}}^{n-2} f(x_i) + f(x_n) \right]
    $$
- **Saída esperada:** Tabela de larguras $f(x_i)$, área estimada por cada método (em $\text{m}^2$) e análise comparativa entre os resultados.

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

2. Compile todos os arquivos-fonte gerando os `.class` em um diretório `bin/` (ou diretamente a partir de `src/`):
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
