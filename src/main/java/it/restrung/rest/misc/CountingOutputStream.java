package it.restrung.rest.misc;


import it.restrung.rest.client.APIPostParams;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CountingOutputStream extends FilterOutputStream {

    /**
     * The listener receiving progress
     */
    private APIPostParams listener;

    /**
     * How many bytes have been transfered this far
     */
    private long transferred;

    /**
     * The known file length
     */
    private long fileLength = 0;

    /**
     * Constructor that wraps an outputstream and set the listener and known file length
     *
     * @param out        the output stream
     * @param listener   the listener
     * @param fileLength the file lenght
     */
    public CountingOutputStream(final OutputStream out,
                                APIPostParams listener, long fileLength) {
        super(out);
        this.listener = listener;
        this.transferred = 0;
        this.fileLength = fileLength;
    }

    /**
     * Writes bytes and notifies of progress to the listener
     *
     * @see java.io.FilterOutputStream#write(byte[], int, int)
     */
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
        this.transferred += len;
        int por = (int) (transferred * 100 / fileLength);
        if (listener != null) this.listener.onProgress(this.transferred, por);
    }

    /**
     * Writes bytes and notifies of progress to the listener
     *
     * @see java.io.FilterOutputStream#write(int)
     */
    @Override
    public void write(int b) throws IOException {
        out.write(b);
        this.transferred++;
        int por = (int) (transferred * 100 / fileLength);
        if (listener != null) this.listener.onProgress(this.transferred, por);
    }
}

