package com.example.myassignment.rest.model.blog_model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class BlogResModel : ArrayList<BlogResModelItem>(), Parcelable

@Parcelize
data class BlogResModelItem(
    @SerializedName("comments")
    val comments: Double,
    @SerializedName("content")
    val content: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("likes")
    val likes: Double,
    @SerializedName("media")
    val media: List<Media>,
    @SerializedName("user")
    val user: List<User>
) : Parcelable

@Parcelize
data class Media(
    @SerializedName("blogId")
    val blogId: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String
) : Parcelable

@Parcelize
data class User(
    @SerializedName("about")
    val about: String,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("blogId")
    val blogId: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("designation")
    val designation: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("lastname")
    val lastname: String,
    @SerializedName("name")
    val name: String
) : Parcelable
