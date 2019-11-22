package com.elyeproj.clientandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity() {

    object Model {
        data class Result(val query: Query)
        data class Query(val searchinfo: SearchInfo)
        data class SearchInfo(val totalhits: Int)
    }

    private var disposable = Disposables.disposed()
    private val httpClient = OkHttpClient.Builder().build()
    private val httpUrlBuilder = HttpUrl.Builder()
        .scheme("https")
        .host("en.wikipedia.org")
        .addPathSegment("w")
        .addPathSegment("api.php")
        .addQueryParameter("action", "query")
        .addQueryParameter("format", "json")
        .addQueryParameter("list", "search")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun beginSearch(view: View) {
        if (searchText.text.isNotBlank()) {
            performFetch(searchText.text.toString())
        }
    }

    private fun performFetch(searchText: String) {
        resultText.text = ""
        progressIndicator.visibility = View.VISIBLE

        disposable.dispose()
        disposable = Single.just(searchText)
            .map{fetchOnBackground(it)}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { showResult("Count is $it") },
                { showResult(it.localizedMessage) })
    }

    private fun showResult(result: String) {
        progressIndicator.visibility = View.GONE
        resultText.text = result
    }

    private fun fetchOnBackground(searchText: String): String {
        val httpUrl = httpUrlBuilder.addQueryParameter("srsearch", searchText)
            .build()
        val request = Request.Builder().get().url(httpUrl).build()
        val response = httpClient.newCall(request).execute()

        return if (response.isSuccessful) {
            val raw = response.body?.string()
            try {
                val result = Gson().fromJson(raw, Model.Result::class.java)
                result.query.searchinfo.totalhits.toString()
            } catch (exception: JsonSyntaxException) {
                "No data found "
            }
        } else {
            response.message
        }
    }

    override fun onPause() {
        disposable.dispose()
        super.onPause()
    }
}
