package com.example.myassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myassignment.blogpage.BlogPageCoordinator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        GlobalScope.launch(context = Dispatchers.Main) {

            delay(FAKE_RESPONSE_TIME)

            BlogPageCoordinator(this@SplashActivity).start()
            finish()
        }
    }

    companion object {
        private const val FAKE_RESPONSE_TIME = 2000L
    }
}