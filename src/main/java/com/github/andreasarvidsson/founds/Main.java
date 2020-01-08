package com.github.andreasarvidsson.founds;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    private static List<Pair<Found, Double>> getFounds(
            final List<Pair<String, Double>> foundNames) throws MalformedURLException, IOException {
        final List<Pair<Found, Double>> res = new ArrayList();
        for (final Pair<String, Double> foundNamePair : foundNames) {
            res.add(new Pair(
                    Avanza.getFound(foundNamePair.first()),
                    foundNamePair.second()
            ));
        }
        return res;
    }

    private static PortfolioSummary summarizePortfoolio(
            final String name,
            final List<Pair<String, Double>> foundNames) throws IOException {
        return new PortfolioSummary(name, getFounds(foundNames));
    }

    public static void main(String[] args) throws ProtocolException, MalformedURLException, IOException {
        final PortfolioSummary founds = summarizePortfoolio("Värdepapper", Arrays.asList(
                new Pair("Avanza Global", 0.45),
                new Pair("Spiltan Aktiefond Investmentbolag", 0.15),
                new Pair("PLUS Småbolag Sverige Index", 0.05),
                new Pair("Avanza Emerging Markets", 0.05),
                new Pair("Core Ny Teknik A", 0.06),
                new Pair("DNB Teknologi A", 0.06),
                new Pair("SEB Teknologifond", 0.06),
                new Pair("Swedbank Robur Ny Teknik A", 0.06),
                new Pair("Swedbank Robur Technology A SEK", 0.06)
        ));
        final PortfolioSummary pension = summarizePortfoolio("Pension", Arrays.asList(
                new Pair("Avanza Global", 0.60),
                new Pair("Handelsbanken Gl Småbolag Ind Cri A1 SEK", 0.05),
                new Pair("Spiltan Aktiefond Investmentbolag", 0.15),
                new Pair("SEB Sverige Indexfond", 0.05),
                new Pair("PLUS Småbolag Sverige Index", 0.05),
                new Pair("Avanza Emerging Markets", 0.05),
                new Pair("Swedbank Robur Access Asien", 0.05)
        ));
        founds.print();
        pension.print();
        founds.compare(pension);
    }

}
