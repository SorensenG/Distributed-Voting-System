package server.core;

import common.model.Election;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class VotingServer {

    private final int port = 28399;
    private Election votacao;
    private VoteManager voteManager;

    public static void main(String[] args) {
        new VotingServer().startServer();
    }

    public void startServer() {
        System.out.println("🗳️ Servidor de votação distribuída iniciado na porta " + port + "\n");


        votacao = new Election(
                "Qual é a sua linguagem de programação favorita?",
                List.of("Java", "C++", "C", "Python", "JavaScript")
        );


        voteManager = new VoteManager(votacao);

        try (ServerSocket serverSocket = new ServerSocket(port);
             Scanner sc = new Scanner(System.in)) {

            System.out.println("Servidor ouvindo na porta " + port + "...");
            System.out.println("Pressione ENTER para encerrar a votação.\n");

            // Thread que aceita conexões de clientes
            Thread acceptThread = new Thread(() -> {
                while (true) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        System.out.println("Novo cliente conectado: " + clientSocket.getInetAddress());
                        new Thread(new ClientHandler(clientSocket, votacao, voteManager)).start();
                    } catch (IOException e) {
                        System.out.println("🛑 Servidor encerrado.");
                        break;
                    }
                }
            });

            acceptThread.start();


            sc.nextLine();

            System.out.println("\n🛑 Encerrando a eleição...");
            voteManager.generateReport();
            System.out.println("✅ Relatório final gerado com sucesso.");
            System.out.println("Encerrando servidor...");

            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
