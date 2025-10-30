package server.ui;

import common.model.Election;
import server.core.VoteManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class ServerDashboard extends JFrame {

    private final VoteManager voteManager;
    private final Election election;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;

    public ServerDashboard(Election election, VoteManager manager) {
        this.election = election;
        this.voteManager = manager;

        setTitle("Painel do Servidor - Monitoramento da Votação");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        initUI();
        refreshResults();
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
        main.add(totalLabel, BorderLayout.SOUTH);

        add(main);
    }

    /** Atualiza os resultados na tabela em tempo real */
    public void refreshResults() {
        Map<String, Integer> results = voteManager.countVotes();
        tableModel.setRowCount(0); // limpa tabela
        for (Map.Entry<String, Integer> entry : results.entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
        totalLabel.setText("Total de eleitores: " + voteManager.getTotalVoters());
    }
}
