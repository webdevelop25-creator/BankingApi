CREATE TABLE accounts (
                          id UUID PRIMARY KEY,
                          iban VARCHAR(34) NOT NULL UNIQUE,
                          balance NUMERIC(19, 2) NOT NULL,
                          account_type VARCHAR(30) NOT NULL,
                          active BOOLEAN NOT NULL,
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP,
                          user_id UUID NOT NULL,

                          CONSTRAINT fk_accounts_user
                              FOREIGN KEY (user_id)
                                  REFERENCES users(id)
);