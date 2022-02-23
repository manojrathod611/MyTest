package com.example.mytest.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import com.example.mytest.adapters.TrendingAdapter
import com.example.mytest.R
import com.example.mytest.base.BaseActivity
import com.example.mytest.databinding.ActivityTrendingBinding
import com.example.mytest.models.TrendingModel
import com.example.mytest.network.Resource
import com.example.mytest.repository.TrendingRepository
import com.example.mytest.utils.Utils
import com.example.mytest.utils.viewBinding
import com.example.mytest.viewmodels.TrendingViewModel

class TrendingActivity :
    BaseActivity<TrendingViewModel, ActivityTrendingBinding, TrendingRepository>(),
    TrendingAdapter.Interaction {

    private val binding1 by viewBinding(ActivityTrendingBinding::inflate)
    private lateinit var trendingAdapter: TrendingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.trending_repos)

        setupRv()
        fetchData()
    }

    private fun setupRv() {
        trendingAdapter = TrendingAdapter(this, viewModel.selectedList)
        binding.recyclerView.apply {
            adapter = trendingAdapter
        }

        binding.refreshLayout.setOnRefreshListener {
            binding.lblInternet.visibility = View.GONE
            fetchData()
        }
    }

    private fun fetchData() {
        showLoaders()
        if (Utils.isOnline(this)) {
            observeResult()
            viewModel.getTrendingRepo()

        } else {
            hideLoaders()
            binding.lblInternet.visibility = View.VISIBLE
        }
    }

    private fun observeResult() {
        viewModel.getTrendingRepo.observe(this, {
            when (it) {
                is Resource.Success -> {
                    hideLoaders()
                    val trendingModel = it.value
                    if (trendingModel.items != null) {
                        trendingAdapter.setData(trendingModel.items)
                    }
                }
                is Resource.Failure -> {
                    hideLoaders()
                    Log.d("TAG", "Failed: " + it)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        val item = menu?.findItem(R.id.search_action)
        val searchView = item?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                trendingAdapter.filter.filter(newText)
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun showLoaders() {
        binding.shimmerFrameLayout.startShimmerAnimation()
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }

    private fun hideLoaders() {
        binding.shimmerFrameLayout.stopShimmerAnimation()
        binding.shimmerFrameLayout.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE

        if (binding.refreshLayout.isRefreshing) {
            binding.refreshLayout.isRefreshing = false
        }
    }

    override fun getViewModel() = TrendingViewModel::class.java
    override fun getActivityBinding() = binding1
    override fun getActivityRepository() = TrendingRepository(apiInterface)

    override fun onItemSelected(position: Int, item: TrendingModel.Item?) {
        item?.id?.let { viewModel.selectItem(it, item.isSelected) }
    }

    override fun onResume() {
        super.onResume()
        binding.shimmerFrameLayout.startShimmerAnimation()
    }

    override fun onPause() {
        binding.shimmerFrameLayout.stopShimmerAnimation()
        super.onPause()
    }

}