package common.utils;

import java.util.regex.Pattern;

public class CPF {
    private final String value;

    private CPF(String cpf) {
        this.value = cpf;
    }


    public static CPF create(String cpf) {
        if (cpf == null) {
            throw new IllegalArgumentException("CPF não pode ser nulo.");
        }

        cpf = cpf.replaceAll("\\D", "");

        if (!Pattern.matches("^\\d{11}$", cpf)) {
            throw new IllegalArgumentException("CPF deve conter exatamente 11 dígitos.");
        }

        if (!isValid(cpf)) {
            throw new IllegalArgumentException("CPF inválido.");
        }

        return new CPF(cpf);
    }


    public static boolean isValid(String cpf) {

        if (cpf.matches("^(\\d)\\1{10}$")) return false;

        String base = cpf.substring(0, 9);
        String checkDigits = cpf.substring(9);

        int d1 = calculateCheckDigit(base, 10);
        int d2 = calculateCheckDigit(base + d1, 11);

        return checkDigits.equals("" + d1 + d2);
    }


    private static int calculateCheckDigit(String digits, int weightStart) {
        int sum = 0;
        for (int i = 0; i < digits.length(); i++) {
            sum += Character.getNumericValue(digits.charAt(i)) * (weightStart - i);
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }


    @Override
    public String toString() {
        return this.value;
    }


    public String getFormatted() {
        return String.format("%s.%s.%s-%s",
                value.substring(0, 3),
                value.substring(3, 6),
                value.substring(6, 9),
                value.substring(9, 11));
    }
}
