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

    private var overlayShowing = false

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        if (event == null) return

        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED)
            return

        val pkg = event.packageName?.toString() ?: return

        Log.d("ACCESSIBILITY", pkg)

        // Bỏ qua event của chính app
        if (pkg == packageName) return

        if (pkg == "com.android.chrome") {
            showOverlay()
        } else {
            hideOverlay()
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        overlayView = LayoutInflater.from(this)
            .inflate(R.layout.overlay_layout, null)

        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.CENTER
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