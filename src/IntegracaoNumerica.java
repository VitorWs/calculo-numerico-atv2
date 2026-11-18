import java.util.Scanner;

/**
 * Tópico 04 - Integração Numérica
 * Problema 04.2 (Área do Rio) — Regra dos Trapézios Repetida e Regra 1/3 de Simpson Repetida.
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

    private static void executarCasoPadrao() {
        System.out.println("\n>>> Executando Problema 04.2 (Area do Rio) com dados padrao...");
        double[] f = new double[MARGEM_1_PADRAO.length];

        System.out.println("\nTabela de Larguras do Rio f(x_i) = M2(x_i) - M1(x_i):");
        System.out.println("----------------------------------------------------------------");
        System.out.println("   x_i (m)  |   M1(x_i) (m)  |   M2(x_i) (m)  |   f(x_i) (m)");
        System.out.println("----------------------------------------------------------------");
        for (int i = 0; i < f.length; i++) {
            f[i] = MARGEM_2_PADRAO[i] - MARGEM_1_PADRAO[i];
            System.out.printf("   %6.1f   |     %7.2f    |     %7.2f    |    %7.2f%n",
                    X_PADRAO[i], MARGEM_1_PADRAO[i], MARGEM_2_PADRAO[i], f[i]);
        }
        System.out.println("----------------------------------------------------------------");

        double areaTrapezio = regraTrapeziosRepetida(f, PASSO_PADRAO_H);
        double areaSimpson = regraSimpson13Repetida(f, PASSO_PADRAO_H);

        System.out.printf("  Trapezio repetido : %10.2f m2%n", areaTrapezio);
        System.out.printf("  Simpson repetido  : %10.2f m2%n", areaSimpson);
        System.out.printf("  Diferenca absoluta: %10.2f m2%n", Math.abs(areaSimpson - areaTrapezio));
        System.out.println("----------------------------------------------------------------");
    }

    private static void executarEntradaManual(Scanner scanner) {
        System.out.println("\n>>> Entrada Manual - Integracao Numerica");
        int n = Main.lerInteiro(scanner, "Informe o numero de subintervalos n (n >= 2, par para Simpson 1/3): ", 2, 200);
        double h;
        while (true) {
            h = Main.lerDouble(scanner, "Informe o passo de integracao h (> 0): ");
            if (h > 0) {
                break;
            }
            System.out.println("[!] O passo h deve ser positivo.");
        }

        double[] f = new double[n + 1];
        for (int i = 0; i <= n; i++) {
            f[i] = Main.lerDouble(scanner, String.format("  f(x_%d): ", i));
        }

        double areaTrapezio = regraTrapeziosRepetida(f, h);
        System.out.printf("%n  Trapezio repetido: %.4f%n", areaTrapezio);
        if (n % 2 == 0) {
            double areaSimpson = regraSimpson13Repetida(f, h);
            System.out.printf("  Simpson repetido : %.4f%n", areaSimpson);
        } else {
            System.out.println("  [!] Regra 1/3 de Simpson requer numero par de subintervalos.");
        }
    }

    public static double regraTrapeziosRepetida(double[] f, double h) {
        int n = f.length - 1;
        double soma = f[0] + f[n];
        for (int i = 1; i < n; i++) {
            soma += 2 * f[i];
        }
        return (h / 2.0) * soma;
    }

    public static double regraSimpson13Repetida(double[] f, double h) {
        int n = f.length - 1;
        if (n % 2 != 0) {
            throw new IllegalArgumentException("O numero de subintervalos deve ser par.");
        }
        double soma = f[0] + f[n];
        for (int i = 1; i < n; i++) {
            if (i % 2 == 0) {
                soma += 2 * f[i];
            } else {
                soma += 4 * f[i];
            }
        }
        return (h / 3.0) * soma;
    }
}
