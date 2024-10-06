package api;

import java.util.ArrayList;
import java.util.List;

import com.ehg.abas.projekt.mes.api.ImportAuftragsdatenApi;
import com.ehg.abas.projekt.mes.invoker.ApiClient;
import com.ehg.abas.projekt.mes.invoker.ApiException;

public class MesApi {

    public static List<MesSatz> genSatzAuftrag() {
        List<MesSatz> dataList = new ArrayList<>();

        dataList.add(new MesSatz("Richtung", 1, 1, 'N', "1"));
        dataList.add(new MesSatz("Satzart", 2, 3, 'N', "100"));
        dataList.add(new MesSatz("Logik", 5, 3, 'N', "350"));
        dataList.add(new MesSatz("Leer", 8, 3, 'C', ""));
        dataList.add(new MesSatz("Datum", 11, 12, 'D', "01.01.1980 00:00:00"));
        dataList.add(new MesSatz("AKNr", 23, 15, 'C', ""));
        dataList.add(new MesSatz("KundAuftrNr", 38, 12, 'C', ""));
        dataList.add(new MesSatz("RueckmeldNr", 50, 10, 'N', ""));

        return dataList;
    }

    public static void setzeWert(List<MesSatz> satz, String name ,String wert) {
        for (MesSatz feld : satz) {
            if (feld.getName().equals(name)) {
                feld.setWert(wert);
            }   
        }
    }

    public static String toString(List<MesSatz> satz) {
        StringBuilder satzBuilder = new StringBuilder();
        for (MesSatz feld : satz) {
            satzBuilder.append(feld.toString());
        }
        return satzBuilder.toString();
    }

    public static void sendeAuftrag(String body) {
            // API-Client initialisieren
        ApiClient client = new ApiClient();
        client.setBasePath("http://your.api.endpoint");
        System.out.println(client.getBasePath());

        // Optionale Parameter für den API-Aufruf
        String language = null;
        Integer crosscompany = null;
        Integer dataview = null;
        String company = null;
        Integer timeout = null;
        String pNlsLanguage = null;
        String pNlsTerritory = null;
        String pNlsSort = null;
        Long callid = null;
        Integer netzbildung = null;
        Integer verknuepfen = null;
        
        ImportAuftragsdatenApi auftragsdaten = new ImportAuftragsdatenApi(client);
        try {
            auftragsdaten.importAuftragsdatenWithHttpInfo(language, crosscompany, dataview, company, timeout, 
                pNlsLanguage, pNlsTerritory, pNlsSort, callid, netzbildung, verknuepfen, body);
        } catch (ApiException e) {
            System.out.println("API Exception: " + e.getMessage());
        }

    }

}
