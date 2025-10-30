package cliente.core;

import common.model.Election;
import common.model.Vote;
import common.network.Message;

import javax.swing.*; // necessário para SwingUtilities
import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import static common.network.Message.MessageType.*;

public class VotingClient {

    private final String host = "127.0.0.1";
    private final int port = 28399;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    // ==========================
    // MODO PRINCIPAL
    // ==========================
    public static void main(String[] args) {
        new VotingClient().start();
    }

    public void start() {
        try {
            Election election = connect();
            System.out.println("✅ Conectado ao servidor!");

            // 🔹 Abre a janela Swing automaticamente
            SwingUtilities.invokeLater(() -> {
                cliente.ui.VotingFrame frame = new cliente.ui.VotingFrame(this, election);
                frame.setVisible(true);
            });

            // 💬 Mantém também o modo terminal (opcional)
            Scanner sc = new Scanner(System.in);

            System.out.println("\n🗳️ " + election.getQuestion());
            List<String> options = election.getOptions();

            for (int i = 0; i < options.size(); i++) {
                System.out.println((i + 1) + " - " + options.get(i));
            }

            System.out.print("\nDigite seu CPF (somente números): ");
            String cpf = sc.nextLine();

            int choice = -1;
            while (choice < 1 || choice > options.size()) {
                System.out.print("Escolha uma opção (1-" + options.size() + "): ");
                if (sc.hasNextInt()) {
                    choice = sc.nextInt();
                    sc.nextLine();
                } else {
                    sc.nextLine();
                    System.out.println("❌ Opção inválida! Digite um número.");
                }
            }

            String option = options.get(choice - 1);
            Vote vote = new Vote(cpf, option);

            String response = sendVote(vote);
            System.out.println("\n📩 Resposta do servidor: " + response);

            close();

        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    // ==========================
    // MÉTODOS USADOS PELA GUI
    // ==========================

    /**
     * Conecta ao servidor e retorna a eleição carregada.
     */
    public Election connect() throws IOException, ClassNotFoundException {
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        Message message = (Message) in.readObject();
        if (message.getType() == ELECTION_DATA) {
            return (Election) message.getData();
        } else {
            throw new IOException("Mensagem inesperada recebida do servidor.");
        }
    }

    /**
     * Envia um voto e retorna a resposta textual do servidor.
     */
    public String sendVote(Vote vote) throws IOException, ClassNotFoundException {
        if (out == null || in == null) {
            throw new IllegalStateException("Cliente não conectado ao servidor.");
        }

        out.writeObject(new Message(VOTE_SUBMISSION, vote));
        Message response = (Message) in.readObject();

        return switch (response.getType()) {
            case SERVER_RESPONSE -> "✅ " + response.getData();
            case ERROR -> "❌ " + response.getData();
            case END_ELECTION -> "🛑 " + response.getData();
            default -> "⚠️ Resposta desconhecida do servidor.";
        };
    }

    /**
     * Fecha a conexão com o servidor.
     */
    public void close() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
        }
    }
}
