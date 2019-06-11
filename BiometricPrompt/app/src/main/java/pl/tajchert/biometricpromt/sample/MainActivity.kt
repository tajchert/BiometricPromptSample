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

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonShowPrompt.setOnClickListener {
            showPrompt()
        }
    }

    fun showPrompt() {
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
                        Toast.makeText(applicationContext, errString, Toast.LENGTH_SHORT).show()
                        super.onAuthenticationError(errorCode, errString)
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                        Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
                        super.onAuthenticationSucceeded(result)
                    }

                    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
                        super.onAuthenticationHelp(helpCode, helpString)
                    }

                    override fun onAuthenticationFailed() {
                        Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
                        super.onAuthenticationFailed()
                    }
                })
        } else {
            TODO("VERSION.SDK_INT < P")
        }

    }


}
