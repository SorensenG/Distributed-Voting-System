package server.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.model.Election;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class VoteManager {

    private final Map<String, String> votes = new ConcurrentHashMap<>();
    private final Election election;

    public VoteManager(Election election) {
        this.election = election;
    }

    public boolean registerVote(String cpf, String option) {
        boolean added = votes.putIfAbsent(cpf, option) == null;
        if (added) {
            System.out.println("\nNovo voto recebido de CPF " + cpf + " (" + option + ")");
            printPartialResults();
        }
        return added;
    }

    public Map<String, Integer> countVotes() {
        Map<String, Integer> count = new LinkedHashMap<>();
        for (String option : election.getOptions()) {
            count.put(option, 0);
        }
        for (String option : votes.values()) {
            count.put(option, count.get(option) + 1);
        }
        return count;
    }

    private void printPartialResults() {
        System.out.println("\nRESULTADOS PARCIAIS");
        System.out.println("--------------------------------------");

        Map<String, Integer> results = countVotes();
        for (Map.Entry<String, Integer> entry : results.entrySet()) {
            System.out.printf("%-15s : %d voto(s)%n", entry.getKey(), entry.getValue());
        }

        System.out.println("--------------------------------------");
        System.out.println("Total de eleitores até agora: " + votes.size());
        System.out.println("======================================");
    }

    public void generateReport() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = "results_" + timestamp + ".txt";

        Map<String, Integer> results = countVotes();
        int maxVotes = results.values().stream().max(Integer::compareTo).orElse(0);

        List<String> winners = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : results.entrySet()) {
            if (entry.getValue() == maxVotes) {
                winners.add(entry.getKey());
            }
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("RELATÓRIO FINAL DA ELEIÇÃO");
            writer.println("======================================");
            writer.println("Pergunta: " + election.getQuestion());
            writer.println("--------------------------------------");

            for (Map.Entry<String, Integer> entry : results.entrySet()) {
                String line = entry.getKey() + ": " + entry.getValue() + " voto(s)";
                if (winners.contains(entry.getKey())) {
                    line += "  ← VENCEDOR";
                }
                writer.println(line);
            }

            writer.println("--------------------------------------");
            writer.println("Total de eleitores: " + votes.size());
            writer.println("Eleitores e seus votos:");
            votes.forEach((cpf, voteChoice) -> writer.println(" - " + cpf + " → " + voteChoice));
            writer.println("======================================");

            if (winners.size() == 1) {
                writer.println("VENCEDOR: " + winners.get(0).toUpperCase() +
                        " com " + maxVotes + " voto(s)!");
            } else {
                writer.println("EMPATE entre: " + String.join(", ", winners) +
                        " com " + maxVotes + " voto(s) cada.");
            }

            writer.println("======================================");
            System.out.println("\nRelatório final gerado com sucesso: " + fileName);

            generateCsvReport(results, timestamp, winners);
            generateJsonReport(results, timestamp, winners);

        } catch (IOException e) {
            System.err.println("Erro ao gerar relatório: " + e.getMessage());
        }
    }

    private void generateCsvReport(Map<String, Integer> results, String timestamp, List<String> winners) {
        String csvName = "results_" + timestamp + ".csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvName))) {
            writer.println("RELATÓRIO FINAL DA ELEIÇÃO");
            writer.println("======================================");
            writer.println("Pergunta: " + election.getQuestion());
            writer.println("--------------------------------------");
            writer.println("Opção,Votos,Vencedor");

            for (Map.Entry<String, Integer> entry : results.entrySet()) {
                String vencedor = winners.contains(entry.getKey()) ? "← VENCEDOR" : "";
                writer.println(entry.getKey() + "," + entry.getValue() + "," + vencedor);
            }

            writer.println("--------------------------------------");
            writer.println("Total de eleitores," + votes.size());
            writer.println("Eleitores e seus votos:");
            votes.forEach((cpf, voteChoice) -> writer.println(cpf + "," + voteChoice));
            writer.println("======================================");

            if (winners.size() == 1) {
                writer.println("VENCEDOR," + winners.get(0).toUpperCase() + "," + maxVotes(results));
            } else {
                writer.println("EMPATE," + String.join(" / ", winners) + "," + maxVotes(results));
            }

            writer.println("======================================");
            System.out.println("Arquivo CSV gerado: " + csvName);
        } catch (IOException e) {
            System.err.println("Erro ao gerar CSV: " + e.getMessage());
        }
    }

    private void generateJsonReport(Map<String, Integer> results, String timestamp, List<String> winners) {
        String jsonName = "results_" + timestamp + ".json";

        Map<String, Object> jsonData = new LinkedHashMap<>();
        jsonData.put("titulo", "RELATÓRIO FINAL DA ELEIÇÃO");
        jsonData.put("pergunta", election.getQuestion());
        jsonData.put("resultados", results);
        jsonData.put("totalEleitores", votes.size());
        jsonData.put("eleitores", new LinkedHashMap<>(votes));
        jsonData.put("vencedores", winners);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(jsonName)) {
            gson.toJson(jsonData, writer);
            System.out.println("Arquivo JSON gerado: " + jsonName);
        } catch (IOException e) {
            System.err.println("Erro ao gerar JSON: " + e.getMessage());
        }
    }

    private int maxVotes(Map<String, Integer> results) {
        return results.values().stream().max(Integer::compareTo).orElse(0);
    }

    public int getTotalVoters() {
        return votes.size();
    }
}
