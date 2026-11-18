import java.util.Scanner;

/**
 * Tópico 01 - Sistemas Lineares (Métodos Diretos)
 * Problema 01.3 (Mineração) — Método da Eliminação de Gauss com Pivoteamento Parcial
 * e Substituição Retroativa.
 *
 * Modelagem Matemática dos Dados Padrão (Sistema 3x3):
 * - x1 = volume minerado da Mina 1 (m³)
 * - x2 = volume minerado da Mina 2 (m³)
 * - x3 = volume minerado da Mina 3 (m³)
 *
 * Equações:
 *   0.55*x1 + 0.25*x2 + 0.25*x3 = 4800  (Areia)
 *   0.30*x1 + 0.45*x2 + 0.20*x3 = 5800  (Cascalho Fino)
 *   0.15*x1 + 0.30*x2 + 0.55*x3 = 5700  (Cascalho Grosso)
 */
public class MetodosDiretos {

    private static final double EPSILON_PIVO = 1e-12;

    // Matriz de coeficientes padrão (proporções de cada material nas 3 minas)
    private static final double[][] MATRIZ_PADRAO_A = {
        {0.55, 0.25, 0.25}, // Areia
        {0.30, 0.45, 0.20}, // Cascalho Fino
        {0.15, 0.30, 0.55}  // Cascalho Grosso
    };

    // Vetor de termos independentes padrão (demanda em m³)
    private static final double[] VETOR_PADRAO_B = {
        4800.0,
        5800.0,
        5700.0
    };

    private static final String[] ROTULOS_MINAS = {
        "x1 (Mina 1 - m3)",
        "x2 (Mina 2 - m3)",
        "x3 (Mina 3 - m3)"
    };

