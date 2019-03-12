/*
 * The MIT License
 *
 * Copyright (c) 2018-2019 German Augusto Sotelo Arevalo

 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package jcrystal.mobile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public enum DateType {
 	DATE_TIME("yyyyMMddHHmmZ"),
    TIME("HHmmZ"),
    DATE("yyyyMMddZ"),
    DATE_SECONDS("yyyyMMddHHmmssZ"),
    TIME_SECONDS("HHmmssZ"),
    DATE_MILIS("yyyyMMddHHmmssSSSZ"),
    TIME_MILIS("HmmssSSSZ");

    public final SimpleDateFormat FORMAT;
    public final String format;
    DateType(String format){
        FORMAT = new SimpleDateFormat(this.format = format);
    }
}
