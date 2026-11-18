
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Locale;

/**
 * Interface gráfica em Swing para integrar os 4 módulos de Cálculo Numérico.
 * Puxa diretamente os métodos já implementados nas outras classes do pacote.
 */
public final class InterfaceGrafica extends JFrame {

    private static final long serialVersionUID = 1L;

    // Cores e fonte padrão da interface
    private static final Color COR_PRIMARIA = new Color(25, 65, 115);
    private static final Color COR_FUNDO    = new Color(245, 247, 250);
    private static final Font FONTE_MONO    = new Font("Monospaced", Font.PLAIN, 13);

    // Componentes da Aba 01 - Métodos Diretos
    private JSpinner spOrdemDireto;
    private DefaultTableModel modelDireto;
    private JTable tableDireto;
    private JTextArea outDireto;

    // Componentes da Aba 02 - Métodos Iterativos
    private JSpinner spOrdemIterativo;
    private JTextField txtTolerancia;
    private JTextField txtMaxIter;
    private JComboBox<String> cbChuteInicial;
    private DefaultTableModel modelIterativo;
    private JTable tableIterativo;
    private JTextArea outIterativo;

    // Componentes da Aba 03 - Mínimos Quadrados
    private DefaultTableModel modelMMQ;
    private JTable tableMMQ;
    private JTextField txtAnoPrevisao;
    private JTextArea outMMQ;

    // Componentes da Aba 04 - Integração Numérica
    private JComboBox<String> cbModoIntegracao;
    private JTextField txtPassoH;
    private DefaultTableModel modelIntegracao;
    private JTable tableIntegracao;
    private JTextArea outIntegracao;

