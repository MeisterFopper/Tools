package manager;

/**
 * Die Klasse SystemdService repräsentiert einen Systemd-Service und speichert Informationen
 * über den Namen, Ladezustand, aktiven Zustand, Unterzustand und die Beschreibung des Services.
 */
public class SystemdService {
    private String name;
    private String loadState;
    private String activeState;
    private String subState;
    private String description;
    private String activeSince;
    private boolean isFromEtcSystemd;

    /**
     * Konstruktor zum Erstellen eines neuen SystemdService-Objekts mit den angegebenen Informationen.
     *
     * @param name            Der Name des Services (z.B. "apache2.service").
     * @param loadState       Der Ladezustand des Services (z.B. "loaded").
     * @param activeState     Der aktive Zustand des Services (z.B. "active").
     * @param subState        Der Unterzustand des Services (z.B. "running").
     * @param description     Eine kurze Beschreibung des Services.
     * @param activeSince     Der Zeitpunkt, seit dem der Service aktiv ist.
     * @param isFromEtcSystemd Gibt an, ob der Service aus /etc/systemd/system/ stammt.
     */
    public SystemdService(String name, String loadState, String activeState, String subState, String description, String activeSince, boolean isFromEtcSystemd) {
        this.name = name;
        this.loadState = loadState;
        this.activeState = activeState;
        this.subState = subState;
        this.description = description;
        this.activeSince = activeSince;
        this.isFromEtcSystemd = isFromEtcSystemd;
    }

    /**
     * Setzt den Namen des Services.
     *
     * @param name Der neue Name des Services.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt den Namen des Services zurück.
     *
     * @return Der Name des Services.
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Ladezustand des Services.
     *
     * @param loadState Der neue Ladezustand des Services.
     */
    public void setLoadState(String loadState) {
        this.loadState = loadState;
    }

    /**
     * Gibt den Ladezustand des Services zurück.
     *
     * @return Der Ladezustand des Services.
     */
    public String getLoadState() {
        return loadState;
    }

    /**
     * Setzt den aktiven Zustand des Services.
     *
     * @param activeState Der neue aktive Zustand des Services.
     */
    public void setActiveState(String activeState) {
        this.activeState = activeState;
    }

    /**
     * Gibt den aktiven Zustand des Services zurück.
     *
     * @return Der aktive Zustand des Services.
     */
    public String getActiveState() {
        return activeState;
    }

    /**
     * Setzt den Unterzustand des Services.
     *
     * @param subState Der neue Unterzustand des Services.
     */
    public void setSubState(String subState) {
        this.subState = subState;
    }

    /**
     * Gibt den Unterzustand des Services zurück.
     *
     * @return Der Unterzustand des Services.
     */
    public String getSubState() {
        return subState;
    }

    /**
     * Setzt die Beschreibung des Services.
     *
     * @param description Die neue Beschreibung des Services.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gibt die Beschreibung des Services zurück.
     *
     * @return Die Beschreibung des Services.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setzt den Zeitpunkt, seit dem der Service aktiv ist.
     *
     * @param activeSince Der neue Zeitpunkt, seit dem der Service aktiv ist.
     */
    public void setActiveSince(String activeSince) {
        this.activeSince = activeSince;
    }

    /**
     * Gibt den Zeitpunkt zurück, seit dem der Service aktiv ist.
     *
     * @return Der Zeitpunkt, seit dem der Service aktiv ist.
     */
    public String getActiveSince() {
        return activeSince;
    }

    /**
     * Setzt, ob der Service aus /etc/systemd/system/ stammt.
     *
     * @param isFromEtcSystemd true, wenn der Service aus /etc/systemd/system/ stammt, andernfalls false.
     */
    public void setFromEtcSystemd(boolean isFromEtcSystemd) {
        this.isFromEtcSystemd = isFromEtcSystemd;
    }

    /**
     * Gibt zurück, ob der Service aus /etc/systemd/system/ stammt.
     *
     * @return true, wenn der Service aus /etc/systemd/system/ stammt, andernfalls false.
     */
    public boolean isFromEtcSystemd() {
        return isFromEtcSystemd;
    }

    /**
     * Gibt eine String-Darstellung des SystemdService-Objekts zurück.
     *
     * @return Eine String-Darstellung des SystemdService-Objekts, die alle Attribute enthält.
     */
    @Override
    public String toString() {
        return "SystemdService{" +
                "name='" + name + '\'' +
                ", loadState='" + loadState + '\'' +
                ", activeState='" + activeState + '\'' +
                ", subState='" + subState + '\'' +
                ", description='" + description + '\'' +
                ", activeSince='" + activeSince + '\'' +
                ", isFromEtcSystemd=" + isFromEtcSystemd +
                '}';
    }
}
