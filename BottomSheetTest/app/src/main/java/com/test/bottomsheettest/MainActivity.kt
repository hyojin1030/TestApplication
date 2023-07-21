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


class MainActivity : AppCompatActivity() {
    val bottomSheet: ConstraintLayout by lazy{findViewById<ConstraintLayout>(R.id.bottom_sheet)}
    val webView: WebView by lazy{findViewById<WebView>(R.id.web_view)}
    val mapViewContainer: ViewGroup by lazy{findViewById<ViewGroup>(R.id.map_view)}
    lateinit var sheetBehavior: BottomSheetBehavior<ConstraintLayout>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getHashKey()

        var mapView: MapView = MapView(this)
        mapViewContainer.addView(mapView)

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633), true);

        webView.webViewClient = WebViewClient()
        webView.loadUrl("https://www.mdpert.net/mobile_M_project/member_home.aspx?app_swipe=1")

        webView.setOnTouchListener { v, event -> event.action == MotionEvent.ACTION_MOVE }

        //bottom sheet의 behavior를 .from 메소드를 이용해 받아온다.
        sheetBehavior= BottomSheetBehavior.from(bottomSheet)
        sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                //슬라이드 될 때

                sheetBehavior.calculateSlideOffset()
            }
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState) {
                    // 접혀있는 상태
                    BottomSheetBehavior.STATE_COLLAPSED-> {
                        Log.d("TAG", "state collapsed")
                    }

                    // 드래그 중
                    BottomSheetBehavior.STATE_DRAGGING-> {
                        Log.d("TAG", "state dragging")
                    }

                    // 완전히 펼쳐진 상태
                    BottomSheetBehavior.STATE_EXPANDED-> {
                        Log.d("TAG", "state expanded")
                    }

                    // 반만 펼쳐진 상태
                    BottomSheetBehavior.STATE_HALF_EXPANDED-> {
                        Log.d("TAG", "state half expanded")
                    }

                    // 숨겨진 상태
                    BottomSheetBehavior.STATE_HIDDEN-> {
                        Log.d("TAG", "state hidden")
                    }

                    // 드래그 후 고정
                    BottomSheetBehavior.STATE_SETTLING-> {
                        Log.d("TAG", "state setting")
                    }
                }
            }
        })

    }

    fun onClickButton(view: View) {
        Toast.makeText(applicationContext, "onClickButton", Toast.LENGTH_SHORT).show()
    }

    private fun getHashKey() {
        var packageInfo: PackageInfo? = null
        try{
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (packageInfo == null) {
            Log.d("hashKey", "null")
        }
        packageInfo?.signatures?.forEach {
            try {
                val md = MessageDigest.getInstance("SHA")
                md.update(it.toByteArray())
                Log.d("hashKey", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
                Log.e("KeyHash", "Unable to get MessageDigest. signature=$it", e)
            }
        }
    }
}
