```

```
# Exchange Asset

Hi! This is a program used for a real-time value display demonstration of personal portfolio assets on an exchange.

# System Introduction

Traders can use this system to display the real-time value of their investment portfolio.
The portfolio includes the following three types of products:

1. Common stocks.
2. European call options on common stocks.
3. European put options on common stocks.

## Operating Environment and Dependencies

Operating Environment: Implemented using Java 1.8, requires the Java 1.8 environment to be configured.
Dependencies: The project depends on Spring, Guava, Protobuf, JUnit, and SQLite.

## Usage Introduction
```
Compile：./gradlew clean build
Run：java -jar ./build/libs/assets-0.0.1-SNAPSHOT.jar
```
## Information on Held Assets

Position information (including stock codes and the number of stocks/contracts in the portfolio) is saved in a CSV position file.
The file is located at asset/src/main/resources/positionfile.csv and can be provided according to the specified format.


## Configuration of Securities Assets Information

This solution uses SQLite to store securities definitions (supporting three types: stocks, call options, and put options).
The table structure is defined as follows:
```
| Field                         | Type          | Content |
| type                          | INTEGER       | Security type (stock, call option, put option) |
| symbol                        | CHAR(32)      | Security code  |
| cur_price                     | VARCHAR(128)  | Current price |
| strike_price                  | REAL          | Strike price  |
| expected_return               | REAL          | Expected annualized return |
| annualized_standard_deviation | REAL          | Annualized standard deviation|
| maturity                      | TEXT          | Fixed maturity date |
```
## Output Example

```
1 Market Data Update
AAPL change to 110.05
TELSA change to 450.03

symbol                        price       qty          value
------------------------------------------------------------

AAPL                         110.05   1000.00      110054.79
AAPL-OCT-2020-110-C           12.88 -20000.00     -257550.26
AAPL-OCT-2020-110-P            9.30  20000.00      185932.62
TELSA                        450.03   -500.00     -225012.61
TELSA-NOV-2020-400-C          52.56  10000.00      525611.56
TELSA-DEC-2020-400-P          38.11 -10000.00     -381110.11
------------------------------------------------------------

Total portfolio                                    -42074.01
```