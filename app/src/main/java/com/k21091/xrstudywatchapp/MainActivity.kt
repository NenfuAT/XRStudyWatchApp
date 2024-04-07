package com.k21091.xrstudywatchapp

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
import com.k21091.xrstudywatchapp.ar.kotlin.ObjectRenderer
import com.k21091.xrstudywatchapp.ui.theme.XRStudyWatchAppTheme
import com.k21091.xrstudywatchapp.util.imageFileName
import com.k21091.xrstudywatchapp.util.imageFilePath
import com.k21091.xrstudywatchapp.view.LoginView
import com.k21091.xrstudywatchapp.view.UiView
import com.k21091.xrstudywatchapp.view.getPathFromUri
import com.k21091.xrstudywatchapp.view.selectedImageBitmapState
import pub.devrel.easypermissions.EasyPermissions
import java.io.IOException

class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "HelloArActivity"
    }

    lateinit var arCoreSessionHelper: ARCoreSessionLifecycleHelper
    lateinit var view: ArView
    lateinit var renderer: ArRenderer
    //lateinit var renderer: ObjectRenderer

    //private var selectedImageBitmapState = mutableStateOf<ImageBitmap?>(null)
    lateinit var getContent: ActivityResultLauncher<String>

    val instantPlacementSettings =
        InstantPlacementSettings()
    val depthSettings =
        DepthSettings()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permissions = arrayOf(
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.BLUETOOTH_ADMIN ,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_ADVERTISE,
            Manifest.permission.NEARBY_WIFI_DEVICES,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission. WRITE_EXTERNAL_STORAGE,
            Manifest.permission. READ_EXTERNAL_STORAGE

        )

        //許可したいpermissionを許可できるように
        if (!EasyPermissions.hasPermissions(this, *permissions)) {
            EasyPermissions.requestPermissions(this, "パーミッションに関する説明", 1, *permissions)
        }
        // getContent を初期化する
        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageFilePath.value= getPathFromUri(this, uri!!).toString()
            imageFileName.value= imageFilePath.value.substring(imageFilePath.value.lastIndexOf("/") + 1)
            Log.d("MainActivity", "Selected image bitmap: $selectedImageBitmapState")
            uri?.let { selectedImageBitmapState.value = uriToBitmap(it,this) }
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
        //renderer = ObjectRenderer(this)
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

        // setContent の後に配置する
        setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                AndroidView(factory = {
                    view.surfaceView
                })
                // NavHostを含むRootコンポーザブル関数を呼び出す
                Root()
            }
        }
    }

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

    fun uriToBitmap(uri: Uri, activity: ComponentActivity): ImageBitmap? {
        return try {
            val inputStream = activity.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            bitmap?.asImageBitmap()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }


    enum class Screens {
        LOGIN,
        MAIN,
    }
    @Composable
    fun Root(modifier: Modifier = Modifier) {
        // Create NavController
        val navController = rememberNavController()
        // Create NavHost
        NavHost(
            navController = navController,
            startDestination =Screens.LOGIN.name
        ) {
            composable(Screens.LOGIN.name) {
                LoginView(
                    toMain= {navController.navigate(Screens.MAIN.name)},
                    modifier = modifier
                )
            }

            composable(Screens.MAIN.name) {
                MainView()
            }

        }
    }
    @Composable
    fun OpenGLView() {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(factory = {
                view.surfaceView
            })
        }
    }




    @Composable
    fun MainView() = XRStudyWatchAppTheme {
        val ui=UiView(this,getContent)
        ui.Buttonlayout()
        ui.Menulayout()
    }

    @Preview(showBackground = true)
    @Composable
    fun MainPreview() {
        MainView()
    }
}
