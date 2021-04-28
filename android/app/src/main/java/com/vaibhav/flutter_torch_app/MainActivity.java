package com.vaibhav.flutter_torch_app;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "com.vaibhav.fluttertorchapp/testing";


    private boolean isFlashOn = false;
    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler(
                        (call, result) -> {
                            if(call.method.equals("toggleFlashlight")) {
                                String message = toggleFlashlight();

                                if(message != null)
                                    result.success(message);
                                else
                                    result.error("ERROR", "Cannot turn on the flashlight", null);
                            }
                            else
                                result.notImplemented();
                        }
                );
    }

    private String toggleFlashlight(){

        CameraManager camManager;
        Camera mCamera;
        Parameters parameters;
        if(isFlashOn) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    String cameraId;
                    camManager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
                    if (camManager != null) {
                        cameraId = camManager.getCameraIdList()[0];
                        camManager.setTorchMode(cameraId, false);
                    }
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            } else {
                mCamera = Camera.open();
                parameters = mCamera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(parameters);
                mCamera.stopPreview();
            }
            isFlashOn = !isFlashOn;
            return "Flashlight turned OFF!";
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                try {
                    String cameraId = null;
                    camManager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
                    if(camManager != null) {
                        cameraId = camManager.getCameraIdList()[0];
                        camManager.setTorchMode(cameraId, true);
                    }
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
            else {
                mCamera = Camera.open();
                parameters = mCamera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(parameters);
                mCamera.startPreview();
            }
            isFlashOn = !isFlashOn;
            return "Flashlight turned ON!";
        }
    }
}
