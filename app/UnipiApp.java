package app;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.parser.ParseException;

import api.UnipiApi;
import api.UnipiDevice;
import api.UnipiDeviceRO;
import api.UnipiDeviceAO;

public class UnipiApp {

    private static void printVersion(UnipiApi unipi) {
        if (unipi != null) {
            try {
                System.out.println("Unipi API: " + unipi.getVersion());
            } catch (IOException | URISyntaxException | ParseException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("No Connection.");
        }
    }
    
    private static void printDevice(UnipiDevice device) {
        if (device != null) {
            System.out.println("Board: " + device.getBoardCount().toString());
            System.out.println("- Model: " + device.getModel());
            System.out.println("- Circuit: " + device.getCircuit());
            System.out.println("- Family: " + device.getFamily());
            System.out.println("- Serial: " + device.getSn().toString());
        } else {
            System.out.println("No device.");
        }
    }

    private static void printDeviceList(UnipiApi unipi) {
        List<UnipiDevice> devices = new ArrayList<>();
        try {
            devices = unipi.getDeviceList();
        } catch (IOException | URISyntaxException | ParseException e) {
            System.out.println(e.getMessage());
        }

        if (devices != null) {
            for (UnipiDevice device : devices) {
                printDevice(device);
            }
        } else {
            System.out.println("No devices.");
        }
    }

    private static void printRelay(UnipiDeviceRO relais) {
        if (relais != null) {
            System.out.println("Relais: " + relais.getCircuit() + ", Value: " + relais.getValue());
        } else {
            System.out.println("No device.");
        }
    }

    private static void printRelayList(UnipiApi unipi) {
        List<UnipiDeviceRO> roList = new ArrayList<>();
        try {
            roList = unipi.getRelaisList();
        } catch (IOException | URISyntaxException | ParseException e) {
            System.out.println(e.getMessage());
        }

        if (roList != null) {
            for (UnipiDeviceRO ro : roList) {
                printRelay(ro);
            }
        } else {
            System.out.println("No Relais.");
        }
    }

    private static void printAnalogOut(UnipiDeviceAO analogOut) {
        if (analogOut != null) {
            System.out.println(analogOut.toString());
        } else {
            System.out.println("No analog out device.");
        }
    }

    private static void printAnalogOutList(UnipiApi unipi) {
        List<UnipiDeviceAO> aoList = new ArrayList<>();
        try {
            aoList = unipi.getAnalogOutList();
        } catch (IOException | URISyntaxException | ParseException e) {
            System.out.println(e.getMessage());
        }

        if (aoList != null) {
            for (UnipiDeviceAO ao : aoList) {
                printAnalogOut(ao);
            }
        } else {
            System.out.println("No analog out list.");
        }
    }

    public static void main(String[] args) {
        String action = "";

        if (args.length > 0) {
            action = args[0];
        }

        UnipiApi unipi = new UnipiApi("UNIPI1-sn1951727616", "8080");
        printVersion(unipi);

        switch (action) {
            case "devices":
                printDeviceList(unipi);
                break;

            case "ro":
                if (args.length > 1) {
                    String circuit = args[1];
                    try {
                        printRelay(unipi.getRelais(circuit));
                    } catch (IOException | URISyntaxException | ParseException e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    printRelayList(unipi);
                }
                break;

            case "ao":
                if (args.length > 1) {
                    String circuit = args[1];
                    try {
                        printAnalogOut(unipi.getAnalogOut(circuit));
                    } catch (IOException | URISyntaxException | ParseException e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    printAnalogOutList(unipi);
                }
                break;

            default:
                printDeviceList(unipi);
                break;
        }

    }

}
