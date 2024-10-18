package manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse bietet Hilfsmethoden zur Verwaltung von Systemd-Services.
 */
public class SystemdServiceManager {

    /**
     * Methode zum Ausführen eines Shell-Befehls und Rückgabe der Ausgabe.
     *
     * @param command Der auszuführende Shell-Befehl.
     * @return Die Ausgabe des Befehls als String.
     */
    public static String executeCommand(String command) {
        StringBuilder output = new StringBuilder();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            // Ausgabe lesen und speichern
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Warten, bis der Prozess beendet ist
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString().trim();
    }

    // public

    /**
     * Methode zum Abrufen aller Systemd-Services und Erstellen von SystemdService-Objekten.
     *
     * @return Eine Liste von SystemdService-Objekten.
     */
    public static List<SystemdService> services() {
        List<SystemdService> services = new ArrayList<>();
        String command = "systemctl list-units --type=service --no-pager --all";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains(".service")) {
                    // Service-Details extrahieren und SystemdService-Objekt erstellen
                    List<String> columns = List.of(line.trim().split("\\s{2,}")); // Mindestens zwei Leerzeichen
                    if (columns.size() >= 5) {
                        SystemdService service = new SystemdService(
                                columns.get(0),
                                columns.get(1),
                                columns.get(2),
                                columns.get(3),
                                columns.get(4),
                                "",
                                false
                        );

                        services.add(service);
                    }
                }
            }

            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return services;
    }

    /**
     * Methode zum Abrufen aller Systemd-Services mit vollständigen Informationen.
     *
     * @return Eine Liste von SystemdService-Objekten, die alle Systemd-Services mit vollständigen Details darstellen.
     */
    public static List<SystemdService> servicesFull() {
        List<SystemdService> services = services();
        updateActiveSinceIfNeeded(services);
        updateIsServiceFromEtcSystemd(services);
        return services;
    }

    /**
     * Methode zum Abrufen eines einzelnen SystemdService-Objekts anhand des Servicenamens.
     *
     * @param serviceName Der Name des zu suchenden Services (z.B. "apache2.service").
     * @return Das SystemdService-Objekt, das den angegebenen Servicenamen repräsentiert, oder ein leeres Objekt, wenn der Service nicht gefunden wurde.
     */
    public static SystemdService service(String serviceName) {
        SystemdService result = new SystemdService(null, null, null, null, null, null, false);

        List<SystemdService> services = services();

        for (SystemdService service : services) {
            if (service.getName().equals(serviceName)) {
                result = service;
            }
        }

        return result;
    }

    /**
     * Methode zum Abrufen eines einzelnen SystemdService-Objekts mit vollständigen Informationen anhand des Servicenamens.
     *
     * @param serviceName Der Name des zu suchenden Services (z.B. "apache2.service").
     * @return Das SystemdService-Objekt, das den angegebenen Servicenamen repräsentiert, oder ein leeres Objekt, wenn der Service nicht gefunden wurde.
     */
    public static SystemdService serviceFull(String serviceName) {
        SystemdService result = service(serviceName);

        updateActiveSinceIfNeeded(result);
        updateIsServiceFromEtcSystemd(result);

        return result;
    }

    /**
     * Methode zum Starten eines Services.
     *
     * @param serviceName Der Name des Services.
     */
    public static SystemdService startService(String serviceName) {
        executeCommand("systemctl start " + serviceName);
        return serviceFull(serviceName);
    }

    /**
     * Methode zum Stoppen eines Services.
     *
     * @param serviceName Der Name des Services.
     */
    public static SystemdService stopService(String serviceName) {
        executeCommand("systemctl stop " + serviceName);
        return serviceFull(serviceName);
    }

    /**
     * Methode zum Neustarten eines Services.
     *
     * @param serviceName Der Name des Services.
     */
    public static SystemdService restartService(String serviceName) {
        executeCommand("systemctl restart " + serviceName);
        return serviceFull(serviceName);
    }

    /**
     * Methode zum Neuladen eines Services.
     *
     * @param serviceName Der Name des Services.
     */
    public static SystemdService reloadService(String serviceName) {
        executeCommand("systemctl reload " + serviceName);
        return serviceFull(serviceName);
    }

    // private

    /**
     * Ruft den aktiven Zeitstempel des angegebenen Services ab.
     *
     * @param service Der SystemdService, dessen aktiver Zeitstempel abgerufen werden soll.
     * @return Der aktive Zeitstempel des Services oder null, wenn kein gültiger Zeitstempel gefunden wurde.
     */
    private static String activeSince(SystemdService service) {
        String command = "systemctl show " + service.getName() + " -p ActiveEnterTimestamp";

        return executeCommand(command).replace("ActiveEnterTimestamp=", "").trim();
    }

    /**
     * Aktualisiert den aktiven Zeitstempel des angegebenen Services, falls dieser aktiv ist.
     *
     * @param service Der SystemdService, dessen aktiver Zeitstempel aktualisiert werden soll.
     */
    private static void updateActiveSinceIfNeeded(SystemdService service) {
        // Überprüfen, ob der Service aktiv ist
        if ("active".equals(service.getActiveState())) {
            String activeSince = activeSince(service);

            // Setzen des aktiven Zeitstempels, falls verfügbar
            if (activeSince != null && !activeSince.isEmpty()) {
                service.setActiveSince(activeSince);
            }
        }
    }

    /**
     * Aktualisiert den aktiven Zeitstempel für eine Liste von Services, falls diese aktiv sind.
     *
     * @param services Eine Liste von SystemdService-Objekten, deren aktive Zeitstempel aktualisiert werden sollen.
     */
    private static void updateActiveSinceIfNeeded(List<SystemdService> services) {
        for (SystemdService service : services) {
            updateActiveSinceIfNeeded(service);
        }
    }

    /**
     * Methode zum Prüfen, ob ein Service aus /etc/systemd/system/ stammt.
     *
     * @param serviceName Der Name des Services.
     * @return true, wenn der Service aus /etc/systemd/system/ stammt, andernfalls false.
     */
    private static boolean isServiceFromEtcSystemd(SystemdService service) {
        String command = "systemctl show " + service.getName() + " -p FragmentPath";
        String output = executeCommand(command);

        return output != null && output.startsWith("FragmentPath=/etc/systemd/system/");
    }

    /**
     * Aktualisiert den aktiven Zeitstempel des Services, falls der Service aktiv ist.
     *
     * @param service Der SystemdService, dessen aktiver Zeitstempel aktualisiert werden soll.
     */
    private static void updateIsServiceFromEtcSystemd(SystemdService service) {
        service.setFromEtcSystemd(isServiceFromEtcSystemd(service));
    }

    /**
     * Aktualisiert die Herkunft für eine Liste von Services.
     *
     * @param services Eine Liste von SystemdService-Objekten, deren Herkunft aktualisiert werden soll.
     */
    private static void updateIsServiceFromEtcSystemd(List<SystemdService> services) {
        for (SystemdService service : services) {
            updateIsServiceFromEtcSystemd(service);
        }
    }

}
