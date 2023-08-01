package com.baljeet.youdotoo

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.baljeet.youdotoo.common.OnAttemptLoginViaGoogle
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.openAppSettings
import com.baljeet.youdotoo.data.dto.SignInResult
import com.baljeet.youdotoo.data.remote.GoogleAuthClient
import com.baljeet.youdotoo.permissions.dialogs.NotificationPermissionTextProvider
import com.baljeet.youdotoo.permissions.dialogs.PermissionDialog
import com.baljeet.youdotoo.presentation.ui.login.SignInState
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooDarkBlue
import com.baljeet.youdotoo.presentation.ui.theme.YouDoTooTheme
import com.baljeet.youdotoo.services.AllBackgroundSnaps
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity(), OnAttemptLoginViaGoogle {

    private var loginState = mutableStateOf(SignInState())


    private val googleAuthClient by lazy {
        GoogleAuthClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private var launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPref.init(applicationContext)
        installSplashScreen()
        setContent {

            val viewModel: MainViewModel = hiltViewModel()
            val dialogQueue = viewModel.visiblePermissionDialogQueue

            val notificationPermissionResultLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isGranted ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        viewModel.onPermissionResult(
                            permission = Manifest.permission.POST_NOTIFICATIONS,
                            isGranted = isGranted
                        )
                    }
                }
            )

            val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = { permissions ->
                    permissions.keys.forEach { permission ->
                        viewModel.onPermissionResult(
                            permission = permission,
                            isGranted = permissions[permission] == true
                        )
                    }
                }
            )

            LaunchedEffect(Unit) {
                delay(200)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationPermissionResultLauncher.launch(
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                }
            }

            dialogQueue.reversed().forEach { permission ->
                PermissionDialog(
                    permissionTextProvider = when (permission) {
                        Manifest.permission.POST_NOTIFICATIONS -> NotificationPermissionTextProvider()
                        else -> return@forEach
                    },
                    isPermanentlyDeclined = !shouldShowRequestPermissionRationale(permission),
                    onDismiss = viewModel::dismissDialog,
                    onClickOk = {
                        viewModel.dismissDialog()
                        multiplePermissionResultLauncher.launch(
                            arrayOf(permission)
                        )
                    },
                    onGoToAppSettingsClick = {
                        viewModel.dismissDialog()
                        this.openAppSettings()
                    },
                    modifier = Modifier
                )
            }

            YouDoTooTheme() {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = if (isSystemInDarkTheme()) {
                        NightDotooDarkBlue
                    } else {
                        Color.White
                    }
                ) {
                    val navController = rememberNavController()

                    launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartIntentSenderForResult(),
                        onResult = { result ->
                            if (result.resultCode == RESULT_OK) {
                                lifecycleScope.launch {
                                    val signInResult = googleAuthClient.signInWithIntent(
                                        intent = result.data ?: return@launch
                                    )
                                    onSignInResult(signInResult)
                                }
                            }
                        }
                    )

                    val state by loginState

                    NavGraph(
                        navController = navController,
                        onSignInAttempt = this@MainActivity,
                        signInState = state
                    )
                }
            }
        }
    }

    private fun onSignInResult(result: SignInResult) {
        result.errorMessage?.let {
            loginState.value = loginState.value.copy(
                signInError = result.errorMessage
            )
        }
        result.data?.let {
            loginState.value = loginState.value.copy(
                userData = it
            )
        }
    }

    override fun attemptLoginViaGoogle() {
        lifecycleScope.launch {
            val signInIntentSender = googleAuthClient.signIn()
            launcher?.launch(
                IntentSenderRequest.Builder(
                    signInIntentSender ?: return@launch
                ).build()
            )
        }
    }

    override fun resetLoginState() {
        loginState.value = SignInState()
    }

    override fun onStart() {
        super.onStart()

        if(SharedPref.isSyncOn.not()) {
            Intent(applicationContext, AllBackgroundSnaps::class.java).also {
                it.action = AllBackgroundSnaps.ServiceActions.START.toString()
                startService(it)
            }
        }
    }

}

