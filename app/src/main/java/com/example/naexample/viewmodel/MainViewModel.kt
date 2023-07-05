package com.example.naexample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.naexample.api.Api
import com.example.naexample.model.Photos
import com.example.naexample.paging.PexelsDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val api: Api) : ViewModel() {
    private val _photosFlow: MutableStateFlow<PagingData<Photos>> =
        MutableStateFlow(PagingData.empty())
    val photosFlow: Flow<PagingData<Photos>> = _photosFlow.asStateFlow()

    fun fetchPhotos(query: String) {
        viewModelScope.launch {
            val pagingConfig = PagingConfig(pageSize = 10, enablePlaceholders = false)
            val photosPagingSource = PexelsDataSource(api, query)
            val photosFlow =
                Pager(pagingConfig) { photosPagingSource }.flow.cachedIn(viewModelScope)
            _photosFlow.emitAll(photosFlow)
        }
    }
}