package com.github.andreasarvidsson.funds;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Andreas Arvidsson
 * https://www.msci.com/market-classification
 * https://www.msci.com/documents/10199/a71b65b5-d0ea-4b5c-a709-24b1213bc3c5
 * DM countries include: Australia,  Austria,  Belgium,  Canada,  Denmark,  Finland,  France,  Germany,  Hong  Kong,  Ireland,  Israel,  Italy,  Japan,  Netherlands,  New  Zealand,  Norway,Portugal, Singapore, Spain, Sweden, Switzerland, the UK and the US. 
 * EM countries include: Argentina, Brazil, Chile, China, Colombia, Czech Republic, Egypt, Greece, Hungary, India,Indonesia, Korea, Kuwait, Malaysia, Mexico, Pakistan, Peru, Philippines, Poland, Qatar, Russia, Saudi Arabia, South Africa, Taiwan, Thailand, Turkey and United Arab Emirates.
 * https://www.msci.com/documents/10199/bf7208b8-81ca-4ae7-be8b-487126577a5c
 * EM countries include: Argentina,  Brazil,  Chile,  China,  Colombia,  Czech  Republic,  Egypt,  Greece,  Hungary,  India,  Indonesia,  Korea,  Kuwait,  Malaysia,  Mexico,  Pakistan,  Peru,Philippines, Poland, Qatar, Russia, Saudi Arabia, South Africa, Taiwan, Thailand, Turkey and United Arab Emirates. 
 * FM countries include: Bahrain, Bangladesh, Burkina Faso, Benin,Croatia, Estonia, Guinea-Bissau, Ivory Coast, Jordan, Kenya, Kuwait, Lebanon, Lithuania, Kazakhstan, Mauritius, Mali, Morocco, Niger, Nigeria, Oman, Romania, Serbia, Senegal,Slovenia, Sri Lanka, Togo, Tunisia and Vietnam   
 * https://www.msci.com/eqb/methodology/meth_docs/MSCI_September2017_GIMIMethodology.pdf
 * 40 E.g. Luxembourg, Iceland or Cyprus. These countries are part of the developed markets universe. Given their modest size these markets are not included in the MSCI World Index. 
 */
public enum Country {
    //Nordamerika, DEVELOPED
    CANADA("Kanada", Region.NORTH_AMERICA, Market.DEVELOPED),
    USA("USA", Region.NORTH_AMERICA, Market.DEVELOPED),
    PUERTO_RICO("Puerto Rico", Region.NORTH_AMERICA, Market.DEVELOPED), //Guestimate: Sovereignty USA. Part of Handelsbanken Gl Småbolag(Solactive ISS ESG Screened Developed Markets Small Cap).

    //Latinamerika
    ARGENTINA("Argentina", Region.LATIN_AMERICA, Market.EMERING),
    BRAZIL("Brasilien", Region.LATIN_AMERICA, Market.EMERING),
    CHILE("Chile", Region.LATIN_AMERICA, Market.EMERING),
    COLOMBIA("Colombia", Region.LATIN_AMERICA, Market.EMERING),
    MEXICO("Mexiko", Region.LATIN_AMERICA, Market.EMERING),
    PERU("Peru", Region.LATIN_AMERICA, Market.EMERING),
    COSTA_RICA("Costa Rica", Region.LATIN_AMERICA, Market.EMERING),

    //Storbritannien
    GREAT_BRITAIN("Storbritannien", Region.GREAT_BRITAIN, Market.DEVELOPED),
    BERMUDA("Bermuda", Region.GREAT_BRITAIN, Market.DEVELOPED), //Guestimate: Sovereignty UK. Part of Handelsbanken Gl Småbolag(Solactive ISS ESG Screened Developed Markets Small Cap).
    BRITISH_VIRGIN_ISLANDS("Brittiska Virgin Islands", Region.GREAT_BRITAIN, Market.EMERING), //Guestimate: Sovereignty UK. Part of Länsförsäkringar Tillväxtmrkd(MSCI EM)
    CAYMAN_ISLANDS("Caymanöarna", Region.GREAT_BRITAIN, Market.EMERING), //Guestimate: Sovereignty UK. Part of Avanza emerging markets(MSCI EM)

    //Europa, DEVELOPED
    AUSTRIA("Österrike", Region.EUROPA, Market.DEVELOPED),
    BELGIUM("Belgien", Region.EUROPA, Market.DEVELOPED),
    DENMARK("Danmark", Region.EUROPA, Market.DEVELOPED),
    FINLAND("Finland", Region.EUROPA, Market.DEVELOPED),
    FRANCE("Frankrike", Region.EUROPA, Market.DEVELOPED),
    GERMANY("Tyskland", Region.EUROPA, Market.DEVELOPED),
    IRELAND("Irland", Region.EUROPA, Market.DEVELOPED),
    ITALY("Italien", Region.EUROPA, Market.DEVELOPED),
    NETHERLANDS("Nederländerna", Region.EUROPA, Market.DEVELOPED),
    NORWAY("Norge", Region.EUROPA, Market.DEVELOPED),
    PORTUGAL("Portugal", Region.EUROPA, Market.DEVELOPED),
    SPAIN("Spanien", Region.EUROPA, Market.DEVELOPED),
    SWEDEN("Sverige", Region.EUROPA, Market.DEVELOPED),
    SWITZERLAND("Schweiz", Region.EUROPA, Market.DEVELOPED),
    LUXEMBOURG("Luxemburg", Region.EUROPA, Market.DEVELOPED), //See 40 above
    ICELAND("Island", Region.EUROPA, Market.DEVELOPED), //See 40 above
    CYPRUS("Cypern", Region.EUROPA, Market.DEVELOPED), //See 40 above

