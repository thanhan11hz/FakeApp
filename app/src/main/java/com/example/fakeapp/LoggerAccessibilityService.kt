package com.example.fakeapp

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent

import android.graphics.PixelFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.fakeapp.databinding.OverlayLayoutBinding
import android.os.Handler
import android.os.Looper

//
//class LoggerAccessibilityService : AccessibilityService() {
//    companion object {
//        private const val TAG = "AccessibilityLogger"
//    }
//
//    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
//        if (event == null) return
//
//        Log.d(TAG, "======================")
//        Log.d(TAG, "Package: ${event.packageName}")
//        Log.d(TAG, "Class: ${event.className}")
//        Log.d(TAG, "Type: ${AccessibilityEvent.eventTypeToString(event.eventType)}")
//
//        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
//
//            val root = rootInActiveWindow
//
//            Log.d(TAG, "========= UI TREE =========")
//
//            dumpNode(root)
//        }
//    }
//
//    override fun onInterrupt() {
//        Log.d(TAG, "Interrupt")
//    }
//
//    override fun onServiceConnected() {
//        super.onServiceConnected()
//        Log.d(TAG, "Accessibility Service Connected")
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        Log.d(TAG, "Service Created")
//    }
//
//    private fun dumpNode(node: AccessibilityNodeInfo?, depth: Int = 0) {
//        if (node == null) return
//
//        val indent = "  ".repeat(depth)
//
//        val rect = Rect()
//        node.getBoundsInScreen(rect)
//
//        Log.d(
//            TAG,
//            indent + nodeSummary(node)
//        )
//
//        for (i in 0 until node.childCount) {
//            dumpNode(node.getChild(i), depth + 1)
//        }
//    }
//
//    private fun nodeSummary(node: AccessibilityNodeInfo): String {
//        return buildString {
//            append(node.className)
//
//            if (!node.text.isNullOrBlank())
//                append(" | text='${node.text}'")
//
//            if (!node.contentDescription.isNullOrBlank())
//                append(" | desc='${node.contentDescription}'")
//
//            if (!node.viewIdResourceName.isNullOrBlank())
//                append(" | id='${node.viewIdResourceName}'")
//
//            if (node.isClickable)
//                append(" | clickable")
//
//            if (node.isEditable)
//                append(" | editable")
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        Log.d(TAG, "Service Destroyed")
//    }
//
//    override fun onUnbind(intent: Intent?): Boolean {
//        Log.d(TAG, "Service Unbound")
//        return super.onUnbind(intent)
//    }
//}

class LoggerAccessibilityService : AccessibilityService() {

    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: View
    private lateinit var params: WindowManager.LayoutParams

    private var screenHeight = 0
    private var overlayShowing = false

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        if (event == null) return

        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED)
            return

        val pkg = event.packageName?.toString() ?: return

        Log.d("ACCESSIBILITY", pkg)

        // Bỏ qua event của chính app
        if (pkg == packageName) return

        if (pkg == "com.example.cs426_seminar_app") {
            showOverlay()
        } else {
            hideOverlay()
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        screenHeight = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            windowManager.currentWindowMetrics.bounds.height()
        } else {
            @Suppress("DEPRECATION")
            val metrics = android.util.DisplayMetrics()
            windowManager.defaultDisplay.getRealMetrics(metrics)
            metrics.heightPixels
        }

        val binding = OverlayLayoutBinding.inflate(LayoutInflater.from(this))
        overlayView = binding.root

        binding.btnLogin.setOnClickListener {
            Log.d("LOGIN", "LOGIN BUTTON")
            // fake warning
            binding.tvError.visibility = View.VISIBLE

            // hide overlay after 2 secs
            Handler(Looper.getMainLooper()).postDelayed({
                hideOverlay()
                binding.tvError.visibility = View.INVISIBLE
            }, 2000)
        }

        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.TOP or Gravity.START
    }

    private fun showOverlay() {

        if (overlayShowing) return

        windowManager.addView(overlayView, params)

        overlayShowing = true

        Log.d("Overlay", "SHOW")
    }

    private fun hideOverlay() {
        if (!overlayShowing) return
        windowManager.removeView(overlayView)
        overlayShowing = false
        Log.d("Overlay", "HIDE")
    }

    override fun onDestroy() {
        if (overlayShowing) {
            windowManager.removeView(overlayView)
        }
        super.onDestroy()
    }
    override fun onInterrupt() {

    }
}