    /**
     * Gerencia o submenu interativo do Módulo 01.
     */
    public static void executarMenu(Scanner scanner) {
        int opcao;
        do {
            opcao = Main.exibirSubMenuModulo(
                scanner,
                "TOPICO 01 - METODOS DIRETOS (ELIMINACAO DE GAUSS)",
                "Problema 01.3 - Mineracao"
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
     * Executa o Problema 01.3 (Mineração) com os dados padrão.
     */
    private static void executarCasoPadrao() {
        System.out.println("\n>>> Executando Problema 01.3 (Mineracao) com dados padrao...");
        System.out.println("Demandas: 4800 m3 de Areia | 5800 m3 de Cascalho Fino | 5700 m3 de Cascalho Grosso");
        resolverEExibir(MATRIZ_PADRAO_A, VETOR_PADRAO_B, ROTULOS_MINAS);
    }

    /**
     * Permite ao usuário informar a ordem do sistema, a matriz A e o vetor b manualmente.
     */
    private static void executarEntradaManual(Scanner scanner) {
        System.out.println("\n>>> Entrada Manual - Eliminacao de Gauss com Pivoteamento Parcial");
        int n = Main.lerInteiro(scanner, "Informe a ordem do sistema linear n (2 a 20): ", 2, 20);

        double[][] a = new double[n][n];
        double[] b = new double[n];

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

        resolverEExibir(a, b, null);
    }

    /**
     * Executa o fluxo completo: exibe a matriz aumentada inicial, aplica a Eliminação de Gauss
     * com pivoteamento parcial, exibe a matriz triangularizada e mostra o vetor solução.
     */
    private static void resolverEExibir(double[][] aOriginal, double[] bOriginal, String[] rotulos) {
        int n = bOriginal.length;
        double[][] a = copiarMatriz(aOriginal);
        double[] b = copiarVetor(bOriginal);

        System.out.println("\n1) Matriz Aumentada Inicial [A | b]:");
        imprimirMatrizAumentada(a, b);

        try {
            double[] x = eliminacaoDeGauss(a, b);

            System.out.println("\n2) Matriz Triangularizada (apos Escalonamento com Pivoteamento Parcial) [U | b']:");
            imprimirMatrizAumentada(a, b);

            System.out.println("\n3) Vetor Solucao Final (Substituicao Retroativa):");
            System.out.println("----------------------------------------------------------------");
            double somaTotal = 0.0;
            for (int i = 0; i < n; i++) {
                String nomeVar = (rotulos != null && i < rotulos.length)
                        ? rotulos[i]
                        : String.format("x%d", i + 1);
                System.out.printf("  %-20s = %12.4f%n", nomeVar, x[i]);
                somaTotal += x[i];
            }
            if (rotulos != null) {
                System.out.println("----------------------------------------------------------------");
                System.out.printf("  %-20s = %12.4f%n", "Volume Total (m3)", somaTotal);
            }
            System.out.println("----------------------------------------------------------------");

            // Verificação do resíduo máximo |A_orig * x - b_orig|
            double residuoMax = calcularResiduoMaximo(aOriginal, bOriginal, x);
            System.out.printf("  Residuo maximo ||Ax - b||_inf: %.4e%n", residuoMax);

        } catch (ArithmeticException e) {
            System.out.println("\n[ERRO MATEMATICO] " + e.getMessage());
        }
    }

    /**
     * Resolve o sistema linear Ax = b pelo Método da Eliminação de Gauss com Pivoteamento Parcial
     * e Substituição Retroativa.
     * Nota: As matrizes passadas (a e b) são modificadas in-place para a forma triangular superior.
     *
     * @param a Matriz dos coeficientes (n x n)
     * @param b Vetor dos termos independentes (n)
     * @return Vetor solução x (n)
     */
    public static double[] eliminacaoDeGauss(double[][] a, double[] b) {
        int n = b.length;

        // 1. Fase de Eliminação (Escalonamento com Pivoteamento Parcial)
        for (int k = 0; k < n - 1; k++) {
            // Localiza o pivô de maior módulo na coluna k (a partir da linha k)
            int linhaPivo = k;
            double maxValor = Math.abs(a[k][k]);
            for (int i = k + 1; i < n; i++) {
                if (Math.abs(a[i][k]) > maxValor) {
                    maxValor = Math.abs(a[i][k]);
                    linhaPivo = i;
                }
            }

            // Verifica se o pivô é nulo (sistema singular ou indeterminado)
            if (maxValor < EPSILON_PIVO) {
                throw new ArithmeticException(
                    "Pivo nulo ou proximo de zero na coluna " + (k + 1) + ". O sistema e singular ou indeterminado."
                );
            }

            // Troca as linhas k e linhaPivo, se necessário
            if (linhaPivo != k) {
                double[] tempLinha = a[k];
                a[k] = a[linhaPivo];
                a[linhaPivo] = tempLinha;

                double tempB = b[k];
                b[k] = b[linhaPivo];
                b[linhaPivo] = tempB;
            }

            // Eliminação dos elementos abaixo do pivô a[k][k]
            for (int i = k + 1; i < n; i++) {
                double fator = a[i][k] / a[k][k];
                a[i][k] = 0.0;
                for (int j = k + 1; j < n; j++) {
                    a[i][j] -= fator * a[k][j];
                }
                b[i] -= fator * b[k];
            }
        }

        // Verifica o último elemento da diagonal principal
        if (Math.abs(a[n - 1][n - 1]) < EPSILON_PIVO) {
            throw new ArithmeticException(
                "Pivo nulo na ultima equacao. O sistema nao possui solucao unica."
            );
        }

        // 2. Fase de Substituição Retroativa
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double soma = b[i];
            for (int j = i + 1; j < n; j++) {
                soma -= a[i][j] * x[j];
            }
            x[i] = soma / a[i][i];
        }

        return x;
    }

    /**
     * Imprime a matriz aumentada [A | b] formatada no terminal.
     */
    public static void imprimirMatrizAumentada(double[][] a, double[] b) {
        int n = b.length;
        for (int i = 0; i < n; i++) {
            System.out.print("  | ");
            for (int j = 0; j < n; j++) {
                System.out.printf("%10.4f ", a[i][j]);
            }
            System.out.printf("| %12.4f |%n", b[i]);
        }
    }

    private static double calcularResiduoMaximo(double[][] a, double[] b, double[] x) {
        int n = b.length;
        double maxErro = 0.0;
        for (int i = 0; i < n; i++) {
            double soma = 0.0;
            for (int j = 0; j < n; j++) {
                soma += a[i][j] * x[j];
            }
            double erro = Math.abs(soma - b[i]);
            if (erro > maxErro) {
                maxErro = erro;
            }
        }
        return maxErro;
    }

    private static double[][] copiarMatriz(double[][] origem) {
        double[][] copia = new double[origem.length][];
        for (int i = 0; i < origem.length; i++) {
            copia[i] = origem[i].clone();
        }
        return copia;
    }

    private static double[] copiarVetor(double[] origem) {
        return origem.clone();
    }
}
