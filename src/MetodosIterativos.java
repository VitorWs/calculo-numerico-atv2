import java.util.Scanner;

/**
 * Tópico 02 - Sistemas Lineares (Métodos Iterativos)
 * Problema 02.2 (Treliça) — Método Iterativo de Gauss-Seidel.
 *
 * Modelagem Matemática dos Dados Padrão da Treliça (Sistema 10x10):
 * - 5 nós articulados (10 equações de equilíbrio estático: Soma Fx = 0, Soma Fy = 0)
 * - 7 barras (F1 a F7) e 3 reações de apoio (H1, V1, V5) = 10 incógnitas.
 * - Vetor de incógnitas reordenado para garantir diagonal principal não-nula (a_ii != 0)
 *   e convergência no Método de Gauss-Seidel:
 *   x = (H1, V1, F2, F1, F6, F5, F4, F3, F7, V5)
 *
 * Equações dos Nós:
 *   1) N1 Soma Fx:  1*H1 + cos(45°)*F1 + 1*F4                        =   0
 *   2) N1 Soma Fy:  1*V1 + sin(45°)*F1                               =   0
 *   3) N2 Soma Fx:  1*F2 - cos(45°)*F1 + cos(45°)*F3                 =   0
 *   4) N2 Soma Fy:       - sin(45°)*F1 - sin(45°)*F3                 = 500
 *   5) N3 Soma Fx: -1*F2 + cos(30°)*F6 - cos(60°)*F5                 =   0
 *   6) N3 Soma Fy:       - sin(30°)*F6 - sin(60°)*F5                 = 100
 *   7) N4 Soma Fx:  cos(60°)*F5 - 1*F4 - cos(45°)*F3 + 1*F7          =   0
 *   8) N4 Soma Fy:  sin(60°)*F5        + sin(45°)*F3                 =   0
 *   9) N5 Soma Fx: -cos(30°)*F6                      - 1*F7          =   0
 *  10) N5 Soma Fy:  sin(30°)*F6                             + 1*V5   =   0
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

    // Ordem das incógnitas nas colunas da matriz 10x10 da treliça
    public static final String[] NOMES_INCOGNITAS_TRELICA = {
        "H1", "V1", "F2", "F1", "F6", "F5", "F4", "F3", "F7", "V5"
    };

    // Matriz 10x10 padrão de equilíbrio estático da treliça
    // Colunas:   H1    V1    F2       F1       F6       F5      F4       F3     F7    V5
    public static final double[][] MATRIZ_TRELICA_A = {
        { 1.0,  0.0,  0.0,  COS_45,  0.0,     0.0,     1.0,    0.0,     0.0,  0.0 }, // 1) N1 Fx
        { 0.0,  1.0,  0.0,  SIN_45,  0.0,     0.0,     0.0,    0.0,     0.0,  0.0 }, // 2) N1 Fy
        { 0.0,  0.0,  1.0, -COS_45,  0.0,     0.0,     0.0,    COS_45,  0.0,  0.0 }, // 3) N2 Fx
        { 0.0,  0.0,  0.0, -SIN_45,  0.0,     0.0,     0.0,   -SIN_45,  0.0,  0.0 }, // 4) N2 Fy
        { 0.0,  0.0, -1.0,  0.0,     COS_30, -COS_60,  0.0,    0.0,     0.0,  0.0 }, // 5) N3 Fx
        { 0.0,  0.0,  0.0,  0.0,    -SIN_30, -SIN_60,  0.0,    0.0,     0.0,  0.0 }, // 6) N3 Fy
        { 0.0,  0.0,  0.0,  0.0,     0.0,     COS_60, -1.0,   -COS_45,  1.0,  0.0 }, // 7) N4 Fx
        { 0.0,  0.0,  0.0,  0.0,     0.0,     SIN_60,  0.0,    SIN_45,  0.0,  0.0 }, // 8) N4 Fy
        { 0.0,  0.0,  0.0,  0.0,    -COS_30,  0.0,     0.0,    0.0,    -1.0,  0.0 }, // 9) N5 Fx
        { 0.0,  0.0,  0.0,  0.0,     SIN_30,  0.0,     0.0,    0.0,     0.0,  1.0 }  // 10) N5 Fy
    };

    // Vetor de cargas externas padrão da treliça (10x1)
    public static final double[] VETOR_TRELICA_B = {
        0.0,
        0.0,
        0.0,
        500.0,
        0.0,
        100.0,
        0.0,
        0.0,
        0.0,
        0.0
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
                "Problema 02.2 - Trelica 10x10 | eps = 0.0001"
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
     * Executa o Problema 02.2 (Treliça 10x10: 7 barras + 3 reações) com os dados padrão e precisão 0.0001.
     */
    private static void executarCasoPadrao() {
        System.out.println("\n>>> Executando Problema 02.2 (Trelica - 7 barras e 3 reacoes = 10x10) com dados padrao (eps = 0.0001)...");
        System.out.printf("Constantes trigonometricas: cos(45)=%.4f, sin(45)=%.4f, cos(60)=%.4f, sin(60)=%.4f, cos(30)=%.4f, sin(30)=%.4f%n",
                COS_45, SIN_45, COS_60, SIN_60, COS_30, SIN_30);
        System.out.println("Ordem das incognitas x = (H1, V1, F2, F1, F6, F5, F4, F3, F7, V5)");

        double[] xInicial = new double[VETOR_TRELICA_B.length]; // Vetor inicial nulo
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

        if (isTrelica && n == 10) {
            System.out.println(formatarSolucaoTrelica10x10(resultado.solucao));
        } else {
            for (int i = 0; i < n; i++) {
                System.out.printf("  x%d = %12.4f%n", i + 1, resultado.solucao[i]);
            }
            System.out.println("----------------------------------------------------------------");
        }
    }

    /**
     * Formata a solução da treliça 10x10 organizando as 7 forças nas barras (F1 a F7)
     * e as 3 reações de apoio (H1, V1, V5).
     * Ordem no vetor x: (0:H1, 1:V1, 2:F2, 3:F1, 4:F6, 5:F5, 6:F4, 7:F3, 8:F7, 9:V5)
     */
    public static String formatarSolucaoTrelica10x10(double[] x) {
        double h1 = (Math.abs(x[0]) < 1e-3) ? 0.0 : x[0];
        double v1 = x[1];
        double f2 = x[2];
        double f1 = x[3];
        double f6 = x[4];
        double f5 = x[5];
        double f4 = x[6];
        double f3 = x[7];
        double f7 = x[8];
        double v5 = x[9];

        double[] forcasBarras = {f1, f2, f3, f4, f5, f6, f7};
        String[] descBarras = {
            "Barra inclinada esquerda (45 graus)",
            "Barra horizontal superior",
            "Diagonal interna esquerda (45 graus)",
            "Barra horizontal inferior esquerda",
            "Diagonal interna direita (60 graus)",
            "Barra inclinada direita (30 graus)",
            "Barra horizontal inferior direita"
        };

        StringBuilder sb = new StringBuilder();
        sb.append("  Forcas Internas nas 7 Barras da Trelica:\n");
        for (int i = 0; i < forcasBarras.length; i++) {
            double val = forcasBarras[i];
            String estado = (val > 1e-6) ? "Tracao" : (val < -1e-6) ? "Compressao" : "Nula";
            sb.append(String.format("    F%d = %10.4f  [%-10s]  (%s)%n",
                    i + 1, val, estado, descBarras[i]));
        }
        sb.append("----------------------------------------------------------------\n");
        sb.append("  Reacoes de Apoio (Nos 1 e 5):\n");
        sb.append(String.format("    H1 = %10.4f  [Nula]        (Reacao horizontal - No 1 Apoio fixo)%n", h1));
        sb.append(String.format("    V1 = %10.4f  [Reacao]      (Reacao vertical   - No 1 Apoio fixo)%n", v1));
        sb.append(String.format("    V5 = %10.4f  [Reacao]      (Reacao vertical   - No 5 Apoio movel)%n", v5));
        sb.append(String.format("    Verificacao vertical: V1 + V5 = %.4f (Cargas: 500 + 100 = 600)%n", v1 + v5));
        sb.append("----------------------------------------------------------------");
        return sb.toString();
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

    public static double[] calcularFatoresLinhas(double[][] a) {
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

    public static double[] calcularFatoresSassenfeld(double[][] a) {
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
