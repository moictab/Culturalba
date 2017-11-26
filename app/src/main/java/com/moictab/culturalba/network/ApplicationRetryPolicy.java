package com.moictab.culturalba.network;

import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;

public class ApplicationRetryPolicy implements RetryPolicy {

    private int currentTimeoutMs;
    private int currentRetryCount;
    private int maxNumRetries;
    private float backoffMultiplier;

    private static final int TIMEOUT_MS = 4000;
    private static final int MAX_RETRIES = 3;
    private static final float BACKOFF_MULT = 1f;

    public ApplicationRetryPolicy() {
        this.currentTimeoutMs = TIMEOUT_MS;
        this.maxNumRetries = MAX_RETRIES;
        this.backoffMultiplier = BACKOFF_MULT;
    }

    @Override
    public int getCurrentTimeout() {
        return this.currentTimeoutMs;
    }

    @Override
    public int getCurrentRetryCount() {
        return this.currentRetryCount;
    }

    @Override
    public void retry(VolleyError error) throws VolleyError {
        currentRetryCount++;
        currentTimeoutMs += (currentTimeoutMs * backoffMultiplier);
        if (currentRetryCount > maxNumRetries) {
            throw error;
        }
    }
}