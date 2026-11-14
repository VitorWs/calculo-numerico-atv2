import java.util.Scanner;

/**
 * Tópico 03 - Interpolação e Ajuste de Curvas
 * Problema 03.1 (Lei de Moore) — Regressão Linear pelo Método dos Mínimos Quadrados (MMQ).
 *
 * Dados Padrão:
 * - Anos (a): 1971, 1972, 1974, 1978, 1982, 1986, 1989, 1993, 1997, 1999, 2000
 * - Transistores (N): 2250, 3300, 6000, 29000, 134000, 275000, 1200000, 3100000, 7500000, 9500000, 42000000
 * - Transformação linearizante: y = log10(N) = b0 + b1 * a  =>  N(a) = 10^(b0 + b1 * a)
 * - Previsões exigidas: Anos de 2010 e 2020.
 */
public class MinimosQuadrados {

    // Nomes dos processadores da tabela original do Problema 03.1
    private static final String[] CHIPS_PADRAO = {
        "4004", "8008", "8080", "8086", "80286", "80386",
        "80486", "Pentium", "Pentium II", "Pentium III", "Pentium 4"
    };

    // Vetor padrão de anos (variável independente a)
    private static final double[] ANOS_PADRAO = {
        1971, 1972, 1974, 1978, 1982, 1986, 1989, 1993, 1997, 1999, 2000
    };

    // Vetor padrão do número de transistores (variável dependente N)
    private static final double[] TRANSISTORES_PADRAO = {
        2_250.0,
        3_300.0,
        6_000.0,
        29_000.0,
        134_000.0,
        275_000.0,
        1_200_000.0,
        3_100_000.0,
        7_500_000.0,
        9_500_000.0,
        42_000_000.0
    };

    /**
     * Gerencia o submenu interativo do Módulo 03.
     */
    public static void executarMenu(Scanner scanner) {
        int opcao;
        do {
            opcao = Main.exibirSubMenuModulo(
                scanner,
                "TOPICO 03 - AJUSTE DE CURVAS (MINIMOS QUADRADOS)",
                "Problema 03.1 - Lei de Moore"
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
     * Executa o Problema 03.1 (Lei de Moore) com os 11 pontos históricos padrão.
     */
    private static void executarCasoPadrao() {
        System.out.println("\n>>> Executando Problema 03.1 (Lei de Moore) com dados padrao...");
        // TODO: Aplicar y_i = Math.log10(N_i), ajustar reta por MMQ, calcular erro quadrático
        //       e estimar N(2010) e N(2020) via Math.pow(10, b0 + b1 * ano).
        System.out.println("[TODO] Implementar Regressao Linear MMQ com transformacao log10(N).");
    }

    /**
     * Permite ao usuário inserir novos pares de pontos (x_i, y_i) para ajuste por MMQ.
     */
    private static void executarEntradaManual(Scanner scanner) {
        System.out.println("\n>>> Entrada Manual - Minimos Quadrados (Regressao Linear)");
        int m = Main.lerInteiro(scanner, "Informe a quantidade de pontos tabelados (m >= 2): ", 2, 100);
        // TODO: Ler vetores x e y (com opção de transformação log10 ou ajuste linear direto) e prever valores.
        System.out.println("[TODO] Implementar leitura de " + m + " pontos e calculo dos coeficientes MMQ.");
    }

    /**
     * Calcula os coeficientes [b0, b1] da reta ajustada y = b0 + b1 * x pelo Método dos Mínimos Quadrados.
     *
     * @param x Vetor da variável independente
     * @param y Vetor da variável dependente (já linearizada, se aplicável)
     * @return Array de tamanho 2 contendo {b0, b1}
     */
    public static double[] ajustarRetaMMQ(double[] x, double[] y) {
        // TODO: Implementar somatórios de x_i, y_i, x_i^2 e x_i*y_i para obter b0 e b1.
        return new double[]{0.0, 0.0};
    }
}
