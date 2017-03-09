package pl.consdata.security.smuggler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BitFlippingAttack {

    private static final int BMP_HEADER_SIZE = 138;

    private static final int PAYLOAD_WIDTH = 600;

    private static final int PAYLOAD_HEIGHT = 100;

    private static final int BMP_BLACK = -16777216;

    private static final int BYTES_PER_PIXEL = 4;

    public byte[] attack(String sourceFile, String text) throws IOException {
        BufferedImage payload = renderTextAsBitmap(text);
        BufferedImage sourceImage = ImageIO.read(new File(sourceFile));
        byte[] source = Files.readAllBytes(Paths.get(sourceFile));

        for (int y = 0; y < PAYLOAD_HEIGHT; y++) {
            for (int x = 0; x < PAYLOAD_WIDTH; x++) {
                int offset = BMP_HEADER_SIZE + (y * sourceImage.getWidth() * BYTES_PER_PIXEL) + (x * BYTES_PER_PIXEL);
                if (payload.getRGB(x, PAYLOAD_HEIGHT - y - 1) != BMP_BLACK) {
                    source[offset] = (byte) (source[offset] ^ Byte.MAX_VALUE);
                    source[offset + 1] = (byte) (source[offset + 1] ^ Byte.MAX_VALUE);
                    source[offset + 2] = (byte) (source[offset + 2] ^ Byte.MAX_VALUE);
                }
            }
        }

        return source;
    }

    private BufferedImage renderTextAsBitmap(String text) {
        final BufferedImage payload = new BufferedImage(PAYLOAD_WIDTH, PAYLOAD_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);

        Graphics2D graphics = (Graphics2D) payload.getGraphics();
        Font currentFont = graphics.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 6F);
        graphics.setFont(newFont);
        graphics.drawString(text.replaceAll("", " "), 0, 60);
        graphics.dispose();

        return payload;
    }
}
