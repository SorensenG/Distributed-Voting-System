package client.ui;

import client.core.VotingClient;
import common.model.Election;
import common.model.Vote;
import common.utils.CPF;

import javax.swing.*;
import java.awt.*;

public class VotingFrame extends JFrame {

    private final VotingClient client;
    private final Election election;
    private JTextField cpfField;
    private ButtonGroup optionsGroup;
    private JLabel statusLabel;

    public VotingFrame(VotingClient client, Election election) {
        this.client = client;
        this.election = election;

        setTitle("Sistema de Votação Distribuída");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(480, 420);
        setLocationRelativeTo(null);
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
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel(election.getQuestion(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JPanel cpfPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cpfPanel.setBorder(BorderFactory.createTitledBorder("Digite seu CPF"));
        cpfField = new JTextField(15);
        cpfPanel.add(cpfField);
        centerPanel.add(cpfPanel);

        JPanel optionsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Escolha sua opção"));
        optionsGroup = new ButtonGroup();

        for (String option : election.getOptions()) {
            JRadioButton radio = new JRadioButton(option);
            radio.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            optionsGroup.add(radio);
            optionsPanel.add(radio);
        }

        centerPanel.add(optionsPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new BorderLayout(5, 5));

        JButton voteButton = new JButton("Enviar Voto");
        voteButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        voteButton.setBackground(new Color(0, 120, 215));
        voteButton.setForeground(Color.WHITE);
        voteButton.setFocusPainted(false);
        voteButton.addActionListener(e -> sendVote());
        footerPanel.add(voteButton, BorderLayout.CENTER);

        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerPanel.add(statusLabel, BorderLayout.SOUTH);

        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel createHelpPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea helpText = new JTextArea(
                "AJUDA AO ELEITOR\n\n" +
                        "• Como votar:\n" +
                        "  1. Digite seu CPF no campo indicado.\n" +
                        "  2. Escolha uma das opções apresentadas.\n" +
                        "  3. Clique no botão 'Enviar Voto'.\n\n" +
                        "• Após enviar o voto:\n" +
                        "  - Seu voto será computado imediatamente.\n" +
                        "  - Uma mensagem de confirmação aparecerá.\n" +
                        "  - A janela será encerrada automaticamente após alguns segundos.\n\n" +
                        "• Erros comuns:\n" +
                        "  • CPF inválido — digite apenas números e verifique se é um CPF real.\n" +
                        "  • Nenhuma opção marcada — selecione uma antes de enviar.\n" +
                        "  • Conexão recusada — o servidor pode não estar ativo. Verifique com o responsável.\n\n" +
                        "• Dicas:\n" +
                        "  - Cada CPF pode votar apenas uma vez.\n" +
                        "  - Não feche a janela durante o envio do voto.\n" +
                        "  - Aguarde a confirmação na tela antes de sair.\n"
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
        info.setBackground(new Color(250, 250, 250));
        info.setMargin(new Insets(10, 10, 10, 10));

        panel.add(new JScrollPane(info), BorderLayout.CENTER);
        return panel;
    }

    private void sendVote() {
        try {
            String cpfInput = cpfField.getText().trim();
            if (cpfInput.isEmpty()) {
                showError("Digite seu CPF.");
                return;
            }

            CPF cpf;
            try {
                cpf = CPF.create(cpfInput);
            } catch (IllegalArgumentException ex) {
                showError("CPF inválido: " + ex.getMessage());
                return;
            }

            String selectedOption = null;
            for (AbstractButton button : java.util.Collections.list(optionsGroup.getElements())) {
                if (button.isSelected()) {
                    selectedOption = button.getText();
                    break;
                }
            }

            if (selectedOption == null) {
                showError("Selecione uma opção.");
                return;
            }

            Vote vote = new Vote(cpf.toString(), selectedOption);
            String response = client.sendVote(vote);
            showSuccess(response);

            Timer timer = new Timer(5000, e -> {
                try {
                    client.close();
                } catch (Exception ex) {
                    System.err.println("Erro ao fechar cliente: " + ex.getMessage());
                }
                dispose();
                System.exit(0);
            });
            timer.setRepeats(false);
            timer.start();

        } catch (Exception e) {
            showError("Erro ao enviar voto: " + e.getMessage());
        }
    }

    private void showError(String msg) {
        statusLabel.setForeground(Color.RED);
        statusLabel.setText(msg);
    }

    private void showSuccess(String msg) {
        statusLabel.setForeground(new Color(0, 128, 0));
        statusLabel.setText(msg + " (a janela será fechada...)");
    }
}
