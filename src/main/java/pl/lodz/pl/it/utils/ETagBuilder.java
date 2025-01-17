package pl.lodz.pl.it.utils;

import pl.lodz.pl.it.config.KeyGenerator;
import org.springframework.util.DigestUtils;

public class ETagBuilder {

    public static String buildETag(String... values) {
        StringBuilder eTag = new StringBuilder();
        for (String value : values) {
            eTag.append(value);
        }
        eTag.append(KeyGenerator.getEtagSecretKey()); // Add a secret key to make ETag more secure
        return DigestUtils.md5DigestAsHex(eTag.toString().getBytes()); // Generate MD5 hash from the concatenated string
    }

    public static boolean isETagValid(String eTag, String version) {
        // Clean the received eTag by trimming and removing quotes if present
        String cleanedETag = eTag.replaceAll("^\"|\"$", "").trim();

        // Generate the expected ETag from version (and any other values you might need)
        String generatedETag = buildETag(version);

        System.out.println("Cleaned received eTag: " + cleanedETag);  // Debugging log to see cleaned eTag
        System.out.println("Generated expected eTag: " + generatedETag);  // Debugging log to see generated eTag

        // Compare the cleaned eTag with the generated one
        return cleanedETag.equals(generatedETag);
    }

}
