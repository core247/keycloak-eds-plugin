/**
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package kz.core247.keycloak.eds.kalkan;


import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * The DateUtils class provides utility methods for handling dates and times.
 */
public final class DateUtils {

    private DateUtils() {
    }

    public static final String ISO_8601_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ISO_8601_DATE_TIME_PATTERN);

    /**
     * Converts a {@link Date} to a string representation of the date and time
     * using the ISO 8601 format "yyyy-MM-dd HH:mm:ss".
     *
     * @param date the date to convert to string
     * @return the formatted string representation of the date and time, or null if the input date is null
     */
    public static String dateTime(Date date) {
        if (date == null) {
            return null;
        }
        return formatter.format(date.toInstant().atZone(ZoneId.systemDefault()));
    }

    public static Date getCurrentDate() {
        return new Date();
    }
}
