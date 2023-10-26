package com.test.bottomsheettest

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class SecondActivity : AppCompatActivity() {
    val webView: WebView by lazy{findViewById<WebView>(R.id.second_web_view)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        overridePendingTransition(R.anim.slide_left_enter, R.anim.none)

        webView.webViewClient = WebViewClient()
        webView.loadUrl(" https://www.mdpert.net/mobile_M_project/member_personal_checkindetail.aspx?app_swipe=1&fmcodeonekey=K2304191007274JE&fsidx=23&fpidx=26")

        webView.setOnTouchListener { v, event -> event.action == MotionEvent.ACTION_MOVE }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.none, R.anim.slide_right_exit)
    }

}
