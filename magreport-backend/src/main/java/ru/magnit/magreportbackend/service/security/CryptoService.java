package ru.magnit.magreportbackend.service.security;

public interface CryptoService {

    String encode(String message);
    String decode(String message);
}
