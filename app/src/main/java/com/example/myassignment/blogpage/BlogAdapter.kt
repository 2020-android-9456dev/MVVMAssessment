package com.example.myassignment.blogpage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myassignment.R
import com.example.myassignment.blogpage.BlogsViewType.VIEW_TYPE_LOADING
import com.example.myassignment.rest.model.blog_model.BlogResModel
import com.example.myassignment.rest.model.blog_model.BlogResModelItem
import kotlinx.android.synthetic.main.blog_item_layout.view.*

interface BlogAdapterListener {
    fun onClickBlog(item : BlogResModelItem)
}

class BlogAdapter(val context: Context, val listener: BlogAdapterListener): RecyclerView.Adapter<BlogsViewHolder>() {

    var items: MutableList<Any> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogsViewHolder {
        return when (viewType) {
            BlogsViewType.VIEW_TYPE_BLOGS.ordinal -> {
                BlogsViewHolder.BlogViewHolder.create(parent)
            }
            else -> {
                BlogsViewHolder.LoadingViewHolder.create(parent)
            }
        }
    }

    override fun getItemCount(): Int {
        return this.items.size
    }

    override fun onBindViewHolder(holder: BlogsViewHolder, position: Int) {
        val item = items[position]
        when (getItemViewType(position)) {
            BlogsViewType.VIEW_TYPE_BLOGS.ordinal -> {
                (holder as BlogsViewHolder.BlogViewHolder).bindView(item as BlogResModelItem)
            }
            VIEW_TYPE_LOADING.ordinal -> {
                (holder as BlogsViewHolder.LoadingViewHolder).bindView(item)
            }
        }

        /**
         * onClickListener : click event of statement list item
         */
        holder.itemView.setOnClickListener {
            if (item is BlogResModelItem) {
                listener.onClickBlog(item)
            }
        }
    }

    /**
     * getItemViewType : It define view type as per item position
     * @param position
     */
    override fun getItemViewType(position: Int): Int {
        if (items[position] is BlogResModelItem) {
            return BlogsViewType.VIEW_TYPE_BLOGS.ordinal
        } else if (items[position] is BlogsViewType) {
            return (items[position] as BlogsViewType).ordinal
        }
        return super.getItemViewType(position)
    }

    /**
     * refresh : It refresh the list items when load more statements perform
     * @param items
     */
    fun refresh(data: List<Any>) {
        val newItems: MutableList<Any> = mutableListOf()

        newItems.addAll(data)

        val diffCallback = BlogsDiffCallback(this.items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
//        this.items.clear()
        this.items.addAll(newItems)
        println("SIZE : "+this.items.size)
        diffResult.dispatchUpdatesTo(this)
    }

    /**
     * addLoadingIndicator : It shows load more spinner in bottom of page
     */
    fun addLoadingIndicator() {
        // To ensure we only ever have one loading indicator in the list
        val index = items.indexOf(VIEW_TYPE_LOADING)
        if (index == -1) {
            items.add(VIEW_TYPE_LOADING)
            notifyItemInserted(items.indexOf(VIEW_TYPE_LOADING))
        }
    }

    /**
     * removeLoadingIndicator : It hide load more spinner in bottom of page
     */
    fun removeLoadingIndicator() {
        val index = items.indexOf(VIEW_TYPE_LOADING)
        if (index > -1) {
            items.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    class BlogsDiffCallback(
        private val oldList: List<Any>,
        private val newList: List<Any>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            if (oldItem is BlogResModelItem && newItem is BlogResModelItem) {
                return oldItem.id == newItem.id
            }
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }
    }
}

sealed class BlogsViewHolder(view: View) : RecyclerView.ViewHolder(view){

    class BlogViewHolder(view: View) : BlogsViewHolder(view) {
        fun bindView(item: BlogResModelItem) {

            itemView.tvUserName.text = item.user.get(0).name
            itemView.tvUserDesignation.text = item.user.get(0).designation
            itemView.tvMediaContent.text = item.content
            if(item.media.size>0) {
                itemView.tvMediaTitle.visibility = View.VISIBLE
                itemView.tvMediaURL.visibility = View.VISIBLE
                itemView.ivMediaAvatar.visibility = View.VISIBLE
                itemView.tvMediaTitle.text = item.media.get(0).title
                itemView.tvMediaURL.text = item.media.get(0).url
                itemView.ivMediaAvatar.load(item.media.get(0).image)
            } else {
                itemView.tvMediaTitle.visibility = View.GONE
                itemView.tvMediaURL.visibility = View.GONE
                itemView.ivMediaAvatar.visibility = View.GONE
            }

            itemView.tvLikes.text = coolFormat(item.likes, 0)+" Likes"

            itemView.tvComments.text = coolFormat(item.comments, 0)+" Comments"

            itemView.ivUserAvatar.load(item.user.get(0).avatar)
        }

        companion object {
            fun create(parent: ViewGroup): BlogsViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.blog_item_layout, parent, false)
                return BlogViewHolder(view)
            }
        }

        private fun coolFormat(n: Double, iteration: Int): String? {
            val c = charArrayOf('k', 'm', 'b', 't')
            val d = n.toLong() / 100 / 10.0
            val isRound =
                d * 10 % 10 == 0.0 //true if the decimal part is equal to 0 (then it's trimmed anyway)
            return if (d < 1000) //this determines the class, i.e. 'k', 'm' etc
                (if (d > 99.9 || isRound || !isRound && d > 9.99) //this decides whether to trim the decimals
                    d.toInt() * 10 / 10 else d.toString() + "" // (int) d * 10 / 10 drops the decimal
                        ).toString() + "" + c.get(iteration) else coolFormat(d, iteration + 1)
        }
    }

    class LoadingViewHolder(view: View) : BlogsViewHolder(view) {
        fun bindView(item: Any) {

        }

        companion object {
            fun create(parent: ViewGroup): LoadingViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_blogs_loading, parent, false)
                return LoadingViewHolder(view)
            }
        }
    }
}

private fun ImageView.load(avatar: String) {
    Glide.with(this)
        .load(avatar)
        .into(this)
}

enum class BlogsViewType() {
    VIEW_TYPE_BLOGS,
    VIEW_TYPE_LOADING,
}
