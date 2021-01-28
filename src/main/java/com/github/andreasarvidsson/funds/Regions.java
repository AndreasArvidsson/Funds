package com.github.andreasarvidsson.funds;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class Regions {

    public static final String SWEDEN = "Sverige";
    public static final String USA = "USA";
    public static final String ASIA = "Asien exkl Japan";

    public static String get(final String country) {
        switch (country) {
            case "Sverige":
            case "Japan":
                return country;

            case "USA":
            case "Kanada":
            case "Kuba":
            case "Puerto Rico":
            case "Bermuda":
            case "Brittiska Virgin Islands":
            case "Caymanöarna":
                return "Nordamerika";

            case "Brasilien":
            case "Mexiko":
            case "Chile":
            case "Colombia":
            case "Peru":
            case "Argentina":
                return "Latinamerika";

            case "Australien":
            case "Nya Zeeland":
                return "Australien och Nya Zeeland";

            case "Finland":
            case "Norge":
            case "Danmark":
            case "Island":
            case "Storbritannien":
            case "Irland":
            case "Tyskland":
            case "Nederländerna":
            case "Frankrike":
            case "Spanien":
            case "Italien":
            case "Portugal":
            case "Österrike":
            case "Belgien":
            case "Schweiz":
            case "Luxemburg":
            case "Malta":
            case "Polen":
            case "Ungern":
            case "Tjeckien":
            case "Rumänien":
            case "Grekland":
            case "Liechtenstein":
                return "Västeuropa exkl Sverige";

            case "Ryssland":
                return "Östeuropa";

            case "Kina":
            case "Hongkong":
            case "Singapore":
            case "Papua Nya Guinea":
            case "Macao":
            case "Taiwan":
            case "Sydkorea":
            case "Indien":
            case "Thailand":
            case "Malaysia":
            case "Indonesien":
            case "Filippinerna":
            case "Pakistan":
                return ASIA;

            case "Sydafrika":
            case "Saudiarabien":
            case "Qatar":
            case "Förenade Arabemiraten":
            case "Egypten":
            case "Burkina Faso":
            case "Turkiet":
            case "Israel":
                return "Afrika och Mellanöstern";

            default:
                //TODO. Add as needed
                System.out.println(String.format("*** UNKNOWN REGION FOR COUNTRY %s", country));
                return "Unknown";
        }
    }

}
