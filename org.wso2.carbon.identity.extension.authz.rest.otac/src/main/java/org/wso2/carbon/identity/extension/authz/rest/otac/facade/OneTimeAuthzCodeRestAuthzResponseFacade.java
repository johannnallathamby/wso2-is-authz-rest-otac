package org.wso2.carbon.identity.extension.authz.rest.otac.facade;

import org.apache.catalina.connector.Response;
import org.apache.catalina.connector.ResponseFacade;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class OneTimeAuthzCodeRestAuthzResponseFacade extends ResponseFacade {

    private final Response response;
    private final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    private ServletOutputStream servletOutputStream = null;
    private PrintWriter printWriter = null;
    private Object streamUsed = null;

    public OneTimeAuthzCodeRestAuthzResponseFacade(Response response) {
        super(response);
        this.response = response;
    }

    public ServletOutputStream getOutputStream() {

        if ((streamUsed != null) && (streamUsed == printWriter)) {
            throw new IllegalStateException("PrintWriter has been already accessed!!!");
        }

        if (servletOutputStream == null) {
            servletOutputStream = new ServletOutputStream() {

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setWriteListener(WriteListener writeListener) {

                }

                @Override public void write(int b) throws IOException {
                    bytes.write(b);
                }
            };
        }

        streamUsed = servletOutputStream;
        return servletOutputStream;
    }

    public PrintWriter getWriter() {

        if ((streamUsed != null) && (streamUsed == servletOutputStream)) {
            throw new IllegalStateException("ServletOutputStream has been already accessed!!!");
        }

        if (printWriter == null) {
            printWriter = new PrintWriter(new OutputStreamWriter(bytes));
            streamUsed = printWriter;
        }

        return printWriter;
    }

    public ByteArrayOutputStream getBody() {
        return this.bytes;
    }

    public Response getResponse() {
        return this.response;
    }
}
