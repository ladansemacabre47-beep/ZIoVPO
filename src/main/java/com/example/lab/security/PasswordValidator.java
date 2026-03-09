package com.example.lab.security;

public class PasswordValidator {

    public static void validate(String password) {
        if (password == null) throw new IllegalArgumentException("Пароль пустой");
        if (password.length() < 8) throw new IllegalArgumentException("Длина >= 8");
        if (!password.matches(".*[A-Z].*")) throw new IllegalArgumentException("Нужна заглавная буква");
        if (!password.matches(".*[a-z].*")) throw new IllegalArgumentException("Нужна строчная буква");
        if (!password.matches(".*\\d.*")) throw new IllegalArgumentException("Нужна цифра");
        if (!password.matches(".*[!@#$%^&*()_+\\-={}:;\"'?/><.,].*")) throw new IllegalArgumentException("Нужен спецсимвол");
    }
}
