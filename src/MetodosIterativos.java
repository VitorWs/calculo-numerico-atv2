import java.util.Scanner;

/**
 * Tópico 02 - Sistemas Lineares (Métodos Iterativos)
 * Problema 02.2 (Treliça) — Método Iterativo de Gauss-Seidel.
 *
 * Dados Padrão:
 * - Equilíbrio estático (Soma Fx = 0, Soma Fy = 0) em treliça plana articulada
 *   com ângulos de 45°, 45°, 60° e 30° e cargas verticais de 500 e 100.
 * - Tolerância / Precisão exigida: epsilon = 0.0001 (1e-4).
 */
public class MetodosIterativos {

    // Precisão padrão exigida pelo problema
    public static final double TOLERANCIA_PADRAO = 0.0001;
    public static final int MAX_ITERACOES_PADRAO = 1000;

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
        // TODO: Montar o sistema de equilíbrio da treliça, verificar Critério das Linhas / Sassenfeld
        //       e executar o método de Gauss-Seidel exibindo o número de iterações e tensões nas barras.
        System.out.println("[TODO] Implementar sistema da trelica e Metodo de Gauss-Seidel.");
    }

    /**
     * Permite ao usuário inserir um sistema linear customizado e a precisão desejada.
     */
    private static void executarEntradaManual(Scanner scanner) {
        System.out.println("\n>>> Entrada Manual - Metodo de Gauss-Seidel");
        int n = Main.lerInteiro(scanner, "Informe a ordem do sistema linear (n >= 2): ", 2, 20);
        // TODO: Ler matriz A (n x n), vetor b (n), tolerância epsilon e chute inicial x^(0).
        System.out.println("[TODO] Implementar leitura do sistema " + n + "x" + n + " e iteracoes de Gauss-Seidel.");
    }

    /**
     * Verifica o Critério de Sassenfeld para convergência do método de Gauss-Seidel.
     *
     * @param a Matriz dos coeficientes (n x n)
     * @return Maior fator beta_i calculado (converge garantidamente se < 1)
     */
    public static double verificarCriterioSassenfeld(double[][] a) {
        // TODO: Implementar cálculo dos coeficientes beta_i de Sassenfeld.
        return 0.0;
    }

    /**
     * Resolve o sistema linear Ax = b pelo Método Iterativo de Gauss-Seidel.
     *
     * @param a            Matriz dos coeficientes (n x n)
     * @param b            Vetor dos termos independentes (n)
     * @param tolerancia   Critério de parada para o erro relativo máximo
     * @param maxIteracoes Limite máximo de segurança de iterações
     * @return Vetor solução x aproximado
     */
    public static double[] gaussSeidel(double[][] a, double[] b, double tolerancia, int maxIteracoes) {
        // TODO: Implementar laço iterativo de Gauss-Seidel com cálculo de erro relativo máximo.
        return new double[b.length];
    }
}
