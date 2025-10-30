package server.ui;

import common.model.Election;
import server.core.VoteManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

        setTitle("Painel do Servidor - Monitoramento da Votação");
        setSize(500, 420);
        setLocationRelativeTo(null);
        setResizable(false);

        initUI();
        refreshResults();

        // Garante que ao clicar no X o servidor seja encerrado corretamente
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeServer();
            }
        });
    }

    private void initUI() {
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

        // Botão de encerrar eleição
        JButton endButton = new JButton("Encerrar Eleição");
        endButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        endButton.setBackground(new Color(200, 50, 50));
        endButton.setForeground(Color.WHITE);
        endButton.setFocusPainted(false);
        endButton.addActionListener(e -> closeServer());

        JPanel footer = new JPanel(new BorderLayout(10, 10));
        footer.add(totalLabel, BorderLayout.CENTER);
        footer.add(endButton, BorderLayout.SOUTH);

        main.add(footer, BorderLayout.SOUTH);
        add(main);
    }

    /** Atualiza os resultados na tabela em tempo real */
    public void refreshResults() {
        Map<String, Integer> results = voteManager.countVotes();
        tableModel.setRowCount(0);
        for (Map.Entry<String, Integer> entry : results.entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
        totalLabel.setText("Total de eleitores: " + voteManager.getTotalVoters());
    }

    /** Gera relatório, fecha o servidor e encerra o app */
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
