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
package jcrystal.mobile.net.utils;
public class RequestError extends Exception{
    public static final int UNAUTHORIZED_CODE = 3;
    public static final int UPGRADE_REQUIRED_CODE = 426;

    public final TipoError tipoError;
    public final String mensaje;
    public final int codigo;

    public RequestError(TipoError tipoError, String mensaje) {
    	super(tipoError + ": " + mensaje);
        this.tipoError = tipoError;
        this.mensaje = mensaje;
        this.codigo = -1;
    }
    public RequestError(int codigo, String mensaje) {
    	super("Error: " + mensaje);
        this.tipoError = TipoError.ERROR;
        this.mensaje = mensaje;
        this.codigo = codigo;
    }
}
