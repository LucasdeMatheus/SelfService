import com.fazecast.jSerialComm.SerialPort;
import java.io.OutputStream;
import java.util.Random;

public class ScaleSimulator {

    public static void main(String[] args) throws Exception {
        SerialPort port = SerialPort.getCommPort("COM5");
        port.setBaudRate(9600);
        port.openPort();

        Random rand = new Random();

        while (true) {
            // Peso aleatório 1.00 a 5.99 kg
            double weight = 1 + rand.nextDouble() * 5;

            // Converte para centésimos e gera 6 dígitos
            int weightInt = (int) Math.round(weight * 100);
            String weightStr = String.format("%06d", weightInt);

            // Monta o pacote STX + peso + ETX
            byte[] packet = new byte[] { 0x02 }; // STX
            byte[] weightBytes = weightStr.getBytes();
            byte[] etx = new byte[] { 0x03 }; // ETX

            byte[] fullPacket = new byte[packet.length + weightBytes.length + etx.length];
            System.arraycopy(packet, 0, fullPacket, 0, packet.length);
            System.arraycopy(weightBytes, 0, fullPacket, packet.length, weightBytes.length);
            System.arraycopy(etx, 0, fullPacket, packet.length + weightBytes.length, etx.length);

            OutputStream out = port.getOutputStream();
            out.write(fullPacket);
            out.flush();

            System.out.println("Peso enviado: " + weightStr);
            Thread.sleep(2000);
        }
    }
}
