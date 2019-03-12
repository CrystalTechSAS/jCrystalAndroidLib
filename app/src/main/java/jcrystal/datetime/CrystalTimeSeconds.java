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
package jcrystal.datetime;
import java.util.*;
import java.text.ParseException;
public class CrystalTimeSeconds extends java.util.Date{
    public static final String FORMAT = "HHmmss";
    public static final java.text.SimpleDateFormat SDF = new java.text.SimpleDateFormat(FORMAT);
    static {SDF.setTimeZone(TimeZone.getTimeZone("UTC"));}
    public CrystalTimeSeconds(String text)throws ParseException{
        super(SDF.parse(text).getTime());
    }
    public CrystalTimeSeconds(long time){
        super(time);
    }
    public static final java.text.SimpleDateFormat SDF_SIMPLE_TIME = new java.text.SimpleDateFormat("HH:mm");
    public static final java.text.SimpleDateFormat SDF_SIMPLE_DATE = new java.text.SimpleDateFormat("dd/MM/yyyy");
    public static final java.text.SimpleDateFormat SDF_SIMPLE_DATE_TEXT = new java.text.SimpleDateFormat("dd MMM yyyy");
    public static final java.text.SimpleDateFormat SDF_SIMPLE_DATE_TIME = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static CrystalTimeSeconds now(){
        return new CrystalTimeSeconds(System.currentTimeMillis());
    }
    public static CrystalTimeSeconds today(){
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(GregorianCalendar.HOUR_OF_DAY, 0);
        gc.set(GregorianCalendar.MINUTE, 0);
        gc.set(GregorianCalendar.MILLISECOND, 0);
        return new CrystalTimeSeconds(gc.getTimeInMillis());
    }
    public static CrystalTimeSeconds toMonth(){
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(GregorianCalendar.HOUR_OF_DAY, 0);
        gc.set(GregorianCalendar.MINUTE, 0);
        gc.set(GregorianCalendar.MILLISECOND, 0);
        gc.set(GregorianCalendar.DAY_OF_MONTH, 1);
        return new CrystalTimeSeconds(gc.getTimeInMillis());
    }
    public static CrystalTimeSeconds toWeek(){
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(GregorianCalendar.HOUR_OF_DAY, 0);
        gc.set(GregorianCalendar.MINUTE, 0);
        gc.set(GregorianCalendar.MILLISECOND, 0);
        gc.set(GregorianCalendar.DAY_OF_WEEK, 0);
        return new CrystalTimeSeconds(gc.getTimeInMillis());
    }
    public CrystalTimeSeconds add(int field, int value){
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(this);
        gc.add(field, value);
        return new CrystalTimeSeconds(gc.getTimeInMillis());
    }
    public CrystalTimeSeconds add(long time){
        return new CrystalTimeSeconds(getTime() + time);
    }
    public String toSimpleTimeFormat(){
        return SDF_SIMPLE_TIME.format(this);
    }
    public String toSimpleDateFormat(){
        return SDF_SIMPLE_DATE.format(this);
    }
    public String toSimpleDateTextFormat(){
        return SDF_SIMPLE_DATE_TEXT.format(this);
    }
    public String toSimpleDateTimeFormat(){
        return SDF_SIMPLE_DATE_TIME.format(this);
    }
    public static int compare(CrystalTimeSeconds f1, CrystalTimeSeconds f2){
        return f1 == null && f2 == null?0: f1 == null? 1: f2 == null?-1 : Long.compare(f1.getTime(), f2.getTime());
    }
    public CrystalTimeSeconds(){
        super();
    }
    public String format(){
        return SDF.format(this);
    }
}
