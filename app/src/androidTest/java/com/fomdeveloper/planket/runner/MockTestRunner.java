package com.fomdeveloper.planket.runner;

import android.Manifest;
import android.app.Application;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.test.runner.AndroidJUnitRunner;
import android.util.Log;


import com.fomdeveloper.planket.injection.MockPlanketApplication;

import java.lang.reflect.Method;

import static android.content.Context.KEYGUARD_SERVICE;
import static android.content.Context.POWER_SERVICE;
import static android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP;
import static android.os.PowerManager.FULL_WAKE_LOCK;
import static android.os.PowerManager.ON_AFTER_RELEASE;
import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * Created by Fernando on 24/12/2016.
 */

public class MockTestRunner extends AndroidJUnitRunner {
    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return super.newApplication(cl, MockPlanketApplication.class.getName(), context);
    }

    @Override
    public void onStart() {

        runOnMainSync(new Runnable() {
            @Override
            public void run() {
                Context app = getTargetContext().getApplicationContext();

                disableAnimations(app);

                String name = MockTestRunner.class.getSimpleName();
                // Unlock the device so that the tests can input keystrokes.
                KeyguardManager keyguard = (KeyguardManager) app.getSystemService(KEYGUARD_SERVICE);
                keyguard.newKeyguardLock(name).disableKeyguard();
                // Wake up the screen.
                PowerManager power = (PowerManager) app.getSystemService(POWER_SERVICE);
                power.newWakeLock(FULL_WAKE_LOCK | ACQUIRE_CAUSES_WAKEUP | ON_AFTER_RELEASE, name)
                        .acquire();
            }
        });

        super.onStart();
    }

    @Override
    public void finish(int resultCode, Bundle results) {
        super.finish(resultCode, results);
        enableAnimations(getInstrumentation().getContext());
    }

    //<editor-fold desc="Animations">
    void disableAnimations(Context context) {
        int permStatus = context.checkCallingOrSelfPermission(Manifest.permission.SET_ANIMATION_SCALE);
        if (permStatus == PackageManager.PERMISSION_GRANTED) {
            setSystemAnimationsScale(0.0f);
        }
    }

    void enableAnimations(Context context) {
        int permStatus = context.checkCallingOrSelfPermission(Manifest.permission.SET_ANIMATION_SCALE);
        if (permStatus == PackageManager.PERMISSION_GRANTED) {
            setSystemAnimationsScale(1.0f);
        }
    }

    private void setSystemAnimationsScale(float animationScale) {
        try {
            Class<?> windowManagerStubClazz = Class.forName("android.view.IWindowManager$Stub");
            Method asInterface = windowManagerStubClazz.getDeclaredMethod("asInterface", IBinder.class);
            Class<?> serviceManagerClazz = Class.forName("android.os.ServiceManager");
            Method getService = serviceManagerClazz.getDeclaredMethod("getService", String.class);
            Class<?> windowManagerClazz = Class.forName("android.view.IWindowManager");
            Method setAnimationScales = windowManagerClazz.getDeclaredMethod("setAnimationScales", float[].class);
            Method getAnimationScales = windowManagerClazz.getDeclaredMethod("getAnimationScales");

            IBinder windowManagerBinder = (IBinder) getService.invoke(null, "window");
            Object windowManagerObj = asInterface.invoke(null, windowManagerBinder);
            float[] currentScales = (float[]) getAnimationScales.invoke(windowManagerObj);
            for (int i = 0; i < currentScales.length; i++) {
                currentScales[i] = animationScale;
            }
            setAnimationScales.invoke(windowManagerObj, new Object[]{currentScales});
        } catch (Exception e) {
            Log.e("SystemAnimations", "Could not change animation scale to " + animationScale + " :'(");
        }
    }
}
