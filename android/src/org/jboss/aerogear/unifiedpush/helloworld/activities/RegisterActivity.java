/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.unifiedpush.helloworld.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.android.unifiedpush.PushRegistrar;
import org.jboss.aerogear.unifiedpush.helloworld.R;

import java.net.URI;
import java.net.URISyntaxException;
import org.jboss.aerogear.android.unifiedpush.AeroGearGCMPushConfiguration;
import org.jboss.aerogear.android.unifiedpush.RegistrarManager;

import static org.jboss.aerogear.unifiedpush.helloworld.Constants.*;

public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        register();
    }

    private void register() {

        try {

            AeroGearGCMPushConfiguration config = RegistrarManager.config("register", AeroGearGCMPushConfiguration.class);

            config.addSenderId(GCM_SENDER_ID)
                    .setPushServerURI(new URI(UNIFIED_PUSH_URL))
                    .setVariantID(VARIANT_ID)
                    .setSecret(SECRET);

            PushRegistrar registrar = config.asRegistrar();
            registrar.register(getApplicationContext(), new Callback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    Toast.makeText(getApplicationContext(),
                            getApplicationContext().getString(R.string.registration_successful),
                            Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplicationContext(), MessagesActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    finish();
                }
            });

        } catch (IllegalArgumentException e) {
            handleException(e);
        } catch (URISyntaxException e) {
            handleException(e);
        }

    }

    private void handleException(Exception e) {
        final String msg = "Unable to reach UnifiedPushServer at \"" + UNIFIED_PUSH_URL + "\".";
        Log.e("RegisterActivity", msg, e);
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        finish();
    }

}
