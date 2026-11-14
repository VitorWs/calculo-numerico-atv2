import java.util.Scanner;

/**
 * Tópico 04 - Integração Numérica
 * Problema 04.2 (Área do Rio) — Regra dos Trapézios Repetida e Regra 1/3 de Simpson Repetida.
 *
 * Dados Padrão:
 * - Intervalo x em [0, 40] com passo h = 10 (n = 4 subintervalos)
 * - x     = [0.0,   10.0,  20.0,  30.0,  40.0]
 * - M1(x) = [50.8,  86.2,  136.0, 72.8,  51.0]
 * - M2(x) = [113.6, 144.5, 185.0, 171.2, 95.3]
 * - Largura do rio: f(x_i) = M2(x_i) - M1(x_i)
 */
public class IntegracaoNumerica {

    private static final double PASSO_PADRAO_H = 10.0;

    private static final double[] X_PADRAO = {
        0.0, 10.0, 20.0, 30.0, 40.0
    };

    private static final double[] MARGEM_1_PADRAO = {
        50.8, 86.2, 136.0, 72.8, 51.0
    };

    private static final double[] MARGEM_2_PADRAO = {
        113.6, 144.5, 185.0, 171.2, 95.3
    };

    /**
     * Gerencia o submenu interativo do Módulo 04.
     */
    public static void executarMenu(Scanner scanner) {
        int opcao;
        do {
            opcao = Main.exibirSubMenuModulo(
                scanner,
                "TOPICO 04 - INTEGRACAO NUMERICA (TRAPEZIOS E SIMPSON 1/3)",
                "Problema 04.2 - Area do Rio"
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
     * Executa o Problema 04.2 (Área do Rio) com os dados padrão.
     */
    private static void executarCasoPadrao() {
        System.out.println("\n>>> Executando Problema 04.2 (Area do Rio) com dados padrao...");
        // TODO: Calcular f(x_i) = M2(x_i) - M1(x_i), aplicar Regra dos Trapézios Repetida
        //       e Regra 1/3 de Simpson Repetida, e exibir comparativo entre os dois métodos.
        System.out.println("[TODO] Implementar Regra dos Trapezios Repetida e Regra 1/3 de Simpson Repetida.");
    }

    /**
     * Permite ao usuário inserir pontos tabelados manualmente (com passo h constante).
     */
    private static void executarEntradaManual(Scanner scanner) {
        System.out.println("\n>>> Entrada Manual - Integracao Numerica");
        int n = Main.lerInteiro(scanner, "Informe o numero de subintervalos n (par para Simpson 1/3, n >= 2): ", 2, 200);
        // TODO: Ler passo h e valores f(x_0)...f(x_n) (ou margens M1 e M2) e calcular integrais.
        System.out.println("[TODO] Implementar leitura de " + (n + 1) + " pontos e integracao numerica.");
    }

    /**
     * Calcula a integral aproximada pela Regra dos Trapézios Repetida.
     *
     * @param fx Valores da função nos pontos equidistantes f(x_0), ..., f(x_n)
     * @param h  Passo de integração
     * @return Valor aproximado da integral
     */
    public static double regraTrapeziosRepetida(double[] fx, double h) {
        // TODO: Implementar (h / 2) * [fx[0] + 2 * soma(fx[1..n-1]) + fx[n]]
        return 0.0;
    }

    /**
     * Calcula a integral aproximada pela Regra 1/3 de Simpson Repetida (requer número par de subintervalos).
     *
     * @param fx Valores da função nos pontos equidistantes f(x_0), ..., f(x_n)
     * @param h  Passo de integração
     * @return Valor aproximado da integral
     */
    public static double regraSimpson13Repetida(double[] fx, double h) {
        // TODO: Implementar (h / 3) * [fx[0] + 4 * somaImpares + 2 * somaPares + fx[n]]
        return 0.0;
    }
}
