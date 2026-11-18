import java.util.Scanner;

/**
 * Tópico 03 - Interpolação e Ajuste de Curvas
 * Problema 03.1 (Lei de Moore) — Regressão Linear pelo Método dos Mínimos Quadrados (MMQ).
 */
public class MinimosQuadrados {

    private static final String[] CHIPS_PADRAO = {
        "4004", "8008", "8080", "8086", "80286", "80386",
        "80486", "Pentium", "Pentium II", "Pentium III", "Pentium 4"
    };

    private static final double[] ANOS_PADRAO = {
        1971, 1972, 1974, 1978, 1982, 1986, 1989, 1993, 1997, 1999, 2000
    };

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

    private static void executarCasoPadrao() {
        System.out.println("\n>>> Executando Problema 03.1 (Lei de Moore) com dados padrao...");
        int m = ANOS_PADRAO.length;
        double[] y = new double[m];
        for (int i = 0; i < m; i++) {
            y[i] = Math.log10(TRANSISTORES_PADRAO[i]);
        }

        double[] coef = ajustarRetaMMQ(ANOS_PADRAO, y);
        double c = coef[0];
        double a = coef[1];

        System.out.println("\nTabela de Dados Transformados (y = log10(N)):");
        System.out.println("----------------------------------------------------------------");
        System.out.println("  Chip         |   Ano   |  Transistores (N)  |   log10(N)");
        System.out.println("----------------------------------------------------------------");
        double erroQuadLog = 0.0;
        for (int i = 0; i < m; i++) {
            double yEst = a * ANOS_PADRAO[i] + c;
            erroQuadLog += Math.pow(y[i] - yEst, 2);
            System.out.printf("  %-12s |  %4.0f   |  %16.0f  |   %8.5f%n",
                    CHIPS_PADRAO[i], ANOS_PADRAO[i], TRANSISTORES_PADRAO[i], y[i]);
        }
        System.out.println("----------------------------------------------------------------");
        System.out.printf("  Equacao ajustada : log10(N) = %.5f * ano + (%.5f)%n", a, c);
        System.out.printf("  Tempo de dobra   : Dobra a cada %.2f anos%n", Math.log10(2.0) / a);
        System.out.printf("  Erro quadratico  : %.6f%n", erroQuadLog);
        System.out.println("----------------------------------------------------------------");

        for (int anoPrev : new int[]{2010, 2020}) {
            double logN = a * anoPrev + c;
            System.out.printf("  Previsao %d    : %.3e transistores (log10(N) = %.5f)%n",
                    anoPrev, Math.pow(10.0, logN), logN);
        }
        System.out.println("----------------------------------------------------------------");
    }

    private static void executarEntradaManual(Scanner scanner) {
        System.out.println("\n>>> Entrada Manual - Minimos Quadrados (Transformacao log10)");
        int m = Main.lerInteiro(scanner, "Informe a quantidade de pontos tabelados (m >= 2): ", 2, 100);
        double[] x = new double[m];
        double[] nVal = new double[m];
        double[] y = new double[m];

        for (int i = 0; i < m; i++) {
            x[i] = Main.lerDouble(scanner, String.format("  Ano/X[%d]: ", i + 1));
            while (true) {
                nVal[i] = Main.lerDouble(scanner, String.format("  Valor N[%d] (> 0): ", i + 1));
                if (nVal[i] > 0) {
                    break;
                }
                System.out.println("[!] O valor N deve ser positivo para aplicar log10(N).");
            }
            y[i] = Math.log10(nVal[i]);
        }

        double[] coef = ajustarRetaMMQ(x, y);
        double c = coef[0];
        double a = coef[1];

        System.out.printf("%nEquacao ajustada: log10(N) = %.5f * x + (%.5f)%n", a, c);
        double xPrev = Main.lerDouble(scanner, "Informe um valor de X/Ano para previsao: ");
        double logN = a * xPrev + c;
        System.out.printf("Previsao para %.1f: %.3e%n", xPrev, Math.pow(10.0, logN));
    }

    /**
     * Calcula os coeficientes [c, a] da reta ajustada y = c + a * x pelo Método dos Mínimos Quadrados.
     */
    public static double[] ajustarRetaMMQ(double[] x, double[] y) {
        int m = x.length;
        double sx = 0, sy = 0, sxx = 0, sxy = 0;
        for (int i = 0; i < m; i++) {
            sx  += x[i];
            sy  += y[i];
            sxx += x[i] * x[i];
            sxy += x[i] * y[i];
        }
        double a = (m * sxy - sx * sy) / (m * sxx - sx * sx);
        double c = (sy - a * sx) / m;
        return new double[]{c, a};
    }
}
