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
        String cleanedETag = eTag.replaceAll("^\"|\"$", "").trim();
        String generatedETag = buildETag(version);
        return cleanedETag.equals(generatedETag);
    }

}
