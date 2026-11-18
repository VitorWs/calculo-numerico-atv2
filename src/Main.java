import java.util.Locale;
import java.util.Scanner;

/**
 * Ponto de entrada principal da aplicação de Cálculo Numérico (UNIVASF).
 * Gerencia o menu interativo centralizado e o roteamento para os 4 módulos.
 */
public class Main {

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner scanner = new Scanner(System.in);
        int opcao;

        exibirCabecalhoSistema();

        do {
            exibirMenuPrincipal();
            opcao = lerInteiro(scanner, "Escolha uma opcao [0-4]: ", 0, 4);

            switch (opcao) {
                case 1:
                    MetodosDiretos.executarMenu(scanner);
                    break;
                case 2:
                    MetodosIterativos.executarMenu(scanner);
                    break;
                case 3:
                    MinimosQuadrados.executarMenu(scanner);
                    break;
                case 4:
                    IntegracaoNumerica.executarMenu(scanner);
                    break;
                case 0:
                    System.out.println("\n================================================================");
                    System.out.println("  Encerrando o sistema de Calculo Numerico. Ate logo!");
                    System.out.println("================================================================");
                    break;
                default:
                    System.out.println("[!] Opcao invalida.");
                    break;
            }
        } while (opcao != 0);

        scanner.close();
    }

    private static void exibirCabecalhoSistema() {
        System.out.println("================================================================");
        System.out.println("          UNIVERSIDADE FEDERAL DO VALE DO SAO FRANCISCO         ");
        System.out.println("             CALCULO NUMERICO - ATIVIDADE COMPLEMENTAR          ");
        System.out.println("                   Prof. Jorge Cavalcanti                       ");
        System.out.println("================================================================");
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n+--------------------------------------------------------------+");
        System.out.println("|                        MENU PRINCIPAL                        |");
        System.out.println("+--------------------------------------------------------------+");
        System.out.println("|  1. Topico 01 - Sistemas Lineares (Metodos Diretos)          |");
        System.out.println("|                 [Eliminacao de Gauss - Prob. 01.3 Mineracao] |");
        System.out.println("|  2. Topico 02 - Sistemas Lineares (Metodos Iterativos)       |");
        System.out.println("|                 [Gauss-Seidel - Prob. 02.2 Trelica]          |");
        System.out.println("|  3. Topico 03 - Interpolacao e Ajuste de Curvas              |");
        System.out.println("|                 [Minimos Quadrados - Prob. 03.1 Lei de Moore]|");
        System.out.println("|  4. Topico 04 - Integracao Numerica                          |");
        System.out.println("|                 [Trapezios e Simpson 1/3 - Prob. 04.2 Rio]   |");
        System.out.println("|  0. Sair do Programa                                         |");
        System.out.println("+--------------------------------------------------------------+");
    }

    /**
     * Exibe o submenu padrão de cada módulo (Dados padrão vs. Inserção manual).
     */
    public static int exibirSubMenuModulo(Scanner scanner, String tituloModulo, String descricaoPadrao) {
        System.out.println("\n================================================================");
        System.out.println("  " + tituloModulo);
        System.out.println("================================================================");
        System.out.println("  1. Executar com dados padrao (" + descricaoPadrao + ")");
        System.out.println("  2. Inserir novos dados manualmente");
        System.out.println("  0. Voltar ao Menu Principal");
        System.out.println("----------------------------------------------------------------");
        return lerInteiro(scanner, "Escolha uma opcao [0-2]: ", 0, 2);
    }

    /**
     * Lê um número inteiro validado dentro do intervalo [min, max].
     */
    public static int lerInteiro(Scanner scanner, String mensagem, int min, int max) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().replace("\uFEFF", "").trim();
            try {
                int valor = Integer.parseInt(entrada);
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.printf("[!] Digite um numero inteiro entre %d e %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("[!] Entrada invalida. Digite um numero inteiro valido.");
            }
        }
    }

    /**
     * Lê um número real (double) aceitando tanto ponto quanto vírgula como separador decimal.
     */
    public static double lerDouble(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().replace("\uFEFF", "").trim().replace(',', '.');
            try {
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.println("[!] Entrada invalida. Digite um valor numerico valido.");
            }
        }
    }

    /**
     * Pausa a execução aguardando o usuário pressionar ENTER para continuar.
     */
    public static void aguardarEnter(Scanner scanner) {
        System.out.print("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }
}
