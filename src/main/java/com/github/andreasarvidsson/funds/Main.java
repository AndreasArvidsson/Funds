package com.github.andreasarvidsson.funds;

import java.io.IOException;

/*
    @author Andreas Arvidsson
    https://github.com/AndreasArvidsson/Funds
 */
public class Main {

    public static void main(String[] args) throws IOException {
        //If true then additional data will NOT be fetched from Morningstar. Saves time.
        //Morningstar.DISABLE = true;

        //If true then fund fees will be fetched from SAVR. 
        //For SelectedFund passing this variable as second parameter.
        final boolean useSavr = true;

        final Result result = new Result();

        final Portfolio rikaTillsammans = new Portfolio(
                "rikaTillsammans",
                new SelectedFund(62.5, "Länsförsäkringar Global Indexnära"),
                new SelectedFund(10, "Handelsbanken Gl Småbolag Ind Cri A1 SEK", "Handelsbanken Global Småbolag Index Criteria A1 SEK"),
                new SelectedFund(10, "SEB Sverige Indexfond"),
                new SelectedFund(5, "Spiltan Aktiefond Investmentbolag"),
                new SelectedFund(12.5, "Länsförsäkringar Tillväxtmrkd Idxnära A", "Länsförsäkringar Tillväxtmarknad Indexnära A")
        );

        final Portfolio pension = new Portfolio(
                "Pension",
                new SelectedFund(64, "Avanza Global"),
                new SelectedFund(8, "Handelsbanken Gl Småbolag Ind Cri A1 SEK", "Handelsbanken Global Småbolag Index Criteria A1 SEK"),
                new SelectedFund(10, "Spiltan Aktiefond Investmentbolag"),
                new SelectedFund(8, "SEB Sverige Indexfond"),
                new SelectedFund(5, "Avanza Emerging Markets"),
                new SelectedFund(5, "Swedbank Robur Access Asien")
        );

        final Portfolio funds = new Portfolio(
                "Värdepapper",
                new SelectedFund(51, "Avanza Global"),
                new SelectedFund(10, "Spiltan Aktiefond Investmentbolag"),
                new SelectedFund(4, "Avanza Emerging Markets"),
                new SelectedFund(5, "Swedbank Robur Access Asien"),
                new SelectedFund(6, useSavr, "Swedbank Robur Technology A SEK"),
                new SelectedFund(6, useSavr, "Swedbank Robur Ny Teknik A"),
                new SelectedFund(6, useSavr, "TIN Ny Teknik A"),
                new SelectedFund(6, useSavr, "SEB Teknologifond"),
                new SelectedFund(6, useSavr, "Skandia Time Global")
        );

        final Rankings rankings = new Rankings(
                "Teknik",
                useSavr,
                "DNB Teknologi A",
                "SEB Teknologifond",
                "Swedbank Robur Ny Teknik A",
                "Swedbank Robur Technology A SEK",
                "Skandia Time Global"
        //"Lannebo Teknik",
        //"TIN Ny Teknik A"
        );

        //Display portfolio metrics.
        result.add(rikaTillsammans);
        result.add(pension);
        result.add(funds);
        //Compare portfolios.
        result.compare(rikaTillsammans, pension);
        result.compare(pension, funds);
        //Rank different funds.
        result.add(rankings);

        //Print result to screen.
        result.print();
        //Store result as excel file in "excel" folder.
        result.save();
    }

}
