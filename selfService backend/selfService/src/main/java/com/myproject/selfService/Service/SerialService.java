package com.myproject.selfService.Service;

import com.fazecast.jSerialComm.SerialPort;
import java.io.InputStream;

public class SerialService {

    private final SerialPort port;
    private final SerialWebSocketHandler webSocketHandler;

    public SerialService(String portName, SerialWebSocketHandler handler) {
        this.webSocketHandler = handler;
        port = SerialPort.getCommPort(portName);
        port.setBaudRate(9600);
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);

        if (port.openPort()) {
            System.out.println("Porta " + portName + " aberta com sucesso!");
        } else {
            System.err.println("Falha ao abrir a porta " + portName);
        }
    }

    public void startListening() {
        try {
            InputStream in = port.getInputStream();
            StringBuilder sb = new StringBuilder();
            boolean reading = false;

            while (true) {
                int b = in.read();
                if (b == -1) continue;

                if (b == 0x02) { // STX
                    sb.setLength(0);
                    reading = true;
                } else if (b == 0x03) { // ETX
                    reading = false;
                    String weightStr = sb.toString();
                    try {
                        int weightInt = Integer.parseInt(weightStr);
                        double weightKg = weightInt / 100.0;

                        // envia pelo WebSocket já com valor calculado
                        webSocketHandler.sendWeight(weightKg);

                        System.out.println("Peso recebido: " + weightKg + " kg");
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao converter peso: [" + weightStr + "]");
                    }
                } else if (reading) {
                    sb.append((char) b);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (port != null && port.isOpen()) {
            port.closePort();
        }
    }
}
