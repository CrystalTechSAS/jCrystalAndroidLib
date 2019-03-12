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
package jcrystal;
import java.io.PrintStream;
public class PrintWriterUtils {

	public static void print(PrintStream pw, String s1) {
		pw.print(s1);
	}
	public static void print(PrintStream pw, String s1, String s2) {
		pw.print(s1);
		pw.print(s2);
	}
	public static void print(PrintStream pw, String s1, int s2) {
		pw.print(s1);
		pw.print(s2);
	}
	public static void print(PrintStream pw, String s1, Integer s2) {
		pw.print(s1);
		pw.print(s2);
	}
	public static void print(PrintStream pw, String s1, long s2) {
		pw.print(s1);
		pw.print(s2);
	}
	public static void print(PrintStream pw, String s1, Long s2) {
		pw.print(s1);
		pw.print(s2);
	}
	public static void print(PrintStream pw, String s1, boolean s2) {
		pw.print(s1);
		pw.print(s2);
	}
	public static void print(PrintStream pw, String s1, Boolean s2) {
		pw.print(s1);
		pw.print(s2);
	}
	public static void print(PrintStream pw, String s1, Double s2) {
		pw.print(s1);
		if(s2 != null && Double.isNaN(s2))
			pw.print("\"NaN\"");
		else
			pw.print(s2);
	}
	public static void print(PrintStream pw, String s1, double s2) {
		pw.print(s1);
		if(Double.isNaN(s2))
			pw.print("\"NaN\"");
		else
			pw.print(s2);
	}

	public static void print(PrintStream pw, String s1, String s2, String s3) {
		pw.print(s1);
		pw.print(s2);
		pw.print(s3);
	}
	public static void print(PrintStream pw, String s1, Long s2, String s3) {
		pw.print(s1);
		pw.print(s2);
		pw.print(s3);
	}
	public static void print(PrintStream pw, String s1, long s2, String s3) {
		pw.print(s1);
		pw.print(s2);
		pw.print(s3);
	}
	public static void print(PrintStream pw, String s1, String s2, String s3, String s4) {
		pw.print(s1);
		pw.print(s2);
		pw.print(s3);
		pw.print(s4);
	}
	
	public static boolean printJsonProp(PrintStream pw, boolean first, String s1, String s2) {
		if(s2!=null) {
			if(!first)
				pw.print(",");
			pw.print(s1);
			pw.print(JSONUtils.jsonQuote(s2));
			return false;
		}
		return first;
	}
	public static boolean printJsonProp(PrintStream pw, boolean first, String s1, Long s2) {
		if(s2!=null) {
			if(!first)
				pw.print(",");
			pw.print(s1);
			pw.print(JSONUtils.jsonQuote(s2));
			return false;
		}
		return first;
	}
	public static boolean printJsonProp(PrintStream pw, boolean first, String s1, Double s2) {
		if(s2!=null) {
			if(!first)
				pw.print(",");
			pw.print(s1);
			pw.print(JSONUtils.jsonQuote(s2));
			return false;
		}
		return first;
	}
	public static boolean printJsonProp(PrintStream pw, boolean first, String s1, Integer s2) {
		if(s2!=null) {
			if(!first)
				pw.print(",");
			pw.print(s1);
			pw.print(JSONUtils.jsonQuote(s2));
			return false;
		}
		return first;
	}
	public static boolean printJsonProp(PrintStream pw, boolean first, String s1, Boolean s2) {
		if(s2!=null) {
			if(!first)
				pw.print(",");
			pw.print(s1);
			pw.print(JSONUtils.jsonQuote(s2));
			return false;
		}
		return first;
	}
	//TO JSON UTILS
	public static <E> void toJson(java.io.PrintStream _pw, java.lang.Iterable<E> lista, Consumer<E> writer){
		_pw.print("[");
		java.util.Iterator<E> iterator = lista.iterator();
		if(iterator.hasNext()){
			E valor = iterator.next();
			writer.accept(valor);
			while(iterator.hasNext()){
				valor = iterator.next();
				_pw.print(",");
				writer.accept(valor);
			}
		}
		_pw.print("]");
	}
	public static <T> void toJson(java.io.PrintStream _pw, java.util.Map<Long, T> mapa, Consumer<T> writer){
		_pw.print("{");
		java.util.Iterator<java.util.Map.Entry<Long, T>> iterator = mapa.entrySet().iterator();
		if(iterator.hasNext()){
			java.util.Map.Entry<Long, T> valor = iterator.next();
			jcrystal.PrintWriterUtils.print(_pw, "\"", valor.getKey(), "\":");
			writer.accept(valor.getValue());
			while(iterator.hasNext()){
				valor = iterator.next();
				jcrystal.PrintWriterUtils.print(_pw, ",\"", valor.getKey(), "\":");
				writer.accept(valor.getValue());
			}
		}
		_pw.print("}");
	}
	public static interface Consumer<T>{
		public void accept(T item);
	}
}
