CREATE TABLE users (
    id IDENTITY PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cryptocurrencies (
    id IDENTITY PRIMARY KEY,
    symbol VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE wallets (
    id IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL,
    crypto_id BIGINT NOT NULL,
    balance DECIMAL(36, 18) DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (crypto_id) REFERENCES cryptocurrencies(id),
    UNIQUE (user_id, crypto_id)
);

CREATE TABLE trading_pairs (
    id IDENTITY PRIMARY KEY,
    base_currency_id BIGINT NOT NULL,     -- BTC
    quote_currency_id BIGINT NOT NULL,    -- USDT
    symbol VARCHAR(20) NOT NULL UNIQUE,   -- BTC/USDT
    FOREIGN KEY (base_currency_id) REFERENCES cryptocurrencies(id),
    FOREIGN KEY (quote_currency_id) REFERENCES cryptocurrencies(id)
);

CREATE TABLE transactions (
    id IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL,
    trading_pair_id BIGINT NOT NULL,
    side VARCHAR(4) NOT NULL CHECK (side IN ('BUY', 'SELL')),
    amount DECIMAL(36, 18) NOT NULL,           -- Quantity of base currency
    price DECIMAL(36, 18) NOT NULL,            -- Price in quote currency
    total DECIMAL(36, 18) NOT NULL,            -- amount * price
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (trading_pair_id) REFERENCES trading_pairs(id)
);

CREATE TABLE price_sources (
    id IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    url VARCHAR(255)
);

CREATE TABLE price_snapshots (
    id IDENTITY PRIMARY KEY,
    trading_pair_id BIGINT NOT NULL,
    bid_price DECIMAL(36, 18) NOT NULL,
    ask_price DECIMAL(36, 18) NOT NULL,
    captured_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (trading_pair_id) REFERENCES trading_pairs(id),
    FOREIGN KEY (source_id) REFERENCES price_sources(id)
);

INSERT INTO users (username, email) VALUES
('Eden', 'eden@aquariux.com'),
('Tom', 'tom@aquariux.com');

INSERT INTO cryptocurrencies (symbol, name) VALUES
('BTC', 'Bitcoin'),
('ETH', 'Ethereum'),
('USDT', 'Tether');

-- Eden
INSERT INTO wallets (user_id, crypto_id, balance) VALUES
(1, 1, 0),        -- Eden has 0 BTC
(1, 2, 0),        -- Eden has 0 ETH
(1, 3, 50000.0);  -- Eden has 50,000 USDT

-- Tom
INSERT INTO wallets (user_id, crypto_id, balance) VALUES
(2, 1, 0),        -- Tom has 0 BTC
(2, 2, 0),        -- Tom has 0 ETH
(2, 3, 50000.0);  -- Tom has 50,000 USDT

INSERT INTO trading_pairs (base_currency_id, quote_currency_id, symbol) VALUES
(1, 3, 'BTCUSDT'),
(2, 3, 'ETHUSDT');

INSERT INTO price_sources (name, url) VALUES
('Binance', 'https://api.binance.com/api/v3/ticker/bookTicker'),
('Houbi', 'https://api.huobi.pro/market/tickers');

