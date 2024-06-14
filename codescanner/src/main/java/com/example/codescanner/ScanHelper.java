/*
 * Copyright 2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.codescanner;

import android.content.Context;
import android.content.Intent;

/**
 * Created by xingcheng on 2018/8/17.
 */

public class ScanHelper {

    private static class Holder {
        private static ScanHelper instance = new ScanHelper();
    }

    private ScanCallback scanCallback;

    private ScanHelper() {
    }

    public static ScanHelper getInstance() {
        return Holder.instance;
    }

    public void scan(Context context, ScanCallback scanCallback) {
        if (context == null) {
            return;
        }
        this.scanCallback = scanCallback;
//        context.startActivity(new Intent(context, CustomScanActivity.class));
    }

    void notifyScanResult(boolean isProcessed, Intent resultData) {
        if (scanCallback != null) {
            scanCallback.onScanResult(isProcessed, resultData);
            scanCallback = null;
        }
    }

    public interface ScanCallback {
        void onScanResult(boolean isProcessed, Intent result);
    }
}
