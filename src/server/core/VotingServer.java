package server.core;

import common.model.Election;
import server.ui.ServerDashboard;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class VotingServer {

    private final int port = 28399;
    private Election votacao;
    private VoteManager voteManager;
    private ServerDashboard dashboard;
    private ServerSocket serverSocket;

    public static void main(String[] args) {
        new VotingServer().startServer();
    }

    public void startServer() {
        System.out.println("Servidor de votação distribuída iniciado na porta " + port + "\n");

        // Cria a eleição
        votacao = new Election(
                "Qual é a sua linguagem de programação favorita?",
                List.of("Java", "C++", "C", "Python", "JavaScript")
        );

        voteManager = new VoteManager(votacao);

        try {
            // 🔹 Cria e mantém o socket aberto até o encerramento manual
            serverSocket = new ServerSocket(port);

            // 🔹 Inicializa e exibe o painel do servidor
            dashboard = new ServerDashboard(votacao, voteManager, serverSocket);
            dashboard.setVisible(true);

            System.out.println("Servidor ouvindo na porta " + port + "...");
            System.out.println("Acompanhe a votação no painel. Para encerrar, use o botão na janela.\n");

            // 🔹 Thread para aceitar conexões
            Thread acceptThread = new Thread(() -> {
                while (!serverSocket.isClosed()) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        System.out.println("Novo cliente conectado: " + clientSocket.getInetAddress());
                        new Thread(new ClientHandler(clientSocket, votacao, voteManager, dashboard)).start();
                    } catch (IOException e) {
                        if (!serverSocket.isClosed()) {
                            System.err.println("Erro ao aceitar cliente: " + e.getMessage());
                        }
                        break;
                    }
                }
            });

            acceptThread.start();

        } catch (IOException e) {
            System.err.println("Erro ao iniciar servidor: " + e.getMessage());
        }
    }
}
