CREATE TABLE wallets
(
    id                 UUID PRIMARY KEY,
    balance            DECIMAL(19, 2) NOT NULL
);

CREATE TABLE transactions
(
    id          UUID PRIMARY KEY,
    wallet_id   UUID           NOT NULL,
    amount      DECIMAL(19, 2) NOT NULL,
    type        VARCHAR(50)    NOT NULL,
    occurred_on TIMESTAMP      NOT NULL,
    FOREIGN KEY (wallet_id) REFERENCES Wallets (id)
);