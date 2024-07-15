package api;

import java.util.ArrayList;
import java.util.List;

import converter.DateTimeConverter;

import db.Product;
import db.ProdPlan;
import db.DbPartUtils;
import db.DbProdPlanUtils;

public class TaktiqAbas {
    public TaktiqVehicles buildVehicle(Product vehicle, Integer productionLineNumber) {
        TaktiqVehicles taktiqVehicle = new TaktiqVehicles();
        List<String> futureList = new ArrayList<>();
        
        if (DbPartUtils.isFahrzeug(vehicle)) {
            Object dbContext = new Object();

            ProdPlan prodplan = DbProdPlanUtils.getProdPlanFuerSerie(dbContext, DbPartUtils.getSerie(vehicle));

            if (prodplan != null) {
                if (prodplan.getBand() != productionLineNumber) {
                    return taktiqVehicle;
                }
                
                boolean isFzgBereich = false;

                ProdPlan.Table table = prodplan.table();
                for (ProdPlan.Row prodPlanRow : table.getRows()) {
                    if (!prodPlanRow.getPfz().isEmpty()) {
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
                                                       
                            Product prodVehicle = prodplan.getArtnr();
                            if (prodVehicle != null) {
                                taktiqVehicle.setModel(prodVehicle.getKurzwort());
                                taktiqVehicle.setDescription(prodVehicle.getKurzwort());
                            }
                            
                            if (prodPlanRow.getYtlterm() != null) {
                                String dateString = prodPlanRow.getYtlterm().toString();
                                if (dateString != null) {
                                    taktiqVehicle.addDate(DateTimeConverter.abasTimestampToTaktiq(dateString), "Station1", "PLAN");
                                }
                            }

                            Product dekor = prodPlanRow.getYdekor();
                            if (dekor != null) {
                                futureList.add(dekor.getYvnum());
                            }
                        }
                        Product ausstattung = prodPlanRow.getPsaart();
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
