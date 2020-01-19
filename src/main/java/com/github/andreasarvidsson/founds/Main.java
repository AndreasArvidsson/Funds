package com.github.andreasarvidsson.founds;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        //If true then additional data will NOT be fetched from Morningstar. Saves time.
        //Morningstar.DISABLE = true;

        final Result result = new Result();
        final Portfolio rikaTillsammans = new Portfolio(
                "rikaTillsammans",
                new SelectedFound(62.5, "Länsförsäkringar Global Indexnära"),
                new SelectedFound(10, "Handelsbanken Gl Småbolag Ind Cri A1 SEK"),
                new SelectedFound(10, "SEB Sverige Indexfond"),
                new SelectedFound(5, "Spiltan Aktiefond Investmentbolag"),
                new SelectedFound(12.5, "Länsförsäkringar Tillväxtmrkd Idxnära A", "Länsförsäkringar Tillväxtmarknad Indexnära")
        );
        final Portfolio pension = new Portfolio(
                "Pension",
                new SelectedFound(64, "Avanza Global"),
                new SelectedFound(8, "Handelsbanken Gl Småbolag Ind Cri A1 SEK"),
                new SelectedFound(10, "Spiltan Aktiefond Investmentbolag"),
                new SelectedFound(8, "SEB Sverige Indexfond"),
                new SelectedFound(5, "Avanza Emerging Markets"),
                new SelectedFound(5, "Swedbank Robur Access Asien")
        );
        final Portfolio founds = new Portfolio(
                "Värdepapper",
                new SelectedFound(50, "Avanza Global"),
                new SelectedFound(11, "Spiltan Aktiefond Investmentbolag"),
                new SelectedFound(5, "Avanza Emerging Markets"),
                new SelectedFound(4, "Swedbank Robur Access Asien"),
                new SelectedFound(5, "Swedbank Robur Technology A SEK"),
                new SelectedFound(5, "Swedbank Robur Ny Teknik A"),
                new SelectedFound(5, "Core Ny Teknik A"),
                new SelectedFound(5, "SEB Teknologifond"),
                new SelectedFound(5, "Skandia Time Global"),
                new SelectedFound(5, "Lannebo Teknik")
        );
        final Rankings rankings = new Rankings(
                "Teknik",
                "DNB Teknologi A",
                "SEB Teknologifond",
                "Swedbank Robur Ny Teknik A",
                "Swedbank Robur Technology A SEK",
                "Lannebo Teknik",
                "Skandia Time Global"
        );

        result.add(rikaTillsammans);
        result.add(pension);
        result.add(founds);
        result.compare(rikaTillsammans, pension);
        result.compare(pension, founds);
        result.add(rankings);

        result.print();
        result.save();
    }

}
