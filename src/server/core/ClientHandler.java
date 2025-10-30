package server.core;

import common.model.Election;
import common.model.Vote;
import common.utils.CPF;
import common.network.Message;
import static common.network.Message.MessageType.*;


import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final Election election;
    private final VoteManager voteManager;

    public ClientHandler(Socket socket, Election election, VoteManager voteManager) {
        this.socket = socket;
        this.election = election;
        this.voteManager = voteManager;
    }

    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {


            //envia os dados da eleição ao cliente
            out.writeObject(new Message(ELECTION_DATA, election));

            //recebe o voto do cliente
            Message received = (Message) in.readObject();

            if (received.getType() == VOTE_SUBMISSION) {
                Vote vote = (Vote) received.getData();


                try {
                    CPF cpfObj = CPF.create(vote.getCpf());

                    boolean success = voteManager.registerVote(cpfObj.toString(), vote.getOption());

                    if (success) {
                        out.writeObject(new Message(SERVER_RESPONSE, "✅ Voto computado com sucesso!"));

                    } else {
                        out.writeObject(new Message(ERROR, "❌ CPF já votou anteriormente!"));

                    }

                } catch (IllegalArgumentException e) {
                    out.writeObject(new Message(ERROR, "❌ CPF inválido: " + e.getMessage()));
                    System.out.println("Voto voto negado, pois: " +  e.getMessage());
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        }
}