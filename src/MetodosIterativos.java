import java.util.Scanner;

/**
 * Tópico 02 - Sistemas Lineares (Métodos Iterativos)
 * Problema 02.2 (Treliça) — Método Iterativo de Gauss-Seidel.
 *
 * Modelagem Matemática dos Dados Padrão da Treliça (Sistema 6x6):
 * - Equilíbrio estático (Soma Fx = 0, Soma Fy = 0) das 6 forças internas (F1 a F6)
 *   nos nós da treliça com ângulos de 45°, 45°, 60° e 30° e cargas verticais de 500 e 100.
 * - Equações já ordenadas para garantir elementos não-nulos na diagonal principal
 *   e convergência no Método de Gauss-Seidel:
 *     1)  cos(45°)*F1 + F4                   =   0   (Nó 1 - Fx)
 *     2) -cos(45°)*F1 + F2 + cos(45°)*F3     =   0   (Nó 2 - Fx)
 *     3) -sin(45°)*F1      - sin(45°)*F3     = 500   (Nó 2 - Fy)
 *     4)              -cos(45°)*F3 - F4 + cos(60°)*F5 = 0 (Nó 4 - Fx)
 *     5)               sin(45°)*F3      + sin(60°)*F5 = 0 (Nó 4 - Fy)
 *     6)                                - sin(60°)*F5 - sin(30°)*F6 = 100 (Nó 3 - Fy)
 */
public class MetodosIterativos {

    public static final double TOLERANCIA_PADRAO = 0.0001;
    public static final int MAX_ITERACOES_PADRAO = 1000;
    private static final double EPSILON_ZERO = 1e-12;

    // Constantes trigonométricas em dupla precisão usando Math.toRadians()
    private static final double COS_45 = Math.cos(Math.toRadians(45.0));
    private static final double SIN_45 = Math.sin(Math.toRadians(45.0));
    private static final double COS_60 = Math.cos(Math.toRadians(60.0));
    private static final double SIN_60 = Math.sin(Math.toRadians(60.0));
    private static final double COS_30 = Math.cos(Math.toRadians(30.0));
    private static final double SIN_30 = Math.sin(Math.toRadians(30.0));

    // Matriz 6x6 padrão de equilíbrio estático da treliça
    private static final double[][] MATRIZ_TRELICA_A = {
        { COS_45,  0.0,    0.0,    1.0,    0.0,    0.0   }, // Eq 1 (F1): cos(45)F1 + F4 = 0
        {-COS_45,  1.0,    COS_45, 0.0,    0.0,    0.0   }, // Eq 2 (F2): -cos(45)F1 + F2 + cos(45)F3 = 0
        {-SIN_45,  0.0,   -SIN_45, 0.0,    0.0,    0.0   }, // Eq 3 (F3): -sin(45)F1 - sin(45)F3 = 500
        { 0.0,     0.0,   -COS_45,-1.0,    COS_60, 0.0   }, // Eq 4 (F4): -cos(45)F3 - F4 + cos(60)F5 = 0
        { 0.0,     0.0,    SIN_45, 0.0,    SIN_60, 0.0   }, // Eq 5 (F5): sin(45)F3 + sin(60)F5 = 0
        { 0.0,     0.0,    0.0,    0.0,   -SIN_60,-SIN_30}  // Eq 6 (F6): -sin(60)F5 - sin(30)F6 = 100
    };

    // Vetor de cargas externas padrão da treliça
    private static final double[] VETOR_TRELICA_B = {
        0.0,
        0.0,
        500.0,
        0.0,
        0.0,
        100.0
    };

    /**
     * Estrutura auxiliar para encapsular o resultado do método iterativo.
     */
    public static class ResultadoIterativo {
        public final double[] solucao;
        public final int iteracoes;
        public final double erroFinal;
        public final boolean convergiu;

        public ResultadoIterativo(double[] solucao, int iteracoes, double erroFinal, boolean convergiu) {
            this.solucao = solucao;
            this.iteracoes = iteracoes;
            this.erroFinal = erroFinal;
            this.convergiu = convergiu;
        }
    }

