CREATE TABLE transactions (
                              id UUID PRIMARY KEY,
                              amount NUMERIC(19,2) NOT NULL,
                              type VARCHAR(30) NOT NULL,
                              description VARCHAR(255),
                              created_at TIMESTAMP NOT NULL,

                              source_account_id UUID,
                              target_account_id UUID,

                              CONSTRAINT fk_transaction_source
                                  FOREIGN KEY (source_account_id)
                                      REFERENCES accounts(id),

                              CONSTRAINT fk_transaction_target
                                  FOREIGN KEY (target_account_id)
                                      REFERENCES accounts(id)
);