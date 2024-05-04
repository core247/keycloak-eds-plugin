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

import org.junit.Test;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test case for DateUtils.dateTime() method.
 */
public final class DateUtilsTest {
   
    /**
     * Tests DateUtils.dateTime() method with a non-null input date.
     */
    @Test
    public void testDateTime() {
        Date testDate = Date.from(java.time.LocalDateTime.of(2022, 12, 25, 10, 20, 30).atZone(ZoneId.systemDefault()).toInstant());
        String expected = "2022-12-25 10:20:30";
        String actual = DateUtils.dateTime(testDate);
        assertEquals("Test when date is not null",expected, actual);
    }

    /**
     * Tests DateUtils.dateTime() method with null as the input date.
     */
    @Test
    public void testDateTimeWithNullDate() {
        Date nullDate = null;
        String actual = DateUtils.dateTime(nullDate);
        assertNull("Test when date is null", actual);
    }
}