    /**
     * Gerencia o submenu interativo do Módulo 02.
     */
    public static void executarMenu(Scanner scanner) {
        int opcao;
        do {
            opcao = Main.exibirSubMenuModulo(
                scanner,
                "TOPICO 02 - METODOS ITERATIVOS (GAUSS-SEIDEL)",
                "Problema 02.2 - Trelica | eps = 0.0001"
            );

            switch (opcao) {
                case 1:
                    executarCasoPadrao();
                    Main.aguardarEnter(scanner);
                    break;
                case 2:
                    executarEntradaManual(scanner);
                    Main.aguardarEnter(scanner);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("[!] Opcao invalida.");
                    break;
            }
        } while (opcao != 0);
    }

    /**
     * Executa o Problema 02.2 (Treliça) com os dados padrão e precisão 0.0001.
     */
    private static void executarCasoPadrao() {
        System.out.println("\n>>> Executando Problema 02.2 (Trelica) com dados padrao (eps = 0.0001)...");
        System.out.printf("Constantes trigonometricas: cos(45)=%.4f, sin(45)=%.4f, cos(60)=%.4f, sin(60)=%.4f, cos(30)=%.4f, sin(30)=%.4f%n",
                COS_45, SIN_45, COS_60, SIN_60, COS_30, SIN_30);

        double[] xInicial = new double[VETOR_TRELICA_B.length]; // Vetor inicial nulo [0, 0, 0, 0, 0, 0]
        resolverEExibir(MATRIZ_TRELICA_A, VETOR_TRELICA_B, xInicial, TOLERANCIA_PADRAO, MAX_ITERACOES_PADRAO, true);
    }

    /**
     * Permite ao usuário inserir um sistema linear customizado, tolerância e vetor inicial.
     */
    private static void executarEntradaManual(Scanner scanner) {
        System.out.println("\n>>> Entrada Manual - Metodo Iterativo de Gauss-Seidel");
        int n = Main.lerInteiro(scanner, "Informe a ordem do sistema linear n (2 a 20): ", 2, 20);

        double[][] a = new double[n][n];
        double[] b = new double[n];
        double[] x0 = new double[n];

        System.out.println("\n--- Digite os coeficientes da Matriz A (" + n + "x" + n + ") ---");
        for (int i = 0; i < n; i++) {
            System.out.println("Linha " + (i + 1) + ":");
            for (int j = 0; j < n; j++) {
                a[i][j] = Main.lerDouble(scanner, String.format("  A[%d][%d]: ", i + 1, j + 1));
            }
        }

        System.out.println("\n--- Digite os termos independentes do Vetor b (" + n + ") ---");
        for (int i = 0; i < n; i++) {
            b[i] = Main.lerDouble(scanner, String.format("  b[%d]: ", i + 1));
        }

        double tolerancia;
        while (true) {
            tolerancia = Main.lerDouble(scanner, "\nInforme a tolerancia/precisao (ex: 0.0001): ");
            if (tolerancia > 0.0) {
                break;
            }
            System.out.println("[!] A tolerancia deve ser um valor estritamente positivo.");
        }

        System.out.println("\nEscolha a aproximacao inicial x^(0):");
        System.out.println("  1. Vetor nulo [0, 0, ..., 0]");
        System.out.println("  2. Quociente diagonal (b_i / a_ii)");
        System.out.println("  3. Digitar vetor inicial manualmente");
        int opChute = Main.lerInteiro(scanner, "Opcao [1-3]: ", 1, 3);

        if (opChute == 2) {
            for (int i = 0; i < n; i++) {
                x0[i] = (Math.abs(a[i][i]) >= EPSILON_ZERO) ? (b[i] / a[i][i]) : 0.0;
            }
        } else if (opChute == 3) {
            for (int i = 0; i < n; i++) {
                x0[i] = Main.lerDouble(scanner, String.format("  x^(0)[%d]: ", i + 1));
            }
        }

        resolverEExibir(a, b, x0, tolerancia, MAX_ITERACOES_PADRAO, false);
    }

