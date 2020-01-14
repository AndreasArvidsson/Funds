package com.github.andreasarvidsson.founds;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws ProtocolException, MalformedURLException, IOException {
        final PortfolioSummary pension = PortfolioSummary.create("Pension", Arrays.asList(
                new Pair("Avanza Global", 0.65),
                new Pair("Handelsbanken Gl Småbolag Ind Cri A1 SEK", 0.05),
                new Pair("Spiltan Aktiefond Investmentbolag", 0.10),
                new Pair("SEB Sverige Indexfond", 0.05),
                new Pair("PLUS Småbolag Sverige Index", 0.05),
                new Pair("Avanza Emerging Markets", 0.05),
                new Pair("Swedbank Robur Access Asien", 0.05)
        ));
        final PortfolioSummary founds = PortfolioSummary.create("Värdepapper", Arrays.asList(
                new Pair("Avanza Global", 0.48),
                new Pair("Spiltan Aktiefond Investmentbolag", 0.13),
                new Pair("Avanza Emerging Markets", 0.05),
                new Pair("Swedbank Robur Access Asien", 0.04),
                new Pair("Swedbank Robur Technology A SEK", 0.05),
                new Pair("Swedbank Robur Ny Teknik A", 0.05),
                new Pair("Core Ny Teknik A", 0.05),
                new Pair("SEB Teknologifond", 0.05),
                new Pair("Skandia Time Global", 0.05),
                new Pair("Lannebo Teknik", 0.05)
        ));
        pension.print();
        founds.print();
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
    }

}
