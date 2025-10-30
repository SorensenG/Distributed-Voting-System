package common.model;

import java.io.Serializable;

public class Vote implements Serializable {
    private String cpf;
    private String option;



    public Vote(String cpf, String option) {
        this.cpf = cpf;
        this.option = option;
    }


    public String getCpf() {
        return cpf;
    }

    public String getOption() {
        return option;
    }

    @Override
    public String toString() {
        return "Voto{" +
                "cpf='" + cpf + '\'' +
                ", opção='" + option + '\'' +
                '}';
    }
}


