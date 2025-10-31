package server.core;

import common.model.Election;
import server.ui.ServerDashboard;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class VotingServer {

    private final int port = 28399;
    private Election election;
    private VoteManager voteManager;
    private ServerDashboard dashboard;
    private ServerSocket serverSocket;

    public static void main(String[] args) {
        new VotingServer().startServer();
    }

    public void startServer() {
        System.out.println("Servidor de votação distribuída iniciado na porta " + port + "\n");

        election = new Election(
                "Qual é a sua linguagem de programação favorita?",
                List.of("Java", "C++", "C", "Python", "JavaScript")
        );

        voteManager = new VoteManager(election);

        try {
            serverSocket = new ServerSocket(port);

            dashboard = new ServerDashboard(election, voteManager, serverSocket);
            dashboard.setVisible(true);

            System.out.println("Servidor ouvindo na porta " + port + "...");
            System.out.println("Acompanhe a votação no painel. Para encerrar, use o botão na janela.\n");

            Thread acceptThread = new Thread(() -> {
                while (!serverSocket.isClosed()) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        System.out.println("Novo cliente conectado: " + clientSocket.getInetAddress());
                        new Thread(new ClientHandler(clientSocket, election, voteManager, dashboard)).start();
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
