import java.io.*;
import java.util.zip.InflaterInputStream;

public class MochiDecrypt16Bit {

    public boolean decrypt(InputStream is, OutputStream os) throws IOException {
        byte[] payload = readInputStream(is);
        if (!handleXor(payload)) {
            return false;
        }
        try (InflaterInputStream inflater = new InflaterInputStream(new ByteArrayInputStream(payload))) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inflater.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }
        return true;
    }

    private byte[] readInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }
        return baos.toByteArray();
    }

    private boolean handleXor(byte[] payload) {
        if (payload.length < 16) {
            return false;
        }
        int[] S = new int[256];
        int i = 0;
        int j;
        int k;
        int n = payload.length - 16;
        while (i < 256) {
            S[i] = i;
            i++;
        }
        j = 0;
        i = 0;
        while (i < 256) {
            j = (j + S[i] + (payload[n + (i & 15)] & 0xff)) & 255;
            int u = S[i];
            S[i] = S[j];
            S[j] = u;
            i++;
        }

        if (n > 0x20000) {
            n = 0x20000;
        }
        j = 0;
        i = 0;
        k = 0;
        while (k < n) {
            i = (i + 1) & 255;
            int u = S[i];
            j = (j + u) & 255;
            int v = S[j];
            S[i] = v;
            S[j] = u;
            payload[k] = (byte) ((payload[k] & 0xff) ^ S[u + v & 255]);
            k++;
        }
        return true;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java MochiDecrypt16Bit <inputFile>");
            return;
        }

        String inputFile = args[0];
        String outputFile = inputFile.replace(".bin", ".swf");

        MochiDecrypt16Bit packer = new MochiDecrypt16Bit();

        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile)) {

            if (!packer.decrypt(fis, fos)) {
                System.out.println("Decryption failed!");
            } else {
                System.out.println("File decrypted successfully: " + outputFile);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
