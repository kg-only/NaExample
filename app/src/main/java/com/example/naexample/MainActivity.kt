package com.example.naexample

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.naexample.adapter.PhotoAdapter
import com.example.naexample.databinding.ActivityMainBinding
import com.example.naexample.databinding.PreviewDialogBinding
import com.example.naexample.viewmodel.MainViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val vm by viewModels<MainViewModel>()
    private val adapter by lazy { PhotoAdapter() }
    private var lastSavedQuery = ""

    companion object {
        const val FIRST_REQUEST = "city"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchPhoto(FIRST_REQUEST)
        initUI()
        initPhotoRecycler()
    }

    private fun initUI() {
        with(binding) {
            swipeRefreshLayout.setOnRefreshListener { fetchPhoto(lastSavedQuery) }
            search.setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) fetchPhoto(v.text.toString())
                false
            }

            adapter.addLoadStateListener { loadStates -> handleLoadStates(loadStates) }
            adapter.setOnClickListener { photoPreview(it.src?.large!!) }
        }
    }

    private fun photoPreview(photoUrl: String) {
        val dialog = Dialog(this@MainActivity)
        val binding = PreviewDialogBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        val window = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        @Suppress("DEPRECATION")
        window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        Picasso.get().load(photoUrl).into(binding.imageView)
        binding.root.setPadding(16, 16, 16, 16)
        binding.close.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun fetchPhoto(text: String?) {
        if (!text.isNullOrEmpty()) {
            binding.swipeRefreshLayout.isRefreshing = true
            lastSavedQuery = text
            vm.fetchPhotos(text)
        } else {
            binding.swipeRefreshLayout.isRefreshing = false
            toast(R.string.input_text)
        }
    }

    private fun initPhotoRecycler() {
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager =
            GridLayoutManager(this@MainActivity, 2, GridLayoutManager.VERTICAL, false)

        lifecycleScope.launch {
            vm.photosFlow.collectLatest { pagingData ->
                binding.swipeRefreshLayout.isRefreshing = false
                adapter.submitData(pagingData)
            }
        }
    }

    private fun handleLoadStates(loadStates: CombinedLoadStates) {
        val refreshState = loadStates.refresh
        val appendState = loadStates.append

        if (refreshState is LoadState.Error) {
            binding.swipeRefreshLayout.isRefreshing = false
            toast(R.string.load_error)
        }
        if (appendState is LoadState.Error) {
            binding.swipeRefreshLayout.isRefreshing = false
            toast(R.string.load_more_error)
        }
    }

    private fun toast(@StringRes text: Int) =
        Toast.makeText(this, getString(text), Toast.LENGTH_SHORT).show()

}