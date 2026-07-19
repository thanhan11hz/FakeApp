package com.example.fakeapp

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.graphics.Rect

class LoggerAccessibilityService : AccessibilityService() {
    companion object {
        private const val TAG = "AccessibilityLogger"
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        Log.d(TAG, "======================")
        Log.d(TAG, "Package: ${event.packageName}")
        Log.d(TAG, "Class: ${event.className}")
        Log.d(TAG, "Type: ${AccessibilityEvent.eventTypeToString(event.eventType)}")

        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {

            val root = rootInActiveWindow

            Log.d(TAG, "========= UI TREE =========")

            dumpNode(root)
        }
    }

    override fun onInterrupt() {
        Log.d(TAG, "Interrupt")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "Accessibility Service Connected")
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service Created")
    }

    private fun dumpNode(node: AccessibilityNodeInfo?, depth: Int = 0) {
        if (node == null) return

        val indent = "  ".repeat(depth)

        val rect = Rect()
        node.getBoundsInScreen(rect)

        Log.d(
            TAG,
            indent + nodeSummary(node)
        )

        for (i in 0 until node.childCount) {
            dumpNode(node.getChild(i), depth + 1)
        }
    }

    private fun nodeSummary(node: AccessibilityNodeInfo): String {
        return buildString {
            append(node.className)

            if (!node.text.isNullOrBlank())
                append(" | text='${node.text}'")

            if (!node.contentDescription.isNullOrBlank())
                append(" | desc='${node.contentDescription}'")

            if (!node.viewIdResourceName.isNullOrBlank())
                append(" | id='${node.viewIdResourceName}'")

            if (node.isClickable)
                append(" | clickable")

            if (node.isEditable)
                append(" | editable")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service Destroyed")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "Service Unbound")
        return super.onUnbind(intent)
    }
}