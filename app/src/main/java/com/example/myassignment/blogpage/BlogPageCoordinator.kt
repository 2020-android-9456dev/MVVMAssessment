package com.example.myassignment.blogpage

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.myassignment.interfaces.Coordinator

class BlogPageCoordinator(private val context: Context) : Coordinator {
    override fun start() {
        val intent = Intent(context, BlogPageActivity::class.java)
        (context as AppCompatActivity).startActivityForResult(intent, 0)
    }
}