    //Europa, EMERGING
    CZECH_REPUBLIC("Tjeckien", Region.EUROPA, Market.EMERING),
    GREECE("Grekland", Region.EUROPA, Market.EMERING),
    HUNGARY("Ungern", Region.EUROPA, Market.EMERING),
    POLAND("Polen", Region.EUROPA, Market.EMERING),
    RUSSIA("Ryssland", Region.EUROPA, Market.EMERING),
    ROMANIA("Rumänien", Region.EUROPA, Market.EMERING), //Guestimate: Part of Länsförsäkringar Tillväxtmrkd(MSCI EM)
    
    //Europa, UNKNOWN
    MALTA("Malta", Region.EUROPA, Market.UNKNOWN),
    LIECHTENSTEIN("Liechtenstein", Region.EUROPA, Market.UNKNOWN),
    
    //Asien, DEVELOPED
    HONG_KONG("Hongkong", Region.ASIA, Market.DEVELOPED),
    SINGAPORE("Singapore", Region.ASIA, Market.DEVELOPED),
    MACAO("Macao", Region.ASIA, Market.DEVELOPED), //Guestimate: Part of Avanza global(MSCI World)
    
    //Asien, EMERGING
    CHINA("Kina", Region.ASIA, Market.EMERING),
    INDIA("Indien", Region.ASIA, Market.EMERING),
    INDONESIA("Indonesien", Region.ASIA, Market.EMERING),
    KOREA("Sydkorea", Region.ASIA, Market.EMERING),
    MALAYSIA("Malaysia", Region.ASIA, Market.EMERING),
    PAKISTAN("Pakistan", Region.ASIA, Market.EMERING),
    PHILIPPINES("Filippinerna", Region.ASIA, Market.EMERING),
    TAIWAN("Taiwan", Region.ASIA, Market.EMERING),
    THAILAND("Thailand", Region.ASIA, Market.EMERING),
    KAZAKHSTAN("Kazakstan", Region.ASIA, Market.FRONTIER),
   
    //Japan
    JAPAN("Japan", Region.JAPAN, Market.DEVELOPED),
    
    //Australasia
    AUSTALIA("Australien", Region.AUSTRALASIA, Market.DEVELOPED),
    NEW_ZEALAND("Nya Zeeland", Region.AUSTRALASIA, Market.DEVELOPED),
    PAPUA_NEW_GUINEA("Papua Nya Guinea", Region.AUSTRALASIA, Market.DEVELOPED), //Guestimate: Part of Avanza global(MSCI World)
    
    //Afrika och Mellanöstern, DEVELOPED
    ISRAEL("Israel", Region.AFRICA_MIDDLE_EAST, Market.DEVELOPED),
    
    //Afrika och Mellanöstern, EMERGING
    EGYPT("Egypten", Region.AFRICA_MIDDLE_EAST, Market.EMERING),
    KUWAIT("Kuwait", Region.AFRICA_MIDDLE_EAST, Market.EMERING),
    QATAR("Qatar", Region.AFRICA_MIDDLE_EAST, Market.EMERING),
    SAUDI_ARABIA("Saudiarabien", Region.AFRICA_MIDDLE_EAST, Market.EMERING),
    SOUTH_AFRICA("Sydafrika", Region.AFRICA_MIDDLE_EAST, Market.EMERING),
    TURKEY("Turkiet", Region.AFRICA_MIDDLE_EAST, Market.EMERING),
    UNITED_ARAB_EMIRATES("Förenade Arabemiraten", Region.AFRICA_MIDDLE_EAST, Market.EMERING),
    
    //Afrika och Mellanöstern, FRONTIER
    BURKINA_FASO("Burkina Faso", Region.AFRICA_MIDDLE_EAST, Market.FRONTIER);
    
    public final String name;
    public final Region region;
    public final Market market;
    private final static Map<String, Country> MAP = new HashMap<>();
    
    static {
        for (Country c : Country.values()) {
            MAP.put(c.name.toLowerCase(), c);
        }
    }

    public static Country fromString(final String countryName) {
        if (MAP.containsKey(countryName.toLowerCase())) {
            return MAP.get(countryName.toLowerCase());
        }
        throw new RuntimeException(String.format("Cant find country: %s", countryName));
    }

    private Country(final String name, final Region region, final Market market) {
        this.name = name;
        this.region = region;
        this.market = market;
    }

    public static enum Region {
        NORTH_AMERICA("Nordamerika"),
        LATIN_AMERICA("Latinamerika"),
        GREAT_BRITAIN("Storbritannien"),
        EUROPA("Europa"),
        ASIA("Asien"),
        JAPAN("Japan"),
        AFRICA_MIDDLE_EAST("Afrika och Mellanöstern"),
        AUSTRALASIA("Australasia");

        public final String name;

        private Region(final String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

    }

    public static enum Market {
        DEVELOPED,
        EMERING,
        FRONTIER,
        UNKNOWN
    }

}
