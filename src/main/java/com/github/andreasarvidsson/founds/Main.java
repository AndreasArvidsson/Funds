package com.github.andreasarvidsson.founds;

import java.io.IOException;

/*
    @author Andreas Arvidsson
    https://github.com/AndreasArvidsson/Founds
 */
public class Main {

    public static void main(String[] args) throws IOException {
        //If true then additional data will NOT be fetched from Morningstar. Saves time.
        //Morningstar.DISABLE = true;

        //If true then found fees will be fetched from SAVR. 
        //For SelectedFound passing this variable as second parameter.
        final boolean useSavr = true;

        final Result result = new Result();

        final Portfolio rikaTillsammans = new Portfolio(
                "rikaTillsammans",
                new SelectedFound(62.5, "Länsförsäkringar Global Indexnära"),
                new SelectedFound(10, "Handelsbanken Gl Småbolag Ind Cri A1 SEK", "Handelsbanken Global Småbolag Index Criteria A1 SEK"),
                new SelectedFound(10, "SEB Sverige Indexfond"),
                new SelectedFound(5, "Spiltan Aktiefond Investmentbolag"),
                new SelectedFound(12.5, "Länsförsäkringar Tillväxtmrkd Idxnära A", "Länsförsäkringar Tillväxtmarknad Indexnära A")
        );

        final Portfolio pension = new Portfolio(
                "Pension",
                new SelectedFound(64, "Avanza Global"),
                new SelectedFound(8, "Handelsbanken Gl Småbolag Ind Cri A1 SEK", "Handelsbanken Global Småbolag Index Criteria A1 SEK"),
                new SelectedFound(10, "Spiltan Aktiefond Investmentbolag"),
                new SelectedFound(8, "SEB Sverige Indexfond"),
                new SelectedFound(5, "Avanza Emerging Markets"),
                new SelectedFound(5, "Swedbank Robur Access Asien")
        );

        final Portfolio founds = new Portfolio(
                "Värdepapper",
                new SelectedFound(51, "Avanza Global"),
                new SelectedFound(10, "Spiltan Aktiefond Investmentbolag"),
                new SelectedFound(4, "Avanza Emerging Markets"),
                new SelectedFound(5, "Swedbank Robur Access Asien"),
                new SelectedFound(6, useSavr, "Swedbank Robur Technology A SEK"),
                new SelectedFound(6, useSavr, "Swedbank Robur Ny Teknik A"),
                new SelectedFound(6, useSavr, "TIN Ny Teknik A"),
                new SelectedFound(6, useSavr, "SEB Teknologifond"),
                new SelectedFound(6, useSavr, "Skandia Time Global")
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
        result.add(founds);
        //Compare portfolios.
        result.compare(rikaTillsammans, pension);
        result.compare(pension, founds);
        //Rank different founds.
        result.add(rankings);

        //Print result to screen.
        result.print();
        //Store result as excel file in "excel" folder.
        result.save();
    }

}
