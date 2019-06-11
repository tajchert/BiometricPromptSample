package pl.tajchert.biometricpromt.sample

import android.content.DialogInterface
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonShowPrompt.setOnClickListener {
            showPrompt()
        }
        BiometricPromptCompat.setOnClickListener {
            showPromptCompat()
        }
    }

    private fun showPrompt() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val cancelListener = DialogInterface.OnClickListener { _, _ -> Log.d("Test", "Cancel Listener") }
            val biometricPrompt = BiometricPrompt.Builder(MainActivity@ this)
                .setTitle("Title")
                .setSubtitle("Subtitle")
                .setDescription("Description")
                .setNegativeButton("Cancel", MainActivity@ this.mainExecutor, cancelListener)
                .build()
            biometricPrompt.authenticate(
                CancellationSignal(),
                MainActivity@ this.mainExecutor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                        super.onAuthenticationError(errorCode, errString)
                        runOnUiThread {
                            Toast.makeText(applicationContext, errString, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                        super.onAuthenticationSucceeded(result)
                        runOnUiThread {
                            Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        runOnUiThread {
                            Toast.makeText(applicationContext, "Fail", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        } else {
            Toast.makeText(MainActivity@ this, "Need Android Pie, API >= 28", Toast.LENGTH_SHORT).show()
        }

    }

    private fun showPromptCompat() {
        val biometricPrompt = androidx.biometric.BiometricPrompt(
            MainActivity@ this,
            Executors.newSingleThreadExecutor(),
            object : androidx.biometric.BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    runOnUiThread {
                        Toast.makeText(baseContext, errString, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    runOnUiThread {
                        Toast.makeText(baseContext, "Success", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    runOnUiThread {
                        Toast.makeText(baseContext, "Fail", Toast.LENGTH_SHORT).show()
                    }
                }
            })

        val promptInfo = androidx.biometric.BiometricPrompt.PromptInfo.Builder()
            .setTitle("Title")
            .setSubtitle("Subtitle")
            .setDescription("Description")
            .setNegativeButtonText("Negative Button")
            .build()
        biometricPrompt.authenticate(promptInfo)
    }
}
