package com.aluxian.zerodays.utils;

import android.os.AsyncTask;

/**
 * Helper class used to run operations off the main thread.
 */
public class Async {

    /**
     * Run fn in a background thread then give its result to the callback, which is called in the same thread that called this method.
     *
     * @param fn       The function to execute in background.
     * @param callback The callback to call after fn is executed.
     * @param <T>      The return type of fn, and the type of the result that is given to the callback.
     */
    public static <T> void run(Function<T> fn, Callback<T> callback) {
        new AsyncTask<Void, Void, T>() {
            @Override
            protected T doInBackground(Void... params) {
                return fn.apply();
            }

            @Override
            protected void onPostExecute(T result) {
                callback.run(result);
            }
        }.execute();
    }

    /**
     * Run fn in a background thread.
     *
     * @param fn The function to execute in background.
     */
    public static void run(Runnable fn) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                fn.run();
                return null;
            }
        }.execute();
    }

    public static interface Function<T> {
        public T apply();
    }

    public static interface Callback<T> {
        public void run(T result);
    }

}
