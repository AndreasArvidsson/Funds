package com.github.andreasarvidsson.founds;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws ProtocolException, MalformedURLException, IOException {
        final long t0 = System.currentTimeMillis();

        //If true then additional data will be fetched from Morningstar. Takes more time.
        final boolean inclMorningstar = true;

        final PortfolioSummary rikaTillsammans = PortfolioSummary.create("rikaTillsammans", Arrays.asList(
                new SelectedFound(62.5, "Länsförsäkringar Global Indexnära"),
                new SelectedFound(10, "Handelsbanken Gl Småbolag Ind Cri A1 SEK"),
                new SelectedFound(10, "SEB Sverige Indexfond"),
                new SelectedFound(5, "Spiltan Aktiefond Investmentbolag"),
                new SelectedFound(12.5, "Länsförsäkringar Tillväxtmrkd Idxnära A", "Länsförsäkringar Tillväxtmarknad Indexnära")
        ), inclMorningstar);
        final PortfolioSummary pension = PortfolioSummary.create("Pension", Arrays.asList(
                new SelectedFound(65, "Avanza Global"),
                new SelectedFound(5, "Handelsbanken Gl Småbolag Ind Cri A1 SEK"),
                new SelectedFound(10, "Spiltan Aktiefond Investmentbolag"),
                new SelectedFound(5, "SEB Sverige Indexfond"),
                new SelectedFound(5, "PLUS Småbolag Sverige Index"),
                new SelectedFound(5, "Avanza Emerging Markets"),
                new SelectedFound(5, "Swedbank Robur Access Asien")
        ), inclMorningstar);
        final PortfolioSummary founds = PortfolioSummary.create("Värdepapper", Arrays.asList(
                new SelectedFound(48, "Avanza Global"),
                new SelectedFound(13, "Spiltan Aktiefond Investmentbolag"),
                new SelectedFound(5, "Avanza Emerging Markets"),
                new SelectedFound(4, "Swedbank Robur Access Asien"),
                new SelectedFound(5, "Swedbank Robur Technology A SEK"),
                new SelectedFound(5, "Swedbank Robur Ny Teknik A"),
                new SelectedFound(5, "Core Ny Teknik A"),
                new SelectedFound(5, "SEB Teknologifond"),
                new SelectedFound(5, "Skandia Time Global"),
                new SelectedFound(5, "Lannebo Teknik")
        ), false);

        rikaTillsammans.print();
        pension.print();
        founds.print();
        rikaTillsammans.compare(pension);
        pension.compare(founds);

        final Rankings rankings = Rankings.create(Arrays.asList(
                "DNB Teknologi A",
                "SEB Teknologifond",
                "Swedbank Robur Ny Teknik A",
                "Swedbank Robur Technology A SEK",
                "Lannebo Teknik",
                "Skandia Time Global"
        ));
        rankings.print();

        System.out.printf("Elapsed time: %.1fs\n", (System.currentTimeMillis() - t0) * 0.001);
    }

}
