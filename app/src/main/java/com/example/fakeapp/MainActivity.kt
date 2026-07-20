//package com.example.fakeapp
//
//import android.content.Intent
//import android.graphics.Color
//import android.graphics.PixelFormat
//import android.net.Uri
//import android.os.Bundle
//import android.provider.Settings
//import android.view.Gravity
//import android.view.WindowManager
//import android.widget.TextView
//import android.util.Log
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import android.view.LayoutInflater
//import android.view.View
//
//class MainActivity : AppCompatActivity() {
//
//    companion object {
//        private const val TAG = "Overlay interface"
//    }
//
//    private lateinit var windowManager: WindowManager
//    private var overlayView: View? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//
//        if (!Settings.canDrawOverlays(this)) {
//
//            val intent = Intent(
//                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                Uri.parse("package:$packageName")
//            )
//
//            startActivity(intent)
//
//        } else {
//
//            // startOverlay()
//
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//
//        if (Settings.canDrawOverlays(this)) {
//            startOverlay()
//        }
//    }
//
//    private fun startOverlay() {
//        Log.d(TAG, "Overlayed App")
//        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
//
//        overlayView = LayoutInflater.from(this)
//            .inflate(R.layout.overlay_layout, null)
//
//        val params = WindowManager.LayoutParams(
//            WindowManager.LayoutParams.WRAP_CONTENT,
//            WindowManager.LayoutParams.WRAP_CONTENT,
//            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
//            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//            PixelFormat.TRANSLUCENT
//        )
//
//        params.gravity = Gravity.CENTER
//
//        windowManager.addView(overlayView, params)
//    }
//}

package com.example.fakeapp

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        checkPermissions()
    }

    override fun onResume() {
        super.onResume()

        if (isAccessibilityServiceEnabled() && Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "All permissions granted", Toast.LENGTH_SHORT).show()
        } else checkPermissions();
    }

    private fun checkPermissions() {

        if (!isAccessibilityServiceEnabled()) {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            return
        }

        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
            return
        }
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val am = getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager

        val enabledServices =
            am.getEnabledAccessibilityServiceList(
                AccessibilityServiceInfo.FEEDBACK_ALL_MASK
            )

        return enabledServices.any {
            it.resolveInfo.serviceInfo.packageName == packageName &&
                    it.resolveInfo.serviceInfo.name == LoggerAccessibilityService::class.java.name
        }
    }
}