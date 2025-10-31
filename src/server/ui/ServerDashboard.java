package server.ui;

import common.model.Election;
import server.core.VoteManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;

public class ServerDashboard extends JFrame {

    private final VoteManager voteManager;
    private final Election election;
    private final ServerSocket serverSocket;

    private DefaultTableModel tableModel;
    private JLabel totalLabel;

    public ServerDashboard(Election election, VoteManager manager, ServerSocket serverSocket) {
        this.election = election;
        this.voteManager = manager;
        this.serverSocket = serverSocket;

        setTitle("Painel do Servidor - Votação Distribuída");
        setSize(600, 470);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Votação", createVotePanel());
        tabs.addTab("Ajuda", createHelpPanel());
        tabs.addTab("Créditos", createCreditsPanel());

        add(tabs);
    }

    private JPanel createVotePanel() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel(election.getQuestion(), SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        main.add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Opção", "Votos"}, 0);
        JTable table = new JTable(tableModel);
        table.setEnabled(false);
        main.add(new JScrollPane(table), BorderLayout.CENTER);

        totalLabel = new JLabel("Total de eleitores: 0", SwingConstants.CENTER);
        totalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JButton endButton = new JButton("Encerrar Eleição");
        endButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        endButton.setBackground(new Color(255, 0, 0));
        endButton.setForeground(Color.WHITE);
        endButton.addActionListener(e -> closeServer());

        JPanel footer = new JPanel(new BorderLayout(10, 10));
        footer.add(totalLabel, BorderLayout.CENTER);
        footer.add(endButton, BorderLayout.SOUTH);

        main.add(footer, BorderLayout.SOUTH);
        return main;
    }

    private JPanel createHelpPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea helpText = new JTextArea(
                "AJUDA E INSTRUÇÕES DO SERVIDOR\n\n" +
                        "Conexão dos clientes:\n" +
                        "   - Cada cliente que executar o programa 'VotingClient'\n" +
                        "     se conectará automaticamente ao servidor.\n" +
                        "   - O cliente verá a pergunta e as opções de voto,\n" +
                        "     e enviará seu voto junto com o CPF.\n\n" +
                        "Atualização em tempo real:\n" +
                        "   - Cada voto recebido é contabilizado automaticamente.\n" +
                        "   - A tabela de resultados e o total de eleitores são\n" +
                        "     atualizados em tempo real no painel de votação.\n\n" +
                        "Encerrando a eleição:\n" +
                        "   - Clique no botão 'Encerrar Eleição' para finalizar o processo.\n" +
                        "   - Um relatório final será gerado automaticamente\n" +
                        "     no diretório do projeto com o nome 'results_<data>.txt'.\n\n" +
                        "Dicas:\n" +
                        "   - Certifique-se de que o servidor esteja em execução antes\n" +
                        "     de iniciar os clientes.\n" +
                        "   - Evite fechar o painel pelo 'X' do Windows. Use o botão\n" +
                        "     'Encerrar Eleição' para garantir o encerramento correto.\n" +
                        "   - Caso um cliente não consiga conectar, verifique se a porta\n" +
                        "     28399 está livre e o firewall permite conexões locais.\n"
        );

        helpText.setEditable(false);
        helpText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        helpText.setBackground(new Color(250, 250, 250));
        helpText.setMargin(new Insets(10, 10, 10, 10));

        panel.add(new JScrollPane(helpText), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCreditsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea info = new JTextArea(
                "Sistema de Votação Distribuída\n\n" +
                        "Desenvolvido por:\n" +
                        "• Gabriel Sorensen M. Traina - 283997\n" +

        );
        info.setEditable(false);
        info.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(new JScrollPane(info), BorderLayout.CENTER);
        return panel;
    }

    public void refreshResults() {
        Map<String, Integer> results = voteManager.countVotes();
        tableModel.setRowCount(0);
        for (Map.Entry<String, Integer> entry : results.entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
        totalLabel.setText("Total de eleitores: " + voteManager.getTotalVoters());
    }

    private void closeServer() {
        try {
            voteManager.generateReport();
            JOptionPane.showMessageDialog(this,
                    "Relatório final gerado com sucesso.\nEncerrando o servidor.",
                    "Encerramento da Eleição", JOptionPane.INFORMATION_MESSAGE);
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Erro ao encerrar servidor: " + e.getMessage());
        } finally {
            dispose();
            System.exit(0);
        }
    }
}
