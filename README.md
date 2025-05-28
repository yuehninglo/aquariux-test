# This is a test from aquariux.

## Preparation for running the project
1. Create the tables and insert test data by executing table.sql
2. Change the value of "spring.datasource.url" to your own H2 database file location in application.properties, also change the username and password if needed.

## Task
1. Create a 10 seconds interval scheduler to retrieve the pricing from the source above and store the best pricing into the database.
   
   -> PriceScheduler.java

2. Create an api to retrieve the latest best aggregated price.
   
   -> example: http://localhost:8080/price/best/BTCUSDT

3. Create an api which allows users to trade based on the latest best aggregated price.
   
   -> example:

   curl -X POST http://localhost:8080/trade/execute/Eden -H "Content-Type: application/json" -d '{
   "tradingSymbol": "BTCUSDT",
   "cryptoSymbol": "BTC",
   "side": "BUY",
   "amount": 0.001}'

4. Create an api to retrieve the user’s crypto currencies wallet balance
   
   -> example: http://localhost:8080/wallet/Eden

5. Create an api to retrieve the user trading history.
   
   -> example: http://localhost:8080/trade/Eden