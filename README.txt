Dear readers and programmers,


This is a Java program.

Author is me, Zoran Bosanac. If you find any error in this program, or if you have an idea how to improve it, please send me email to: 
zoran.bosanac9@gmail.com

Before you can run this program, you need to create database called stockstable in Postgres local (you also need to create sequence for the 
column id). I suggest German locale for the database. Because this program uses German format for numbers and currencies.

This program has 3 tables:
1. Table is TableTrading: fetches particular stocks data from the website Yahoo finance in CSV format, formats the data, and then sets 
	formatted data into the cells. It has 2 buttons for buying and selling stocks. When user presses each of them, data from the pressed 
	row will be sent into the Postgres database (local), also data will be sent into the 3rd table TableTransactions.

2. Table is TablePortfolio: connects to the Postgres database (local), gets the data, formats it, and for each row from the database it adds row
	into the table.

3. Table is TableTransactions: shows formatted data received from the 1st table TableTrading.

Important to note it that each of these 3 tables was built on a different way. 1st table uses Runnable interface for creating threads, each row
in that table is independent thread. 2nd table was created using SwingWorker, so the entire table is one SwingWorker (it first deletes all the rows
from the table then adds entire new table - thats why you will see flashing every 2 seconds). 3rd table is the simplest form, without using any 
threads or databases.


 * Important note: starting on 22nd of July, problems with the Yahoo server became very often. 
 * At the moment there are no other sources of stock data for free (Google finance doesn't have 
 * it and to get stock data directly from the stock markets, Reuters or Bloomberg - that would 
 * be costly endeavor). Because of that, this program sometimes is not fully operational. 
 * Please accept my apology, but at the moment I don't have any other option.


Thats all.

Best regards
Zoran Bosanac