    /**
     * Executa o fluxo completo: verifica e garante diagonal não-nula, avalia os critérios
     * de convergência (Linhas e Sassenfeld), executa o Gauss-Seidel e imprime a solução.
     */
    private static void resolverEExibir(
            double[][] aOriginal,
            double[] bOriginal,
            double[] x0,
            double tolerancia,
            int maxIteracoes,
            boolean isTrelica) {

        int n = bOriginal.length;
        double[][] a = copiarMatriz(aOriginal);
        double[] b = bOriginal.clone();

        // Garante que nenhum elemento da diagonal principal seja zero (troca de linhas se necessário)
        if (!garantirDiagonalNaoNula(a, b)) {
            System.out.println("\n[ERRO MATEMATICO] Nao foi possivel posicionar elementos nao-nulos em toda a diagonal principal.");
            return;
        }

        System.out.println("\n1) Sistema Linear [A | b]:");
        MetodosDiretos.imprimirMatrizAumentada(a, b);

        // 2. Verificação dos Critérios de Convergência (Linhas e Sassenfeld)
        System.out.println("\n2) Analise de Convergencia (Criterio das Linhas e Criterio de Sassenfeld):");
        double[] alphas = calcularFatoresLinhas(a);
        double[] betas = calcularFatoresSassenfeld(a);
        double maxAlpha = verificarCriterioLinhas(a);
        double maxBeta = verificarCriterioSassenfeld(a);

        System.out.println("----------------------------------------------------------------");
        System.out.println("  Linha (i)  |  Alfa_i (Criterio Linhas)  |  Beta_i (Sassenfeld)");
        System.out.println("----------------------------------------------------------------");
        for (int i = 0; i < n; i++) {
            System.out.printf("     %2d      |         %10.4f         |       %10.4f%n", i + 1, alphas[i], betas[i]);
        }
        System.out.println("----------------------------------------------------------------");
        System.out.printf("  Maximo     |  max(Alfa) = %10.4f    |  max(Beta) = %10.4f%n", maxAlpha, maxBeta);
        System.out.println("----------------------------------------------------------------");

        if (maxAlpha < 1.0) {
            System.out.println("  [OK] Criterio das Linhas SATISFEITO (max Alfa < 1). Convergencia garantida!");
        } else if (maxBeta < 1.0) {
            System.out.println("  [OK] Criterio de Sassenfeld SATISFEITO (max Beta < 1). Convergencia garantida!");
        } else {
            System.out.println("  [AVISO] Os criterios suficientes (Linhas/Sassenfeld) resultaram em fator >= 1.");
            System.out.println("          Como sao condicoes apenas SUFICIENTES (nao necessarias), o metodo");
            System.out.println("          de Gauss-Seidel sera executado com limite de seguranca de " + maxIteracoes + " iteracoes.");
        }

        // 3. Execução do Método de Gauss-Seidel
        ResultadoIterativo resultado = executarGaussSeidelDetalhado(a, b, x0, tolerancia, maxIteracoes);

        System.out.println("\n3) Resultado do Metodo de Gauss-Seidel:");
        System.out.println("----------------------------------------------------------------");
        if (resultado.convergiu) {
            System.out.printf("  Status             : CONVERGIU COM SUCESSO%n");
        } else {
            System.out.printf("  Status             : NAO CONVERGIU (interrompido na iteracao %d)%n", resultado.iteracoes);
        }
        System.out.printf("  Iteracoes realizadas: %d%n", resultado.iteracoes);
        System.out.printf("  Tolerancia exigida : %.6f%n", tolerancia);
        System.out.printf("  Erro final (max)   : %.6e%n", resultado.erroFinal);
        System.out.println("----------------------------------------------------------------");

        for (int i = 0; i < n; i++) {
            if (isTrelica) {
                String estado = (resultado.solucao[i] > 1e-6) ? "(Tracao)"
                              : (resultado.solucao[i] < -1e-6) ? "(Compressao)" : "(Nula)";
                System.out.printf("  F%d = %12.4f  %s%n", i + 1, resultado.solucao[i], estado);
            } else {
                System.out.printf("  x%d = %12.4f%n", i + 1, resultado.solucao[i]);
            }
        }
        System.out.println("----------------------------------------------------------------");
    }

    /**
     * Verifica o Critério das Linhas para a matriz A.
     *
     * @param a Matriz dos coeficientes (n x n)
     * @return Maior fator alfa_i calculado (converge garantidamente se < 1)
     */
    public static double verificarCriterioLinhas(double[][] a) {
        double[] alphas = calcularFatoresLinhas(a);
        double max = 0.0;
        for (double alpha : alphas) {
            if (alpha > max) {
                max = alpha;
            }
        }
        return max;
    }

