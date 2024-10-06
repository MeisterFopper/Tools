package api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MesSatz {
    private String name;    // Bezeichnung
    private Integer von;    // Startposition
    private Integer laenge; // Länge
    private char typ;       // Typ: N, C, D
    private String wert;    // Wert

    // Formatter für das Datum
    private static final DateTimeFormatter DATE_FORMAT_IN = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMAT_OUT = DateTimeFormatter.ofPattern("yyMMddHHmmss");

    public MesSatz(String name, Integer von, Integer laenge, char typ, String wert) {
        setName(name);
        setVon(von);
        setLaenge(laenge);
        setTyp(typ);
        setWert(wert);
    }

    // Getter und Setter
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVon() {
        return this.von;
    }

    public void setVon(Integer von) {
        this.von = von;
    }

    public Integer getLaenge() {
        return this.laenge;
    }

    public void setLaenge(Integer laenge) {
        this.laenge = laenge;
    }

    public char getTyp() {
        return this.typ;
    }

    public void setTyp(char typ) {
        this.typ = typ;
    }

    public String getWert() {
        return this.wert;
    }

    public void setWert(String wert) {
        this.wert = wert;
    }

    private String typeC() {
        // Wenn der Wert null ist, setze ihn auf einen leeren String
        if (getWert() == null) {
            setWert("");
        }
    
        // Initialisiere einen String mit der gewünschten Länge an Leerzeichen
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < getLaenge(); i++) {
            stringBuilder.append(' '); // Füge Leerzeichen hinzu
        }
    
        // Hänge den Wert an den String an
        stringBuilder.append(getWert());
    
        // Schneide den String von rechts ab, um die definierte Länge zu erreichen  
        return stringBuilder.toString().substring(stringBuilder.length() - getLaenge());
    }

    private String typeN() {
        if (getWert() == null) {
            setWert("0");
        }

        // Initialisiere einen String mit der gewünschten Länge an Leerzeichen
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < getLaenge(); i++) {
            stringBuilder.append('0'); // Füge 0 hinzu
        }

        // Hänge den Wert an den String an
        stringBuilder.append(getWert());

        // Schneide den String von rechts ab, um die definierte Länge zu erreichen  
        return stringBuilder.toString().substring(stringBuilder.length() - getLaenge());
    }

    private String typeD() {
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(wert, DATE_FORMAT_IN);
        } catch (DateTimeParseException e) {
            // Bei ungültigem Datum auf Standardwert setzen
            dateTime = LocalDateTime.parse("01.01.1970 00:00:00", DATE_FORMAT_IN);
        }

        return dateTime.format(DATE_FORMAT_OUT);
    }

    @Override
    public String toString() {
        String result = null;

        switch (typ) {
            case 'C': // Typ C: String mit führenden Leerzeichen aufgefüllt
                result = typeC();
                break;

            case 'N': // Typ N: Zahl mit führenden Nullen
                result = typeN();
                break;

            case 'D': // Typ D: Datum im Format YYMMDDHHMISS
                result = typeD();
                break;

            default: // Typ C: String mit führenden Leerzeichen aufgefüllt
                result = typeC();
                break;
        }

        return result;
    }

}
