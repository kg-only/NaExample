package com.example.naexample.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.naexample.api.Api
import com.example.naexample.model.Photos

class PexelsDataSource(
    private val api: Api,
    private val query: String
) : PagingSource<Int, Photos>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photos> {
        return try {
            val currentPage = params.key ?: 1 // Starting page, if null, default to 1
            val response = api.searchPhotos(query, currentPage, params.loadSize)
            if (response.isSuccessful) {
                val pexelResponse = response.body()
                val photos = pexelResponse?.photos ?: emptyList()
                val prevPage = if (currentPage > 1) currentPage - 1 else null
                val nextPageNumber = if (photos.isNotEmpty()) currentPage + 1 else null
                LoadResult.Page(photos, prevPage, nextPageNumber)
            } else {
                LoadResult.Error(Exception("Failed to load data"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photos>): Int? {
        return null // This example doesn't support refreshing, so return null
    }
}