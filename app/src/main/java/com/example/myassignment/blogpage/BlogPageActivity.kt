package com.example.myassignment.blogpage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myassignment.R
import kotlinx.android.synthetic.main.activity_blogs.*

class BlogPageActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blogs)
        setSupportActionBar(toolbar)
    }

}