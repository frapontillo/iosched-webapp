package com.conferenceengineer.server.exporters;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Base class for publishers which can send the data for a conference
 * to an endpoint.
 */
public abstract class ExportPublisher implements Runnable {

    private boolean mCompleted = false;

    private IOException mLastIOException = null;

    private ExecutorService mExportExecutor;

    public ExportPublisher() {
        mExportExecutor = Executors.newCachedThreadPool();
    }

    public boolean hasExportCompletedSuccessfully() {
        return mCompleted;
    }

    public IOException getLastIOException() {
        return mLastIOException;
    }

    protected void setComplete() {
        mCompleted = true;
    }

    protected void setLastIOException(IOException e) {
        mLastIOException = e;
    }

    private void submitExporterToExecutor(final Runnable Exporter) {

    }
}
