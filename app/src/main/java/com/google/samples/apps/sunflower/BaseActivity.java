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

package com.google.samples.apps.sunflower;


import android.app.Activity;
import android.os.Bundle;

//import com.mpaas.mas.adapter.api.MPTracker;

public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        MPTracker.onActivityCreate(this);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        MPTracker.onActivityWindowFocusChanged(this, hasFocus);
    }
    @Override
    protected void onResume() {
        super.onResume();
//        MPTracker.onActivityResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
//        MPTracker.onActivityPause(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        MPTracker.onActivityDestroy(this);
    }
}
