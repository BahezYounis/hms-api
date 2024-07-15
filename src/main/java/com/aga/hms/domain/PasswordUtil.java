package com.aga.hms.domain;

public interface PasswordUtil {
    String hash(String password);

    boolean validate(String password, String hashedPassword);
}
