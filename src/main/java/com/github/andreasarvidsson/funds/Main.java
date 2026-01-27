package com.github.andreasarvidsson.funds;

import java.io.IOException;

/*
    @author Andreas Arvidsson
    https://github.com/AndreasArvidsson/Funds
 */
public class Main {

    public static void main(String[] args) throws IOException {
        final Result result = new Result();

        final Portfolio globalIndex = new Portfolio(
            "Global index",
            new SelectedFund(100, "Länsförsäkringar Global Index")
        );

        final Portfolio rikaTillsammans = new Portfolio(
            "rikaTillsammans 2024",
            new SelectedFund(70, "DNB Global Indeks S"),
            new SelectedFund(20, "PLUS Allabolag Sverige Index"),
            new SelectedFund(10, "Avanza Emerging Markets")
        );

        final Portfolio funds = new Portfolio(
            "Värdepapper",
            new SelectedFund(80, "DNB Global Indeks S"),
            new SelectedFund(10, "PLUS Allabolag Sverige Index"),
            new SelectedFund(10, "Avanza Emerging Markets")
        );

        final Portfolio izabel = new Portfolio(
            "Izabel",
            new SelectedFund(75, "Avanza Global"),
            new SelectedFund(15, "PLUS Allabolag Sverige Index"),
            new SelectedFund(10, "Avanza Emerging Markets")
        );

        // Display portfolio metrics.
        result.add(globalIndex);
        result.add(rikaTillsammans);
        result.add(funds);
        result.add(izabel);

        // Compare portfolios.
        result.compare(globalIndex, funds);
        result.compare(rikaTillsammans, funds);

        // Print result to screen.
        result.print();

        // Store result as excel file in "excel" folder.
        // result.save();
    }
}
