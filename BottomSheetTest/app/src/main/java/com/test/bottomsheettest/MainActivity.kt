package com.test.bottomsheettest

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior


class MainActivity : AppCompatActivity() {
    val bottomSheet: LinearLayout by lazy{findViewById<LinearLayout>(R.id.bottom_sheet)}
    val webView: WebView by lazy{findViewById<WebView>(R.id.webview)}
    lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView.webViewClient = WebViewClient()
        webView.loadUrl("https://www.google.com")

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
}
