package app;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.ImportAuftragsdatenApi;

public class CronetworkMesApp {
    public static void main(String[] args) {
        ApiClient client = new ApiClient();
        client.setBasePath("http://google.com");

        ImportAuftragsdatenApi auftragsdaten = new ImportAuftragsdatenApi(client);

        String body = null;
        String acceptLanguage = null;
        Integer pCrosscompany = null;
        Integer pDataview = null;
        String pCompany = null;
        Integer pQuerytimeout = null;
        String pNlsLanguage = null;
        String pNlsTerritory = null;
        String pNlsSort = null;
        Long callid = null;
        Integer netzbildung = null;
        Integer verknuepfen = null;

        ApiResponse<String> response;
        try {
            response = auftragsdaten.importAuftragsdatenWithHttpInfo(body, acceptLanguage, pCrosscompany, pDataview, pCompany, pQuerytimeout, pNlsLanguage, pNlsTerritory, pNlsSort, callid ,netzbildung, verknuepfen);
            System.out.println(response);
        } catch (ApiException e) {
            System.out.println(e.getMessage());
        }

    }

}
