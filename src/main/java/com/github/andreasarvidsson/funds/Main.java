package com.github.andreasarvidsson.funds;

import java.io.IOException;

/*
    @author Andreas Arvidsson
    https://github.com/AndreasArvidsson/Funds
 */
public class Main {

    //SAVR fees. Update as needed.
    private static final double ROBUR_TECH_FEE = 1.06;
    private static final double ROBUR_NYTEKNIK_FEE = 1.02;
    private static final double TIN_NYTEKNIK_FEE = 1.07;
    private static final double SEB_TEK_FEE = 0.95;
    private static final double SKANDIA_TIME_FEE = 1.0;
    private static final double DNB_TEK_FEE = 0.76;

    public static void main(String[] args) throws IOException {
        final Result result = new Result();

        final Portfolio rikaTillsammans2019 = new Portfolio(
                "rikaTillsammans 2019",
                new SelectedFund(62.5, "Länsförsäkringar Global Indexnära"),
                new SelectedFund(10, "Handelsbanken Gl Småbolag Ind Cri A1 SEK"),
                new SelectedFund(10, "SEB Hållbar Sverige Indexnära"),
                new SelectedFund(12.5, "Länsförsäkringar Tillväxtmrkd Idxnära A"),
                new SelectedFund(5, "Spiltan Aktiefond Investmentbolag")
        );

        final Portfolio rikaTillsammans2020 = new Portfolio(
                "rikaTillsammans 2020",
                new SelectedFund(70, "Länsförsäkringar Global Indexnära"),
                new SelectedFund(10, "Handelsbanken Gl Småbolag Ind Cri A1 SEK"),
                new SelectedFund(10, "Länsförsäkringar Tillväxtmrkd Idxnära A"),
                new SelectedFund(10, "PLUS Allabolag Sverige Index")
        );

        final Portfolio pension = new Portfolio(
                "Pension",
                new SelectedFund(64, "Länsförsäkringar Global Indexnära"),
                new SelectedFund(8, "Handelsbanken Gl Småbolag Ind Cri A1 SEK"),
                new SelectedFund(18, "Spiltan Aktiefond Investmentbolag"),
                new SelectedFund(5, "Avanza Emerging Markets"),
                new SelectedFund(5, "Swedbank Robur Access Asien A")
        );

        final Portfolio funds = new Portfolio(
                "Värdepapper",
                new SelectedFund(51, "Avanza Global"),
                new SelectedFund(10, "Spiltan Aktiefond Investmentbolag"),
                new SelectedFund(4, "Avanza Emerging Markets"),
                new SelectedFund(5, "Swedbank Robur Access Asien A"),
                new SelectedFund(6, "Swedbank Robur Technology A", ROBUR_TECH_FEE),
                new SelectedFund(6, "Swedbank Robur Ny Teknik A", ROBUR_NYTEKNIK_FEE),
                new SelectedFund(6, "TIN Ny Teknik A", TIN_NYTEKNIK_FEE),
                new SelectedFund(6, "SEB Teknologifond", SEB_TEK_FEE),
                new SelectedFund(6, "Skandia Time Global", SKANDIA_TIME_FEE)
        );

        final Rankings rankings = new Rankings(
                "Teknik",
                new SelectedFund("DNB Teknologi A", DNB_TEK_FEE),
                new SelectedFund("SEB Teknologifond", SEB_TEK_FEE),
                new SelectedFund("Swedbank Robur Ny Teknik A", ROBUR_NYTEKNIK_FEE),
                new SelectedFund("Swedbank Robur Technology A", ROBUR_TECH_FEE),
                new SelectedFund("TIN Ny Teknik A", TIN_NYTEKNIK_FEE),
                new SelectedFund("Skandia Time Global", SKANDIA_TIME_FEE)
        );

        //Display portfolio metrics.
        result.add(rikaTillsammans2019);
        result.add(rikaTillsammans2020);
        result.add(pension);
        result.add(funds);

        //Compare portfolios.
        result.compare(rikaTillsammans2019, rikaTillsammans2020);
        result.compare(pension, funds);

        //Rank different funds.
        result.add(rankings);

        //Print result to screen.
        result.print();

        //Store result as excel file in "excel" folder.
        result.save();
    }

}
