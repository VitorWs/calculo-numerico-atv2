import java.util.Scanner;

/**
 * Tópico 01 - Sistemas Lineares (Métodos Diretos)
 * Problema 01.3 (Mineração) — Método da Eliminação de Gauss com Pivoteamento Parcial.
 *
 * Dados Padrão:
 * - Demanda: 4800 m³ de areia, 5800 m³ de cascalho fino, 5700 m³ de cascalho grosso.
 * - Mina 1: 55% areia, 30% cascalho fino, 15% cascalho grosso.
 * - Mina 2: 25% areia, 45% cascalho fino, 30% cascalho grosso.
 * - Mina 3: 25% areia, 20% cascalho fino, 55% cascalho grosso.
 */
public class MetodosDiretos {

    // Matriz de coeficientes padrão (proporções de cada material nas 3 minas)
    private static final double[][] MATRIZ_PADRAO_A = {
        {0.55, 0.25, 0.25}, // Areia
        {0.30, 0.45, 0.20}, // Cascalho fino
        {0.15, 0.30, 0.55}  // Cascalho grosso
    };

    // Vetor de termos independentes padrão (demanda em m³)
    private static final double[] VETOR_PADRAO_B = {
        4800.0,
        5800.0,
        5700.0
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
        // TODO: Chamar eliminacaoDeGauss(MATRIZ_PADRAO_A, VETOR_PADRAO_B) e exibir resultados formatados.
        System.out.println("[TODO] Implementar Eliminacao de Gauss e Substituicao Regressiva.");
    }

    /**
     * Permite ao usuário informar a ordem do sistema, a matriz A e o vetor b manualmente.
     */
    private static void executarEntradaManual(Scanner scanner) {
        System.out.println("\n>>> Entrada Manual - Eliminacao de Gauss");
        int n = Main.lerInteiro(scanner, "Informe a ordem do sistema linear (n >= 2): ", 2, 20);
        // TODO: Ler matriz A (n x n), vetor b (n) e resolver via eliminacaoDeGauss(A, b).
        System.out.println("[TODO] Implementar leitura da matriz " + n + "x" + n + " e resolucao por Eliminacao de Gauss.");
    }

    /**
     * Resolve o sistema linear Ax = b pelo Método da Eliminação de Gauss com pivoteamento parcial.
     *
     * @param a Matriz dos coeficientes (n x n)
     * @param b Vetor dos termos independentes (n)
     * @return Vetor solução x (n)
     */
    public static double[] eliminacaoDeGauss(double[][] a, double[] b) {
        // TODO: Implementar escalonamento com pivoteamento parcial e substituição regressiva.
        return new double[b.length];
    }
}
