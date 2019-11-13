package br.com.labbs.monitor.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;

public class CountingServletResponse extends HttpServletResponseWrapper {

    private final boolean debug;
    private final HttpServletResponse response;
    private CountingServletOutputStream output;
    private CountingPrintWriter writer;

    CountingServletResponse(HttpServletResponse response, boolean debug) throws IOException {
        super(response);
        this.response = response;
        this.debug = debug;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if(output == null){
            output = new CountingServletOutputStream(response.getOutputStream(), debug);
        }
        return output;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if(writer == null){
            writer = new CountingPrintWriter(response.getWriter(), debug);
        }
        return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
        response.flushBuffer();
    }

    long getByteCount() throws IOException {
        if(output != null){
            return output.getByteCount();    
        } else if(writer != null){
            return writer.getCount();
        }
        return 0;
    }

    String getStatusRange() {
        int n = this.getStatus() / 100;
        return n + "xx";
    }

}