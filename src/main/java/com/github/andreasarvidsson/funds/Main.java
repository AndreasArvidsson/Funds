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
                                new SelectedFund(100, "Länsförsäkringar Global Index"));

                final Portfolio rikaTillsammans = new Portfolio(
                                "rikaTillsammans 2024",
                                new SelectedFund(70, "DNB Global Indeks S"),
                                new SelectedFund(20, "PLUS Allabolag Sverige Index"),
                                new SelectedFund(10, "Avanza Emerging Markets"));

                // final Portfolio funds_old = new Portfolio(
                // "Värdepapper gammal",
                // new SelectedFund(65, "Länsförsäkringar Global Index"),
                // new SelectedFund(10, "Avanza Emerging Markets"),
                // new SelectedFund(10, "Handelsbanken Gl Småbolag Ind Cri A1 SEK"),
                // new SelectedFund(10, "Spiltan Aktiefond Investmentbolag"),
                // new SelectedFund(5, "PLUS Allabolag Sverige Index"));

                final Portfolio funds = new Portfolio(
                                "Värdepapper",
                                new SelectedFund(75, "DNB Global Indeks S"),
                                new SelectedFund(15, "PLUS Allabolag Sverige Index"),
                                new SelectedFund(10, "Avanza Emerging Markets"));

                final Portfolio izabel = new Portfolio(
                                "Izabel",
                                new SelectedFund(75, "Avanza Global"),
                                new SelectedFund(15, "PLUS Allabolag Sverige Index"),
                                new SelectedFund(10, "Avanza Emerging Markets"));

                final Rankings rankings = new Rankings(
                                "Teknik",
                                new SelectedFund("Swedbank Robur Technology A"),
                                new SelectedFund("Swedbank Robur Ny Teknik A"),
                                new SelectedFund("SEB Teknologifond"),
                                new SelectedFund("Skandia Time Global"),
                                new SelectedFund("DNB Teknologi S"),
                                new SelectedFund("Öhman Global Growth A")
                // new SelectedFund("TIN Ny Teknik A"),
                // new SelectedFund("Lannebo Teknik"),
                // new SelectedFund("Avanza World Tech by TIN")
                );

                // Display portfolio metrics.
                result.add(globalIndex);
                result.add(rikaTillsammans);
                result.add(funds);
                result.add(izabel);

                // Compare portfolios.
                result.compare(globalIndex, funds);
                result.compare(rikaTillsammans, funds);

                // Rank different funds.
                result.add(rankings);

                // Print result to screen.
                result.print();

                // Store result as excel file in "excel" folder.
                result.save();
        }

}
