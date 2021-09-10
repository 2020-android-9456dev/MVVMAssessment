package com.example.myassignment.blogpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myassignment.R
import com.example.myassignment.base.BaseCoroutineFragment
import com.example.myassignment.commonview.EndlessRecyclerViewScrollListener
import com.example.myassignment.network.BlogsListNetworkDataSource
import com.example.myassignment.presentables.LoadingViewStatus
import com.example.myassignment.rest.model.blog_model.BlogResModel
import com.example.myassignment.rest.model.blog_model.BlogResModelItem
import com.example.myassignment.rest.network.ApiService
import com.example.myassignment.rest.repository.BlogsListRepository
import kotlinx.android.synthetic.main.fragment_blogs.*

class BlogFragment: BaseCoroutineFragment<BlogViewModel>(), BlogAdapterListener {

    override lateinit var viewModel: BlogViewModel
    lateinit var adapter: BlogAdapter
    private var scrollListener: EndlessRecyclerViewScrollListener? = null

    /* Repo that contains all the statements for selected account */
    private val blogsRepository by lazy {
        val dataSource = BlogsListNetworkDataSource(context!!, ApiService())
        BlogsListRepository(dataSource)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        /* Inflate the layout for this fragment */
        return inflater.inflate(R.layout.fragment_blogs, container, false)
    }

    /**
     * onViewCreated: onViewCreated Fragment
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        /* setup view model */
        setupViewModel()

        /* setup UI */
        setupUI()
    }

    private fun setupUI() {
        /* Setup layout manager and recycler */
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                val isLoading = (viewModel.isLoadingInitial.value ?: false)
                        || (viewModel.isLoadingMore.value ?: false)
                if (!isLoading) {
                    viewModel.fetchMoreBlogData()
                }
            }
        }

        /* Add listener */
        recyclerView.addOnScrollListener(scrollListener!!)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(requireActivity(), BlogViewModelFactory(requireContext(), blogsRepository)).get(BlogViewModel::class.java)
        adapter = BlogAdapter(context!!, this)
        recyclerView.adapter = adapter

        viewModel.fetchBlogData()

        viewModel.blogListData.observe(viewLifecycleOwner, Observer<BlogResModel> {
//            adapter.notifyDataSetChanged()
            println("********** FIRST CALLED ")
            adapter.refresh(it)
        })

        viewModel.blogListMoreData.observe(viewLifecycleOwner, Observer<BlogResModel> {
            println("********** Second CALLED ")
            adapter.refresh(it)
        })

        /* Observe initial loading statements */
        viewModel.isLoadingInitial.observe(viewLifecycleOwner, Observer { isLoading ->
            val status = if (isLoading) LoadingViewStatus.VISIBLE else LoadingViewStatus.HIDDEN
            setLoadingViewStatus(status)
        })

        /* Observe loading more statements */
        viewModel.isLoadingMore.observe(viewLifecycleOwner, Observer { isLoadingMore ->
            if (isLoadingMore) {
                showFooterProgressView()
            } else {
                hideFooterProgressView()
            }
        })
    }

    private fun showFooterProgressView() {
        (recyclerView.adapter as? BlogAdapter)?.addLoadingIndicator()
    }

    private fun hideFooterProgressView() {
        (recyclerView.adapter as? BlogAdapter)?.removeLoadingIndicator()
    }

    override fun onClickBlog(item: BlogResModelItem) {

    }

}