    private static double[] calcularFatoresLinhas(double[][] a) {
        int n = a.length;
        double[] alphas = new double[n];
        for (int i = 0; i < n; i++) {
            double soma = 0.0;
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    soma += Math.abs(a[i][j]);
                }
            }
            alphas[i] = soma / Math.abs(a[i][i]);
        }
        return alphas;
    }

    /**
     * Verifica o Critério de Sassenfeld para convergência do método de Gauss-Seidel.
     *
     * @param a Matriz dos coeficientes (n x n)
     * @return Maior fator beta_i calculado (converge garantidamente se < 1)
     */
    public static double verificarCriterioSassenfeld(double[][] a) {
        double[] betas = calcularFatoresSassenfeld(a);
        double max = 0.0;
        for (double beta : betas) {
            if (beta > max) {
                max = beta;
            }
        }
        return max;
    }

    private static double[] calcularFatoresSassenfeld(double[][] a) {
        int n = a.length;
        double[] betas = new double[n];
        for (int i = 0; i < n; i++) {
            double soma = 0.0;
            for (int j = 0; j < n; j++) {
                if (j < i) {
                    soma += Math.abs(a[i][j]) * betas[j];
                } else if (j > i) {
                    soma += Math.abs(a[i][j]);
                }
            }
            betas[i] = soma / Math.abs(a[i][i]);
        }
        return betas;
    }

    /**
     * Resolve o sistema linear Ax = b pelo Método Iterativo de Gauss-Seidel
     * partindo do vetor inicial nulo.
     */
    public static double[] gaussSeidel(double[][] a, double[] b, double tolerancia, int maxIteracoes) {
        double[] x0 = new double[b.length];
        return executarGaussSeidelDetalhado(a, b, x0, tolerancia, maxIteracoes).solucao;
    }

    /**
     * Executa o Método Iterativo de Gauss-Seidel usando o critério de parada
     * de erro máximo absoluto: max |x_i^(k+1) - x_i^(k)| <= tolerancia.
     */
    public static ResultadoIterativo executarGaussSeidelDetalhado(
            double[][] a,
            double[] b,
            double[] xInicial,
            double tolerancia,
            int maxIteracoes) {

        int n = b.length;
        double[] x = xInicial.clone();
        double erroMax = Double.MAX_VALUE;

        for (int iter = 1; iter <= maxIteracoes; iter++) {
            erroMax = 0.0;

            for (int i = 0; i < n; i++) {
                double soma = b[i];
                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        soma -= a[i][j] * x[j];
                    }
                }

                double novoXi = soma / a[i][i];
                double diff = Math.abs(novoXi - x[i]);
                if (diff > erroMax) {
                    erroMax = diff;
                }
                x[i] = novoXi;

                // Proteção contra divergência explosiva (overflow / NaN)
                if (Double.isNaN(novoXi) || Double.isInfinite(novoXi) || Math.abs(novoXi) > 1e15) {
                    return new ResultadoIterativo(x, iter, erroMax, false);
                }
            }

            if (erroMax <= tolerancia) {
                return new ResultadoIterativo(x, iter, erroMax, true);
            }
        }

        return new ResultadoIterativo(x, maxIteracoes, erroMax, false);
    }

    /**
     * Realiza troca de linhas caso algum elemento da diagonal principal a[i][i] seja zero.
     */
    private static boolean garantirDiagonalNaoNula(double[][] a, double[] b) {
        int n = b.length;
        for (int i = 0; i < n; i++) {
            if (Math.abs(a[i][i]) < EPSILON_ZERO) {
                int linhaTroca = -1;
                double maior = EPSILON_ZERO;
                for (int k = i + 1; k < n; k++) {
                    if (Math.abs(a[k][i]) > maior && Math.abs(a[i][k]) > EPSILON_ZERO) {
                        maior = Math.abs(a[k][i]);
                        linhaTroca = k;
                    }
                }
                if (linhaTroca == -1) {
                    for (int k = 0; k < n; k++) {
                        if (k != i && Math.abs(a[k][i]) > EPSILON_ZERO && Math.abs(a[i][k]) > EPSILON_ZERO) {
                            linhaTroca = k;
                            break;
                        }
                    }
                }
                if (linhaTroca != -1) {
                    double[] tempLinha = a[i];
                    a[i] = a[linhaTroca];
                    a[linhaTroca] = tempLinha;

                    double tempB = b[i];
                    b[i] = b[linhaTroca];
                    b[linhaTroca] = tempB;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    private static double[][] copiarMatriz(double[][] origem) {
        double[][] copia = new double[origem.length][];
        for (int i = 0; i < origem.length; i++) {
            copia[i] = origem[i].clone();
        }
        return copia;
    }
}
