package common.model;

import java.io.Serializable;
import java.util.List;

public class Election implements Serializable {
    private String question;
    private List<String> options;

    public Election(String question, List<String> options) {
        this.question = question;
        this.options = options;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    @Override
    public String toString() {
        return "Eleição{" + "Pergunta='" + question + '\'' + ", Respostas=" + options + '}';
    }


}
