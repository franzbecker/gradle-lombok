package net.franz_becker.gradle.lombok.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Calculate SHA-256 hash for a given file.
 * <p>
 * Gradle supports only an internal API for this (@link{org.gradle.internal.hash.HashUtil}).
 */
public class HashUtil {

    /**
     * Calculates the SHA-256 to a given file.
     *
     * @param file the file
     * @return the SHA-256
     */
    public String calculateSha256(File file) {
        try (RandomAccessFile inputStream = new RandomAccessFile(file, "r"); FileChannel channel = inputStream.getChannel()) {
            // Calculate SHA-256
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            sha256.update(buffer);

            // Convert to String
            byte digest[] = sha256.digest();
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                String hex = Integer.toHexString(0xff & digest[i]);
                if (hex.length() == 1) {
                    result.append('0');
                }
                result.append(hex);
            }

            return result.toString();
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException("Could not calculate SHA-256 hash of file: " + file, e);
        }
    }

}
