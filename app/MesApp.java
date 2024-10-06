package app;

import java.util.List;

import api.MesApi;
import api.MesSatz;

public class MesApp {
    public static void main(String[] args) {
        StringBuilder bodyBuilder = new StringBuilder();

        List<MesSatz> satz = MesApi.genSatzAuftrag();
        MesApi.setzeWert(satz, "AKNr", "123");
        bodyBuilder.append(MesApi.toString(satz));
        bodyBuilder.append(System.lineSeparator());

        System.out.println(bodyBuilder.toString());
        MesApi.sendeAuftrag(bodyBuilder.toString());

    }

}
