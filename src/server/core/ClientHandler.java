package server.core;

import common.model.Election;
import common.model.Vote;
import common.utils.CPF;
import common.network.Message;
import server.ui.ServerDashboard;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

import static common.network.Message.MessageType.*;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final Election election;
    private final VoteManager voteManager;
    private final ServerDashboard dashboard; // 🧩 novo campo

    public ClientHandler(Socket socket, Election election, VoteManager voteManager, ServerDashboard dashboard) {
        this.socket = socket;
        this.election = election;
        this.voteManager = voteManager;
        this.dashboard = dashboard;
    }

    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            // Envia dados da eleição ao cliente
            out.writeObject(new Message(ELECTION_DATA, election));

            // Recebe o voto do cliente
            Message received = (Message) in.readObject();

            if (received.getType() == VOTE_SUBMISSION) {
                Vote vote = (Vote) received.getData();

                try {
                    CPF cpfObj = CPF.create(vote.getCpf());

                    boolean success = voteManager.registerVote(cpfObj.toString(), vote.getOption());

                    if (success) {
                        out.writeObject(new Message(SERVER_RESPONSE, "Voto computado com sucesso!"));
                        System.out.println("✔ Voto recebido: " + cpfObj.getFormatted() + " → " + vote.getOption());

                        // 🧩 Atualiza o painel visual em tempo real
                        if (dashboard != null) {
                            SwingUtilities.invokeLater(() -> dashboard.refreshResults());
                        }

                    } else {
                        out.writeObject(new Message(ERROR, "CPF já votou anteriormente!"));
                        System.out.println("⚠ Tentativa duplicada: " + cpfObj.getFormatted());
                    }

                } catch (IllegalArgumentException e) {
                    out.writeObject(new Message(ERROR, "CPF inválido: " + e.getMessage()));
                    System.out.println("❌ CPF inválido recebido: " + e.getMessage());
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro no cliente: " + e.getMessage());
        }
    }
}
