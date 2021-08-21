package com.example.jdrodi.utilities;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Equivalent to {@link AsyncTask}, but also adds a timeout.  If the task exceeds the timeout,
 * {@link AsyncTaskWithTimeout#onTimeout()} will be called via the UI {@link Thread}.
 */
public abstract class AsyncTaskWithTimeout<Params, Progress, Result>
        extends AsyncTask<Params, Progress, Result> {
    private final long timeout;
    private final TimeUnit units;
    private final Activity context;

    // used for interruption
    private Thread backgroundThread;

    public AsyncTaskWithTimeout(Activity context, long timeout, TimeUnit units) {
        this.context = context;
        this.timeout = timeout;
        this.units = units;
    }

    @Override
    protected final void onPreExecute() {
        Thread timeoutThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // start the timeout ticker
                    AsyncTaskWithTimeout.this.get(timeout, units);
                } catch (InterruptedException e) {
                    Log.w("grok", "Background thread for AsyncTask timeout was interrupted.  Killing timeout thread.");
                    Thread.currentThread().interrupt();
                } catch (TimeoutException e) {
                    Log.d("grok", "Timeout reached.  Interrupting AsyncTask thread and calling #onTimeout.");
                    AsyncTaskWithTimeout.this.interruptTask();
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onTimeout();
                        }
                    });

                } catch (Exception e) {
                    Log.w("grok", e.toString());
                }
            }
        });
        timeoutThread.setDaemon(true);
        onPreExec();
        timeoutThread.start();
    }

    /**
     * Equivalent to {@link AsyncTask#onPreExecute()}
     */
    protected void onPreExec() {
    }

    @Override
    protected final Result doInBackground(Params... params) {
        // save off reference to background thread so it can be interrupted on timeout
        this.backgroundThread = Thread.currentThread();
        return runInBackground(params);
    }

    /**
     * Equivalent to {@link AsyncTask#doInBackground(Object[])}
     */
    protected abstract Result runInBackground(Params... params);


    /**
     * This will be run on the UI thread if the timeout is reached.
     */
    protected void onTimeout() {
    }

    /**
     * Called if the AsyncTask throws an exception.
     * By default wrap in {@link RuntimeException} and let it bubble up to the system.
     */
    protected void onException(ExecutionException e) {
        throw new RuntimeException(e);
    }

    private final void interruptTask() {
        if (backgroundThread != null) {
            Log.w("grok", "Interrupting AsyncTask because of timeout");
            backgroundThread.interrupt();
        }
    }
}