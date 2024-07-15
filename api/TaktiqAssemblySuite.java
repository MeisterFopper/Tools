package api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import manager.TokenManager;
import util.JsonParserUtil;

public class TaktiqAssemblySuite {

    private String baseurl;
    private TokenManager tokenManager;

    private Integer productionLineNumber;

    public List<TaktiqVehicles> taktiqVehicles;
    public List<TaktiqVehicles> taktiqSeries;

    private static final long TOKEN_EXPIRATION_TIME = 10000;

    private static final String AUTHORIZATION_URL = "/api/Users/authenticate";
    private static final String AUTHORIZATION_METHOD = "POST";

    private static final String ORDERS_SEND_URL = "/api/Orders/createorupdate";
    private static final String ORDERS_SEND_METHOD = "PUT";

    private static final String ORDERS_READ_URL = "/api/Orders";
    private static final String ORDERS_READ_METHOD = "GET";


    // public

    // OK
    public TaktiqAssemblySuite(String username, String password, String baseurl) throws IOException, ParseException {
        if ((username == null) || (password == null)  || (baseurl != null)) {
            throw new IOException("Not all credentials are set.");
        }

        this.baseurl = baseurl;
        authenticate(username, password);
        
        resetOrders();
    }

    public void setProductionLineNumber(Integer band) {
        this.productionLineNumber = band;
        resetOrders();
    }

    public void resetOrders() {
        this.taktiqVehicles = new ArrayList<>();
        this.taktiqSeries = new ArrayList<>();
    }

    // OK
    @SuppressWarnings("unchecked")
    public void authenticate(String username, String password) throws IOException, ParseException {

        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put("username", username);
        jsonPayload.put("password", password);

        HttpURLConnection conn = UrlHandler.createConnection(this.baseurl + AUTHORIZATION_URL, AUTHORIZATION_METHOD, null , jsonPayload.toString());
        JSONObject json = JsonParserUtil.parseStringToObject(HttpResponseHandler.getResponse(conn));

        this.tokenManager = new TokenManager(json.get("jwtToken").toString(), json.get("refreshToken").toString(), TOKEN_EXPIRATION_TIME);
    }

    // OK
    @SuppressWarnings("unchecked")
    public void putVehicles() throws IOException, ParseException {
        if (this.taktiqVehicles != null) {
            String planningArea = "PlanningArea" + this.productionLineNumber.toString();
            JSONObject orders = new JSONObject();
            
            orders.put("planningArea", planningArea);
            orders.put("orders", this.taktiqVehicles.toString());
            orders.put("isTransaction", true);

            checkToken();

            HttpURLConnection conn = UrlHandler.createConnection(ORDERS_SEND_URL, ORDERS_SEND_METHOD, this.tokenManager.getAccessToken(), taktiqVehicles.toString());
            JsonParserUtil.parseStringToObject(HttpResponseHandler.getResponse(conn));
        }
    }

    // OK
    public void getVehicles() throws IOException, ParseException {
        String planningArea = "?PlanningArea" + this.productionLineNumber.toString();

        checkToken();

        HttpURLConnection conn = UrlHandler.createConnection(ORDERS_READ_URL + planningArea, ORDERS_READ_METHOD, this.tokenManager.getAccessToken(), null);
        JSONArray json = JsonParserUtil.parseStringToArray(HttpResponseHandler.getResponse(conn));

        for (Object obj : json) {
            // Skip if obj is not a JSONObject
            if (obj instanceof JSONObject) {
                JSONObject order = (JSONObject) obj;
                Object orderNumberObj = order.get(TaktiqVehicles.ORDER_NUMBER_STRING);

                // Skip if orderNumberObj is not a String
                if (orderNumberObj instanceof String) {
                    TaktiqVehicles taktiqVehicle = new TaktiqVehicles();
                    taktiqVehicle.loadVehicleFromJson(order.toString());
                    this.taktiqVehicles.add(taktiqVehicle);
                }
            }
        }
    }

    // OK
    public void getSeries(String seriesNumber) {
        if (seriesNumber == null || seriesNumber.length() != 9) {
            return;
        }

        if (this.taktiqVehicles == null) {
            return;
        }

        for (TaktiqVehicles vehicle : this.taktiqVehicles) {
            if (vehicle.getSeriesNumber().startsWith(seriesNumber)) {
                this.taktiqSeries.add(vehicle);
            }
        }
    }

    // OK
    public Integer getVehicleSequencePos(TaktiqVehicles vehicle) {
        Integer sequencepos = null;

        if ((this.taktiqSeries == null) || (vehicle == null)) {
            return sequencepos;
        }

        Integer counter = 0;

        for (TaktiqVehicles seriesVehicle : this.taktiqSeries) {
            counter++;
            if (vehicle.equals(seriesVehicle)) {
                sequencepos = counter;
            }
        }
    
        return sequencepos;
    }
    
    // private

    private static String formatDate(String dateTime) {
        String formattedDate = null;
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, inputFormatter);
        ZoneId zoneId = ZoneId.of("Europe/Berlin");
        java.time.ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX");
        formattedDate = zonedDateTime.format(outputFormatter);
        
        return formattedDate;
    }

    private void checkToken() {	
        if (tokenManager.isRefreshRequired()) {
            //
        }
    }

    // OK, abas?
    private TaktiqVehicles buildVehicle(Product vehicle) {
        TaktiqVehicles taktiqVehicle = new TaktiqVehicles();
        List<String> futureList;
        
        if (Artikel.isFahrzeug(vehicle)) {
            ProdPlan prodplan = DbProdPlanUtils.getProdPlanFuerSerie(this.dbContext, DbPartUtils.getSerie(vehicle));

            if (prodplan != null) {
                if (prodplan.getBand() != this.productionLineNumber) {
                    return taktiqVehicle;
                }
                
                boolean isFzgBereich = false;

                ProdPlan.Table table = prodplan.table();
                for (ProdPlan.Row prodPlanRow : table.getRows()) {
                    // Neue laufende Nummer
                    if (!prodPlanRow.getPfz().isEmpty()) {
                        // Laufende Nummer mit gesuchter Nummer vergleichen
                        if (prodPlanRow.getPfz().equals(DbPartUtils.getLaufendeNummer(vehicle))) {
                            isFzgBereich = true;
                            futureList = new ArrayList<>();
                        } else {
                            isFzgBereich = false;
                        }
                    }

                    if (isFzgBereich) {
                        if (!prodPlanRow.getPfz().isEmpty()) {
                            taktiqVehicle.setOrderNumber(vehicle.getIdno());
                                                       
                            Product artikel = (Product) prodplan.getArtnr();
                            if (artikel != null) {
                                taktiqVehicle.setModel(artikel.getKurzwort());
                                taktiqVehicle.setDescription(artikel.getKurzwort());
                            }
                            
                            if (prodPlanRow.getYtlterm() != null) {
                                String dateString = prodPlanRow.getYtlterm().toString();
                                if (dateString != null) {
                                    taktiqVehicle.addDate(formatDate(dateString), "Station1", "PLAN"));
                                }
                            }

                            Product dekor = prodPlanRow.getYdekor();
                            if (dekor != null) {
                                futureList.add(dekor.getYvnum());
                            }
                        }
                        Product ausstattung = (Product) prodPlanRow.getPsaart();
                        if (ausstattung != null) {
                            futureList.add(ausstattung.getYvnum());
                        }
                    }

                }

            } 
        } 
        
        taktiqVehicle.setFeaturesList(futureList);

        return taktiqVehicle;
    } 
}