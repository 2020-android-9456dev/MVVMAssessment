package com.example.myassignment.base

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.myassignment.commonview.CustomProgressBar
import com.example.myassignment.interfaces.FetchDataViewModelInterface
import com.example.myassignment.presentables.ErrorPresentable
import com.example.myassignment.presentables.LoadingViewPresentable
import com.example.myassignment.presentables.LoadingViewStatus
import com.example.myassignment.utils.CustomError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class BaseCoroutineFragment<T : FetchDataViewModelInterface> : CorountineScopedFragment(),
    LoadingViewPresentable, ErrorPresentable {

    override lateinit var progressBar: CustomProgressBar
    abstract val viewModel: T

    // menu
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = CustomProgressBar(activity as Activity)
        setHasOptionsMenu(true)
        bindDefaultUI()
    }

    /**
     * Adds a X button
     *
     * @param menu
     * @param inflater
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

    }

    /**
     * Handling option items
     * home button ("<-" icon) triggers the activity's back button, this allows to animate when exiting a fragment
     *
     * @param item
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                activity?.onBackPressed()
        }

        return false
    }



    open fun onDisplayError(error: CustomError) {
        error.showAlertDialog(this@BaseCoroutineFragment.requireContext())
    }

    /**
     * Default UI binding with loading view and display error
     *
     */
    private fun bindDefaultUI() {
        launch {
            viewModel.let { viewModel ->
                viewModel.isLoading.observe(viewLifecycleOwner, Observer<Boolean> {
                    val status = if (it) LoadingViewStatus.VISIBLE else LoadingViewStatus.HIDDEN
                    setLoadingViewStatus(status)
                })

                viewModel.displayError.observe(viewLifecycleOwner, Observer<CustomError> { aError ->
                    this@BaseCoroutineFragment.context?.let {
                        onDisplayError(aError)
                    }
                })
            }
        }
    }
}

abstract class CorountineScopedFragment : Fragment(), CoroutineScope {
    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}