    public InterfaceGrafica() {
        // Usa ponto como separador decimal na formatação das saídas
        Locale.setDefault(Locale.US);

        setTitle("Cálculo Numérico - UNIVASF | Atividade Complementar");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1020, 680);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);

        // Pede confirmação se o usuário clicar no 'X' da janela
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                confirmarSaida();
            }
        });

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(COR_FUNDO);
        root.setBorder(new EmptyBorder(10, 12, 10, 12));

        // Abas principais com os 4 tópicos
        JTabbedPane abas = new JTabbedPane();
        abas.setFont(new Font("SansSerif", Font.BOLD, 13));
        abas.addTab("1. Métodos Diretos (Gauss)", criarPainelDiretos());
        abas.addTab("2. Métodos Iterativos (Gauss-Seidel)", criarPainelIterativos());
        abas.addTab("3. Mínimos Quadrados (MMQ)", criarPainelMMQ());
        abas.addTab("4. Integração Numérica", criarPainelIntegracao());
        root.add(abas, BorderLayout.CENTER);

        // Barra inferior com dica de digitação e botão de sair
        root.add(criarRodape(), BorderLayout.SOUTH);

        setContentPane(root);

        // Já abre com os dados dos problemas sorteados preenchidos
        carregarPadraoDiretos();
        carregarPadraoIterativos();
        carregarPadraoMMQ();
        carregarPadraoIntegracao();
    }

    private JPanel criarRodape() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(COR_FUNDO);

        JLabel lblDica = new JLabel("Dica: Você pode digitar números usando ponto (.) ou vírgula (,) e alterar as tabelas para novos problemas.");
        lblDica.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblDica.setForeground(Color.DARK_GRAY);

        JButton btnSair = new JButton("Sair do Programa");
        btnSair.setBackground(new Color(180, 45, 45));
        btnSair.setForeground(Color.WHITE);
        btnSair.setFocusPainted(false);
        btnSair.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnSair.addActionListener(e -> confirmarSaida());

        footer.add(lblDica, BorderLayout.WEST);
        footer.add(btnSair, BorderLayout.EAST);
        return footer;
    }

    private void confirmarSaida() {
        int resp = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente encerrar a aplicação de Cálculo Numérico?",
                "Encerrar Programa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (resp == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }

    // -------------------------------------------------------------------------
    // ABA 01: MÉTODOS DIRETOS (ELIMINAÇÃO DE GAUSS)
    // -------------------------------------------------------------------------

    private JPanel criarPainelDiretos() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        controles.setBorder(new TitledBorder("Configuração do Sistema Linear [A | b]"));

        controles.add(new JLabel("Ordem do sistema (n):"));
        spOrdemDireto = new JSpinner(new SpinnerNumberModel(3, 2, 15, 1));
        controles.add(spOrdemDireto);

        JButton btnRedimensionar = new JButton("Aplicar Ordem (n)");
        btnRedimensionar.addActionListener(e ->
                redimensionarTabelaSistema(modelDireto, (int) spOrdemDireto.getValue(), false));
        controles.add(btnRedimensionar);

        JButton btnPadrao = new JButton("Carregar Padrão (Prob. 01.3 - Mineração)");
        btnPadrao.addActionListener(e -> carregarPadraoDiretos());
        controles.add(btnPadrao);

        JButton btnLimpar = new JButton("Limpar / Novo Cálculo");
        btnLimpar.addActionListener(e -> {
            limparTabela(modelDireto);
            outDireto.setText("Campos limpos. Preencha a matriz aumentada [A | b] e clique em 'Resolver Sistema'.");
        });
        controles.add(btnLimpar);

        JButton btnCalcular = new JButton("Resolver Sistema (Gauss)");
        btnCalcular.setBackground(COR_PRIMARIA);
        btnCalcular.setForeground(Color.WHITE);
        btnCalcular.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnCalcular.addActionListener(e -> executarDiretos());
        controles.add(btnCalcular);

        modelDireto = new DefaultTableModel();
        tableDireto = criarTabelaEstilizada(modelDireto);
        outDireto = criarAreaResultado();

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                envolverTabela(tableDireto, "Matriz Aumentada [A | b] (edite as células abaixo)"),
                envolverSaida(outDireto, "Solução Passo a Passo"));
        split.setResizeWeight(0.42);

        panel.add(controles, BorderLayout.NORTH);
        panel.add(split, BorderLayout.CENTER);
        return panel;
    }

    // Preenche a tabela com os dados do Problema 01.3 (Mineração)
    private void carregarPadraoDiretos() {
        spOrdemDireto.setValue(3);
        redimensionarTabelaSistema(modelDireto, 3, false);

        double[][] a = {
                {0.55, 0.25, 0.25}, // Areia
                {0.30, 0.45, 0.20}, // Cascalho fino
                {0.15, 0.30, 0.55}  // Cascalho grosso
        };
        double[] b = {4800.0, 5800.0, 5700.0};

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                modelDireto.setValueAt(a[i][j], i, j);
            }
            modelDireto.setValueAt(b[i], i, 3);
        }
        outDireto.setText("Dados padrão carregados: Problema 01.3 (Mineração - Areia, Cascalho Fino e Cascalho Grosso).\nClique em 'Resolver Sistema (Gauss)' para calcular.");
    }

    private void executarDiretos() {
        pararEdicao(tableDireto);
        int n = modelDireto.getRowCount();
        try {
            double[][] aOrig = new double[n][n];
            double[] bOrig = new double[n];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    aOrig[i][j] = lerCelulaDouble(modelDireto.getValueAt(i, j), "A[" + (i + 1) + "][" + (j + 1) + "]");
                }
                bOrig[i] = lerCelulaDouble(modelDireto.getValueAt(i, n), "b[" + (i + 1) + "]");
            }

            // Fazemos cópia porque o método eliminacaoDeGauss altera a matriz original durante o escalonamento
            double[][] aEscalonada = copiarMatriz(aOrig);
            double[] bEscalonado = bOrig.clone();

            double[] x = MetodosDiretos.eliminacaoDeGauss(aEscalonada, bEscalonado);

            StringBuilder sb = new StringBuilder();
            sb.append("=== MÉTODO DA ELIMINAÇÃO DE GAUSS COM PIVOTEAMENTO PARCIAL ===\n\n");
            sb.append("1) Matriz Aumentada Inicial [A | b]:\n");
            sb.append(formatarMatrizAumentada(aOrig, bOrig)).append("\n");

            sb.append("2) Matriz Triangularizada [U | b']:\n");
            sb.append(formatarMatrizAumentada(aEscalonada, bEscalonado)).append("\n");

            sb.append("3) Vetor Solução Final:\n");
            sb.append("------------------------------------------------------------\n");

            // Se ainda forem os dados do problema da mineração, exibe os nomes das minas junto com x1, x2, x3
            boolean isMineracaoPadrao = (n == 3 && Math.abs(bOrig[0] - 4800.0) < 1e-6 && Math.abs(aOrig[0][0] - 0.55) < 1e-6);
            String[] rotulos = {"x1 (Mina 1 - m³)", "x2 (Mina 2 - m³)", "x3 (Mina 3 - m³)"};
            double soma = 0.0;

            for (int i = 0; i < n; i++) {
                String nome = isMineracaoPadrao ? rotulos[i] : ("x" + (i + 1));
                sb.append(String.format("  %-22s = %14.4f%n", nome, x[i]));
                soma += x[i];
            }
            if (isMineracaoPadrao) {
                sb.append("------------------------------------------------------------\n");
                sb.append(String.format("  %-22s = %14.4f%n", "Volume Total (m³)", soma));
            }
            sb.append("------------------------------------------------------------\n");
            sb.append(String.format("  Resíduo máximo ||Ax - b||_inf: %.4e%n", calcularResiduoMaximo(aOrig, bOrig, x)));

            outDireto.setText(sb.toString());
            outDireto.setCaretPosition(0);

        } catch (IllegalArgumentException | ArithmeticException ex) {
            mostrarErro(ex.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // ABA 02: MÉTODOS ITERATIVOS (GAUSS-SEIDEL)
    // -------------------------------------------------------------------------

    private JPanel criarPainelIterativos() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel controles = new JPanel(new GridLayout(2, 1, 4, 4));
        controles.setBorder(new TitledBorder("Parâmetros do Método de Gauss-Seidel"));

        JPanel linha1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        linha1.add(new JLabel("Ordem (n):"));
        spOrdemIterativo = new JSpinner(new SpinnerNumberModel(6, 2, 15, 1));
        linha1.add(spOrdemIterativo);

        JButton btnRedim = new JButton("Aplicar Ordem (n)");
        btnRedim.addActionListener(e ->
                redimensionarTabelaSistema(modelIterativo, (int) spOrdemIterativo.getValue(), true));
        linha1.add(btnRedim);

        linha1.add(new JLabel("Tolerância (ε):"));
        txtTolerancia = new JTextField("0.0001", 8);
        linha1.add(txtTolerancia);

        linha1.add(new JLabel("Máx. Iterações:"));
        txtMaxIter = new JTextField("1000", 6);
        linha1.add(txtMaxIter);

        linha1.add(new JLabel("Chute x^(0):"));
        cbChuteInicial = new JComboBox<>(new String[]{
                "Vetor Nulo [0, ..., 0]",
                "Quociente Diagonal (b_i / a_ii)",
                "Coluna x^(0) da Tabela"
        });
        linha1.add(cbChuteInicial);

        JPanel linha2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        JButton btnPadrao = new JButton("Carregar Padrão (Prob. 02.2 - Treliça)");
        btnPadrao.addActionListener(e -> carregarPadraoIterativos());
        linha2.add(btnPadrao);

        JButton btnLimpar = new JButton("Limpar / Novo Cálculo");
        btnLimpar.addActionListener(e -> {
            limparTabela(modelIterativo);
            outIterativo.setText("Campos limpos. Preencha o sistema [A | b] e clique em 'Executar Gauss-Seidel'.");
        });
        linha2.add(btnLimpar);

        JButton btnCalcular = new JButton("Executar Gauss-Seidel");
        btnCalcular.setBackground(COR_PRIMARIA);
        btnCalcular.setForeground(Color.WHITE);
        btnCalcular.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnCalcular.addActionListener(e -> executarIterativos());
        linha2.add(btnCalcular);

        controles.add(linha1);
        controles.add(linha2);

        modelIterativo = new DefaultTableModel();
        tableIterativo = criarTabelaEstilizada(modelIterativo);
        outIterativo = criarAreaResultado();

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                envolverTabela(tableIterativo, "Sistema Linear [A | b | x^(0)]"),
                envolverSaida(outIterativo, "Análise de Convergência e Iterações"));
        split.setResizeWeight(0.45);

        panel.add(controles, BorderLayout.NORTH);
        panel.add(split, BorderLayout.CENTER);
        return panel;
    }

    // Preenche a tabela com o sistema 6x6 do Problema 02.2 (Treliça)
    private void carregarPadraoIterativos() {
        spOrdemIterativo.setValue(6);
        redimensionarTabelaSistema(modelIterativo, 6, true);
        txtTolerancia.setText("0.0001");
        txtMaxIter.setText("1000");
        cbChuteInicial.setSelectedIndex(0);

        double c45 = Math.cos(Math.toRadians(45.0));
        double s45 = Math.sin(Math.toRadians(45.0));
        double c60 = Math.cos(Math.toRadians(60.0));
        double s60 = Math.sin(Math.toRadians(60.0));
        double s30 = Math.sin(Math.toRadians(30.0));

        // Equações de equilíbrio dos nós da treliça (F1 a F6)
        double[][] a = {
                { c45, 0.0,  0.0,  1.0,  0.0,  0.0 },
                {-c45, 1.0,  c45,  0.0,  0.0,  0.0 },
                {-s45, 0.0, -s45,  0.0,  0.0,  0.0 },
                { 0.0, 0.0, -c45, -1.0,  c60,  0.0 },
                { 0.0, 0.0,  s45,  0.0,  s60,  0.0 },
                { 0.0, 0.0,  0.0,  0.0, -s60, -s30 }
        };
        double[] b = {0.0, 0.0, 500.0, 0.0, 0.0, 100.0};

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                modelIterativo.setValueAt(a[i][j], i, j);
            }
            modelIterativo.setValueAt(b[i], i, 6);
            modelIterativo.setValueAt(0.0, i, 7);
        }
        outIterativo.setText("Dados padrão carregados: Problema 02.2 (Treliça - Sistema 6x6 com ε = 0.0001).\nClique em 'Executar Gauss-Seidel' para resolver.");
    }

    private void executarIterativos() {
        pararEdicao(tableIterativo);
        int n = modelIterativo.getRowCount();
        try {
            double[][] a = new double[n][n];
            double[] b = new double[n];
            double[] x0 = new double[n];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    a[i][j] = lerCelulaDouble(modelIterativo.getValueAt(i, j), "A[" + (i + 1) + "][" + (j + 1) + "]");
                }
                b[i] = lerCelulaDouble(modelIterativo.getValueAt(i, n), "b[" + (i + 1) + "]");
            }

            double tol = lerCelulaDouble(txtTolerancia.getText(), "Tolerância (ε)");
            if (tol <= 0) {
                throw new IllegalArgumentException("A tolerância deve ser estritamente positiva.");
            }
            int maxIter = Integer.parseInt(txtMaxIter.getText().trim());
            if (maxIter <= 0) {
                throw new IllegalArgumentException("O número máximo de iterações deve ser positivo.");
            }

            // Troca linhas se houver zero na diagonal principal para evitar divisão por zero
            if (!garantirDiagonalNaoNula(a, b)) {
                throw new ArithmeticException("Não foi possível eliminar zeros da diagonal principal por troca de linhas.");
            }

            // Monta o vetor inicial x^(0) conforme a opção escolhida
            int modoChute = cbChuteInicial.getSelectedIndex();
            for (int i = 0; i < n; i++) {
                if (modoChute == 0) {
                    x0[i] = 0.0;
                } else if (modoChute == 1) {
                    x0[i] = b[i] / a[i][i];
                } else {
                    x0[i] = lerCelulaDouble(modelIterativo.getValueAt(i, n + 1), "x^(0)[" + (i + 1) + "]");
                }
            }

            // Verifica critérios de convergência e roda o Gauss-Seidel
            double maxAlpha = MetodosIterativos.verificarCriterioLinhas(a);
            double maxBeta = MetodosIterativos.verificarCriterioSassenfeld(a);
            MetodosIterativos.ResultadoIterativo res =
                    MetodosIterativos.executarGaussSeidelDetalhado(a, b, x0, tol, maxIter);

            StringBuilder sb = new StringBuilder();
            sb.append("=== MÉTODO ITERATIVO DE GAUSS-SEIDEL ===\n\n");
            sb.append("1) Análise de Convergência:\n");
            sb.append(String.format("  Critério das Linhas (max α)     : %.4f %s%n",
                    maxAlpha, (maxAlpha < 1.0 ? "[SATISFEITO]" : "[>= 1]")));
            sb.append(String.format("  Critério de Sassenfeld (max β)  : %.4f %s%n\n",
                    maxBeta, (maxBeta < 1.0 ? "[SATISFEITO]" : "[>= 1]")));

            sb.append("2) Resultado das Iterações:\n");
            sb.append("------------------------------------------------------------\n");
            sb.append("  Status               : ").append(res.convergiu ? "CONVERGIU COM SUCESSO" : "NÃO CONVERGIU NO LIMITE").append("\n");
            sb.append(String.format("  Iterações realizadas : %d%n", res.iteracoes));
            sb.append(String.format("  Erro máximo final    : %.6e (Tolerância: %.6f)%n", res.erroFinal, tol));
            sb.append("------------------------------------------------------------\n");

            // No caso da treliça, mostra também se a barra está sob tração ou compressão
            boolean isTrelica = (n == 6 && Math.abs(b[2] - 500.0) < 1e-6 && Math.abs(b[5] - 100.0) < 1e-6);
            for (int i = 0; i < n; i++) {
                if (isTrelica) {
                    String estado = (res.solucao[i] > 1e-6) ? "(Tração)"
                            : (res.solucao[i] < -1e-6) ? "(Compressão)" : "(Nula)";
                    sb.append(String.format("  F%d = %12.4f  %s%n", i + 1, res.solucao[i], estado));
                } else {
                    sb.append(String.format("  x%d = %12.4f%n", i + 1, res.solucao[i]));
                }
            }
            sb.append("------------------------------------------------------------\n");

            outIterativo.setText(sb.toString());
            outIterativo.setCaretPosition(0);

        } catch (NumberFormatException ex) {
            mostrarErro("O número máximo de iterações deve ser um inteiro válido.");
        } catch (IllegalArgumentException | ArithmeticException ex) {
            mostrarErro(ex.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // ABA 03: MÍNIMOS QUADRADOS (LEI DE MOORE / LOG10)
    // -------------------------------------------------------------------------

    private JPanel criarPainelMMQ() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        controles.setBorder(new TitledBorder("Ajuste de Curvas - Mínimos Quadrados com Transformação log10(N)"));

        JButton btnAddLinha = new JButton("+ Ponto");
        btnAddLinha.addActionListener(e ->
                modelMMQ.addRow(new Object[]{"Ponto " + (modelMMQ.getRowCount() + 1), "", ""}));
        controles.add(btnAddLinha);

        JButton btnRemLinha = new JButton("- Remover Ponto");
        btnRemLinha.addActionListener(e -> {
            if (modelMMQ.getRowCount() > 2) {
                modelMMQ.removeRow(modelMMQ.getRowCount() - 1);
            } else {
                mostrarErro("São necessários pelo menos 2 pontos para o ajuste linear.");
            }
        });
        controles.add(btnRemLinha);

        controles.add(new JLabel("Prever para X/Ano:"));
        txtAnoPrevisao = new JTextField("2025", 6);
        controles.add(txtAnoPrevisao);

        JButton btnPadrao = new JButton("Carregar Padrão (Prob. 03.1 - Lei de Moore)");
        btnPadrao.addActionListener(e -> carregarPadraoMMQ());
        controles.add(btnPadrao);

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(e -> {
            limparTabela(modelMMQ);
            outMMQ.setText("Campos limpos. Informe os pares (X, N) e clique em 'Ajustar Curva (MMQ)'.");
        });
        controles.add(btnLimpar);

        JButton btnCalcular = new JButton("Ajustar Curva (MMQ)");
        btnCalcular.setBackground(COR_PRIMARIA);
        btnCalcular.setForeground(Color.WHITE);
        btnCalcular.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnCalcular.addActionListener(e -> executarMMQ());
        controles.add(btnCalcular);

        modelMMQ = new DefaultTableModel(new String[]{"Rótulo / Chip (Opcional)", "X (Ano)", "N (Transistores > 0)"}, 0);
        tableMMQ = criarTabelaEstilizada(modelMMQ);
        outMMQ = criarAreaResultado();

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                envolverTabela(tableMMQ, "Tabela de Pontos Experimentais"),
                envolverSaida(outMMQ, "Equação Ajustada e Previsões"));
        split.setResizeWeight(0.45);

        panel.add(controles, BorderLayout.NORTH);
        panel.add(split, BorderLayout.CENTER);
        return panel;
    }

    // Preenche a tabela com os 11 processadores do Problema 03.1 (Lei de Moore)
    private void carregarPadraoMMQ() {
        modelMMQ.setRowCount(0);
        String[] chips = {"4004", "8008", "8080", "8086", "80286", "80386", "80486", "Pentium", "Pentium II", "Pentium III", "Pentium 4"};
        double[] anos = {1971, 1972, 1974, 1978, 1982, 1986, 1989, 1993, 1997, 1999, 2000};
        double[] trans = {2250, 3300, 6000, 29000, 134000, 275000, 1200000, 3100000, 7500000, 9500000, 42000000};

        for (int i = 0; i < anos.length; i++) {
            modelMMQ.addRow(new Object[]{chips[i], anos[i], trans[i]});
        }
        txtAnoPrevisao.setText("2026");
        outMMQ.setText("Dados padrão carregados: Problema 03.1 (Lei de Moore - 11 processadores).\nClique em 'Ajustar Curva (MMQ)' para calcular.");
    }

    private void executarMMQ() {
        pararEdicao(tableMMQ);
        int m = modelMMQ.getRowCount();
        try {
            if (m < 2) {
                throw new IllegalArgumentException("Insira pelo menos 2 pontos válidos.");
            }
            String[] rotulos = new String[m];
            double[] x = new double[m];
            double[] nVal = new double[m];
            double[] y = new double[m];

            for (int i = 0; i < m; i++) {
                Object rot = modelMMQ.getValueAt(i, 0);
                rotulos[i] = (rot == null || rot.toString().trim().isEmpty()) ? ("Ponto " + (i + 1)) : rot.toString().trim();
                x[i] = lerCelulaDouble(modelMMQ.getValueAt(i, 1), "X[" + (i + 1) + "]");
                nVal[i] = lerCelulaDouble(modelMMQ.getValueAt(i, 2), "N[" + (i + 1) + "]");

                // Não existe log10 de zero ou número negativo
                if (nVal[i] <= 0.0) {
                    throw new IllegalArgumentException("O valor N[" + (i + 1) + "] deve ser estritamente positivo para aplicar log10(N).");
                }
                y[i] = Math.log10(nVal[i]); // Linearização
            }

            // Eq. normais: retorna [c, a] da reta y = c + a*x
            double[] coef = MinimosQuadrados.ajustarRetaMMQ(x, y);
            double c = coef[0];
            double a = coef[1];

            StringBuilder sb = new StringBuilder();
            sb.append("=== REGRESSÃO LINEAR POR MÍNIMOS QUADRADOS (y = log10(N)) ===\n\n");
            sb.append("  Rótulo       |     X     |         N          |   y = log10(N)  |  y_ajustado\n");
            sb.append("-------------------------------------------------------------------------------\n");

            double erroQuad = 0.0;
            for (int i = 0; i < m; i++) {
                double yEst = a * x[i] + c;
                erroQuad += Math.pow(y[i] - yEst, 2);
                sb.append(String.format("  %-12s | %9.2f | %18.2f | %15.5f | %11.5f%n",
                        rotulos[i], x[i], nVal[i], y[i], yEst));
            }
            sb.append("-------------------------------------------------------------------------------\n");
            sb.append(String.format("  Equação ajustada  : log10(N) = %.5f * X + (%.5f)%n", a, c));
            sb.append(String.format("  Tempo de dobra    : Dobra a cada %.2f unidades de X (anos)%n", Math.log10(2.0) / a));
            sb.append(String.format("  Erro quadrático   : %.6f%n", erroQuad));
            sb.append("-------------------------------------------------------------------------------\n");

            // Previsões fixas pedidas no problema (2010 e 2020)
            for (int anoPadrao : new int[]{2010, 2020}) {
                double logPrev = a * anoPadrao + c;
                sb.append(String.format("  Previsão %d     : %.3e (log10(N) = %.5f)%n",
                        anoPadrao, Math.pow(10.0, logPrev), logPrev));
            }

            // Previsão extra para o valor que o usuário digitou no campo
            if (!txtAnoPrevisao.getText().trim().isEmpty()) {
                double xExtra = lerCelulaDouble(txtAnoPrevisao.getText(), "Prever para X/Ano");
                double logExtra = a * xExtra + c;
                sb.append(String.format("  Previsão %.1f   : %.3e (log10(N) = %.5f)%n",
                        xExtra, Math.pow(10.0, logExtra), logExtra));
            }

            outMMQ.setText(sb.toString());
            outMMQ.setCaretPosition(0);

        } catch (IllegalArgumentException ex) {
            mostrarErro(ex.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // ABA 04: INTEGRAÇÃO NUMÉRICA (TRAPÉZIOS E SIMPSON 1/3)
    // -------------------------------------------------------------------------

    private JPanel criarPainelIntegracao() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel controles = new JPanel(new GridLayout(2, 1, 4, 4));
        controles.setBorder(new TitledBorder("Integração Numérica — Regras dos Trapézios e 1/3 de Simpson Repetidas"));

        JPanel linha1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        linha1.add(new JLabel("Modo de Entrada:"));
        cbModoIntegracao = new JComboBox<>(new String[]{
                "Duas Margens M1(x) e M2(x)  [f(x) = M2 - M1]",
                "Função Direta f(x_i)"
        });
        cbModoIntegracao.addActionListener(e -> alternarModoIntegracao());
        linha1.add(cbModoIntegracao);

        linha1.add(new JLabel("Passo (h > 0):"));
        txtPassoH = new JTextField("10.0", 7);
        linha1.add(txtPassoH);

        JButton btnAdd = new JButton("+ Ponto");
        btnAdd.addActionListener(e -> {
            int idx = modelIntegracao.getRowCount();
            if (cbModoIntegracao.getSelectedIndex() == 0) {
                modelIntegracao.addRow(new Object[]{idx, "", ""});
            } else {
                modelIntegracao.addRow(new Object[]{idx, ""});
            }
        });
        linha1.add(btnAdd);

        JButton btnRem = new JButton("- Remover Ponto");
        btnRem.addActionListener(e -> {
            if (modelIntegracao.getRowCount() > 3) {
                modelIntegracao.removeRow(modelIntegracao.getRowCount() - 1);
            } else {
                mostrarErro("São necessários pelo menos 3 pontos (n >= 2 subintervalos).");
            }
        });
        linha1.add(btnRem);

        JPanel linha2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        JButton btnPadrao = new JButton("Carregar Padrão (Prob. 04.2 - Área do Rio)");
        btnPadrao.addActionListener(e -> carregarPadraoIntegracao());
        linha2.add(btnPadrao);

        JButton btnLimpar = new JButton("Limpar / Novo Cálculo");
        btnLimpar.addActionListener(e -> {
            limparTabela(modelIntegracao);
            outIntegracao.setText("Campos limpos. Informe o passo h, preencha os pontos e clique em 'Calcular Integral'.");
        });
        linha2.add(btnLimpar);

        JButton btnCalcular = new JButton("Calcular Integral");
        btnCalcular.setBackground(COR_PRIMARIA);
        btnCalcular.setForeground(Color.WHITE);
        btnCalcular.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnCalcular.addActionListener(e -> executarIntegracao());
        linha2.add(btnCalcular);

        controles.add(linha1);
        controles.add(linha2);

        modelIntegracao = new DefaultTableModel();
        tableIntegracao = criarTabelaEstilizada(modelIntegracao);
        outIntegracao = criarAreaResultado();

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                envolverTabela(tableIntegracao, "Pontos Igualmente Espaçados (n = total de linhas - 1)"),
                envolverSaida(outIntegracao, "Resultados das Regras de Integração"));
        split.setResizeWeight(0.45);

        panel.add(controles, BorderLayout.NORTH);
        panel.add(split, BorderLayout.CENTER);
        return panel;
    }

    // Troca as colunas da tabela entre modo de duas margens (M1, M2) e função direta f(x)
    private void alternarModoIntegracao() {
        int linhasAtuais = Math.max(5, modelIntegracao.getRowCount());
        if (cbModoIntegracao.getSelectedIndex() == 0) {
            modelIntegracao.setColumnIdentifiers(new String[]{"x_i (Opcional)", "M1(x_i)", "M2(x_i)"});
        } else {
            modelIntegracao.setColumnIdentifiers(new String[]{"x_i (Opcional)", "f(x_i)"});
        }
        modelIntegracao.setRowCount(linhasAtuais);
    }

    // Preenche a tabela com os dados do Problema 04.2 (Área do Rio)
    private void carregarPadraoIntegracao() {
        cbModoIntegracao.setSelectedIndex(0);
        modelIntegracao.setColumnIdentifiers(new String[]{"x_i (m)", "M1(x_i) (m)", "M2(x_i) (m)"});
        modelIntegracao.setRowCount(0);

        txtPassoH.setText("10.0");
        double[] x = {0.0, 10.0, 20.0, 30.0, 40.0};
        double[] m1 = {50.8, 86.2, 136.0, 72.8, 51.0};
        double[] m2 = {113.6, 144.5, 185.0, 171.2, 95.3};

        for (int i = 0; i < x.length; i++) {
            modelIntegracao.addRow(new Object[]{x[i], m1[i], m2[i]});
        }
        outIntegracao.setText("Dados padrão carregados: Problema 04.2 (Área da Superfície do Rio no intervalo [0, 40]).\nClique em 'Calcular Integral' para resolver.");
    }

    private void executarIntegracao() {
        pararEdicao(tableIntegracao);
        int totalPontos = modelIntegracao.getRowCount();
        int n = totalPontos - 1; // Quantidade de subintervalos
        try {
            if (n < 2) {
                throw new IllegalArgumentException("Informe pelo menos 3 pontos (n >= 2 subintervalos).");
            }
            double h = lerCelulaDouble(txtPassoH.getText(), "Passo (h)");
            if (h <= 0.0) {
                throw new IllegalArgumentException("O passo de integração h deve ser positivo.");
            }

            double[] f = new double[totalPontos];
            boolean modoDuasMargens = (cbModoIntegracao.getSelectedIndex() == 0);

            StringBuilder sb = new StringBuilder();
            sb.append("=== INTEGRAÇÃO NUMÉRICA (TRAPÉZIOS E 1/3 DE SIMPSON REPETIDAS) ===\n\n");
            sb.append(String.format("  Subintervalos (n) : %d | Passo (h) : %.4f%n%n", n, h));

            if (modoDuasMargens) {
                sb.append("   i  |    x_i    |   M1(x_i)  |   M2(x_i)  |  f(x_i) = M2 - M1\n");
                sb.append("-----------------------------------------------------------------\n");
                for (int i = 0; i < totalPontos; i++) {
                    Object xiObj = modelIntegracao.getValueAt(i, 0);
                    String xiStr = (xiObj == null || xiObj.toString().trim().isEmpty()) ? String.valueOf(i) : xiObj.toString().trim();
                    double m1 = lerCelulaDouble(modelIntegracao.getValueAt(i, 1), "M1[" + i + "]");
                    double m2 = lerCelulaDouble(modelIntegracao.getValueAt(i, 2), "M2[" + i + "]");
                    f[i] = m2 - m1;
                    sb.append(String.format("  %2d  | %9s | %10.4f | %10.4f | %14.4f%n", i, xiStr, m1, m2, f[i]));
                }
            } else {
                sb.append("   i  |    x_i    |      f(x_i)\n");
                sb.append("---------------------------------------\n");
                for (int i = 0; i < totalPontos; i++) {
                    Object xiObj = modelIntegracao.getValueAt(i, 0);
                    String xiStr = (xiObj == null || xiObj.toString().trim().isEmpty()) ? String.valueOf(i) : xiObj.toString().trim();
                    f[i] = lerCelulaDouble(modelIntegracao.getValueAt(i, 1), "f[" + i + "]");
                    sb.append(String.format("  %2d  | %9s | %14.4f%n", i, xiStr, f[i]));
                }
            }

            double areaTrapezio = IntegracaoNumerica.regraTrapeziosRepetida(f, h);
            sb.append("\n-----------------------------------------------------------------\n");
            sb.append(String.format("  Regra dos Trapézios Repetida : %12.4f%n", areaTrapezio));

            // Simpson 1/3 só pode ser aplicado quando n é par
            if (n % 2 == 0) {
                double areaSimpson = IntegracaoNumerica.regraSimpson13Repetida(f, h);
                sb.append(String.format("  Regra 1/3 de Simpson Repetida: %12.4f%n", areaSimpson));
                sb.append(String.format("  Diferença Absoluta           : %12.4f%n", Math.abs(areaSimpson - areaTrapezio)));
            } else {
                sb.append("  Regra 1/3 de Simpson Repetida: [Indisponível — n deve ser PAR]\n");
            }
            sb.append("-----------------------------------------------------------------\n");

            outIntegracao.setText(sb.toString());
            outIntegracao.setCaretPosition(0);

        } catch (IllegalArgumentException ex) {
            mostrarErro(ex.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // MÉTODOS AUXILIARES
    // -------------------------------------------------------------------------

    private JTable criarTabelaEstilizada(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(24);
        table.setFont(FONTE_MONO);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.setGridColor(new Color(210, 215, 222));
        table.setShowGrid(true);
        return table;
    }

    private JTextArea criarAreaResultado() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(FONTE_MONO);
        area.setBackground(new Color(252, 253, 255));
        area.setBorder(new EmptyBorder(8, 10, 8, 10));
        return area;
    }

    private JScrollPane envolverTabela(JTable table, String titulo) {
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new TitledBorder(titulo));
        return scroll;
    }

    private JScrollPane envolverSaida(JTextArea area, String titulo) {
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(new TitledBorder(titulo));
        return scroll;
    }

    private void redimensionarTabelaSistema(DefaultTableModel model, int n, boolean incluirColunaChute) {
        int cols = incluirColunaChute ? (n + 2) : (n + 1);
        String[] headers = new String[cols];
        for (int j = 0; j < n; j++) {
            headers[j] = "x" + (j + 1);
        }
        headers[n] = "b (Termo Indep.)";
        if (incluirColunaChute) {
            headers[n + 1] = "x^(0)";
        }
        model.setColumnIdentifiers(headers);
        model.setRowCount(n);
    }

    private void limparTabela(DefaultTableModel model) {
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                model.setValueAt("", i, j);
            }
        }
    }

    // Evita perder o que o usuário digitou se ele clicar em calcular sem apertar Enter antes
    private void pararEdicao(JTable table) {
        if (table.isEditing() && table.getCellEditor() != null) {
            table.getCellEditor().stopCellEditing();
        }
    }

    // Lê o valor da célula aceitando tanto vírgula quanto ponto decimal
    private double lerCelulaDouble(Object valor, String nomeCampo) {
        if (valor == null || valor.toString().trim().isEmpty()) {
            throw new IllegalArgumentException("O campo '" + nomeCampo + "' está vazio. Preencha todos os valores.");
        }
        String texto = valor.toString().trim().replace(',', '.');
        try {
            double num = Double.parseDouble(texto);
            if (Double.isNaN(num) || Double.isInfinite(num)) {
                throw new IllegalArgumentException("Valor inválido em '" + nomeCampo + "'.");
            }
            return num;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor numérico inválido em '" + nomeCampo + "': \"" + valor + "\"");
        }
    }

    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Aviso / Validação de Dados", JOptionPane.WARNING_MESSAGE);
    }

    private String formatarMatrizAumentada(double[][] a, double[] b) {
        int n = b.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append("  | ");
            for (int j = 0; j < n; j++) {
                sb.append(String.format("%10.4f ", a[i][j]));
            }
            sb.append(String.format("| %12.4f |%n", b[i]));
        }
        return sb.toString();
    }

    private double calcularResiduoMaximo(double[][] a, double[] b, double[] x) {
        int n = b.length;
        double maxErro = 0.0;
        for (int i = 0; i < n; i++) {
            double soma = 0.0;
            for (int j = 0; j < n; j++) {
                soma += a[i][j] * x[j];
            }
            maxErro = Math.max(maxErro, Math.abs(soma - b[i]));
        }
        return maxErro;
    }

    private double[][] copiarMatriz(double[][] origem) {
        double[][] copia = new double[origem.length][];
        for (int i = 0; i < origem.length; i++) {
            copia[i] = origem[i].clone();
        }
        return copia;
    }

    // Troca linhas da matriz se encontrar algum zero na diagonal principal
    private boolean garantirDiagonalNaoNula(double[][] a, double[] b) {
        int n = b.length;
        for (int i = 0; i < n; i++) {
            if (Math.abs(a[i][i]) < 1e-12) {
                int linhaTroca = -1;
                for (int k = 0; k < n; k++) {
                    if (k != i && Math.abs(a[k][i]) > 1e-12 && Math.abs(a[i][k]) > 1e-12) {
                        linhaTroca = k;
                        break;
                    }
                }
                if (linhaTroca == -1) {
                    return false;
                }
                double[] tempA = a[i];
                a[i] = a[linhaTroca];
                a[linhaTroca] = tempA;

                double tempB = b[i];
                b[i] = b[linhaTroca];
                b[linhaTroca] = tempB;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        // Tenta ativar o tema visual Nimbus do Java se estiver disponível
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> new InterfaceGrafica().setVisible(true));
    }
}