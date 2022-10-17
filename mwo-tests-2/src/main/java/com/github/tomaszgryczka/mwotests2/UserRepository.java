package com.github.tomaszgryczka.mwotests2;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    public String getUserPasswordByLogin(final String login) {
        // ...
        return null;
    }

    public boolean checkIfUserExists(final String login) {
        // ...
        return false;
    }

    public boolean registerUser(final String login, final String password, final String email) {
        // ...
        return false;
    }
}
