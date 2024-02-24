package com.k21091.xrstudywatchapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.core.exceptions.UnavailableApkTooOldException
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException
import com.google.ar.core.exceptions.UnavailableSdkTooOldException
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException
import com.k21091.xrstudywatchapp.ar.kotlin.common.helpers.ARCoreSessionLifecycleHelper
import com.k21091.xrstudywatchapp.ar.helpers.CameraPermissionHelper
import com.k21091.xrstudywatchapp.ar.helpers.DepthSettings
import com.k21091.xrstudywatchapp.ar.helpers.FullScreenHelper
import com.k21091.xrstudywatchapp.ar.helpers.InstantPlacementSettings
import com.k21091.xrstudywatchapp.ar.samplerender.SampleRender
import com.k21091.xrstudywatchapp.ar.kotlin.ArRenderer
import com.k21091.xrstudywatchapp.ar.kotlin.ArView
import com.k21091.xrstudywatchapp.ui.theme.XRStudyWatchAppTheme
import com.k21091.xrstudywatchapp.view.UiView

class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "HelloArActivity"
    }

    lateinit var arCoreSessionHelper: ARCoreSessionLifecycleHelper
    lateinit var view: ArView
    lateinit var renderer: ArRenderer

    val instantPlacementSettings =
        InstantPlacementSettings()
    val depthSettings =
        DepthSettings()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainView()
        }
        // Setup ARCore session lifecycle helper and configuration.
        arCoreSessionHelper = ARCoreSessionLifecycleHelper(this)
        // If Session creation or Session.resume() fails, display a message and log detailed
        // information.
        arCoreSessionHelper.exceptionCallback =
            { exception ->
                val message =
                    when (exception) {
                        is UnavailableUserDeclinedInstallationException ->
                            "Google Play Services for ARをインストールしてください"
                        is UnavailableApkTooOldException -> "Please update ARCore"
                        is UnavailableSdkTooOldException -> "Please update this app"
                        is UnavailableDeviceNotCompatibleException -> "このデバイスはARに対応していません"
                        is CameraNotAvailableException -> "カメラが動作していません.アプリを再起動してください"
                        else -> "Failed to create AR session: $exception"
                    }
                Log.e(TAG, "ARCore threw an exception", exception)
            }

        // Configure session features, including: Lighting Estimation, Depth mode, Instant Placement.
        arCoreSessionHelper.beforeSessionResume = ::configureSession
        lifecycle.addObserver(arCoreSessionHelper)

        // Set up the Hello AR renderer.
        renderer = ArRenderer(this)
        lifecycle.addObserver(renderer)

        // Set up Hello AR UI.
        view = ArView(this)
        lifecycle.addObserver(view)
        //setContentView(view.root)

        // Sets up an example renderer using our HelloARRenderer.
        SampleRender(
            view.surfaceView,
            renderer,
            assets
        )

        depthSettings.onCreate(this)
        instantPlacementSettings.onCreate(this)
    }
    val ui = UiView()

    // Configure the session, using Lighting Estimation, and Depth mode.
    fun configureSession(session: Session) {
        session.configure(
            session.config.apply {
                lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
                cloudAnchorMode = Config.CloudAnchorMode.ENABLED

                // Depth API is used if it is configured in Hello AR's settings.
                depthMode =
                    if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                        Config.DepthMode.AUTOMATIC
                    } else {
                        Config.DepthMode.DISABLED
                    }

                // Instant Placement is used if it is configured in Hello AR's settings.
                instantPlacementMode =
                    if (instantPlacementSettings.isInstantPlacementEnabled) {
                        Config.InstantPlacementMode.LOCAL_Y_UP
                    } else {
                        Config.InstantPlacementMode.DISABLED
                    }
            }
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        results: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, results)
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            // Use toast instead of snackbar here since the activity will exit.
            Toast.makeText(this, "このアプリはカメラ権限を必要とします", Toast.LENGTH_LONG)
                .show()
            if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                // Permission denied with checking "Do not ask again".
                CameraPermissionHelper.launchPermissionSettings(this)
            }
            finish()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        FullScreenHelper.setFullScreenOnWindowFocusChanged(this, hasFocus)
    }
    @Composable
    fun OpenGLView() {
        AndroidView(factory = {
            view.surfaceView
        })
    }

    @Composable
    fun MainView() = XRStudyWatchAppTheme {
        OpenGLView()
        ui.Menulayout()
        ui.Buttonlayout()
    }
    @Preview(showBackground = true)
    @Composable
    fun MainPreview() {
        MainView()
    }
    @Composable
    @Preview
    fun UiPreview(){
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
        ){

        }
        ui.Menulayout()
        ui.Buttonlayout()
    }

}


