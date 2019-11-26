package com.mobiliya.workshop.util;

import com.mobiliya.workshop.exception.FailureMetaData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for some common operations.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtil {

    public static String getTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat(CommonConstants.DATE_FORMAT);
        Date date = new Date();
        return sdf.format(date.getTime());
    }

    public static FailureMetaData getDataValidationFailureResponse(
            String failedClassName, String errorDescription, String data) {
        return FailureMetaData.builder()
                .failedClass(failedClassName)
                .description(errorDescription)
                .precursorDataString(data)
                .timestamp(getTimeStamp())
                .build();
    }
}
