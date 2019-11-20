package com.mobiliya.workshop.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Utility class for some common operations.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtil {
    public static String getEventPayloadJson(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}
