package com.elyeproj.clientandroid

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    object Model {
        data class Result(val data: Data)
        data class Data(val wikiCount: WikiCount)
        data class WikiCount(val keyword: String, val totalhits: Int)
    }

    companion object {
        val queryString = """
            query GetWikicountByKeyword(${"$"}keyword: String!){ 
                wikiCount(keyword: ${"$"}keyword) {
                    keyword 
                    totalhits
                }
            }
            """.trimIndent().replace("\n", "")
    }

    private var disposable = Disposables.disposed()
    private val httpClient = OkHttpClient.Builder().build()
    private val httpUrlBuilder = HttpUrl.Builder()
        .scheme("http")
        .host("EDIT_YOUR_HOST_HERE")
        .port(4000)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun beginSearch(view: View) {
        if (searchText.text.isNotBlank()) {
            performFetch(searchText.text.toString())
        }
    }

    fun beginApolloSearch(view: View) {
        if (searchText.text.isNotBlank()) {
            performFetchApollo(searchText.text.toString())
        }
    }

    private fun performFetchApollo(keyword: String) {
        resultText.text = ""
        progressIndicator.visibility = View.VISIBLE

        ApolloConnector.setupApollo().query(
            GetWikicountByKeywordQuery
                .builder()
                .keyword(keyword)
                .build()
        )
            .enqueue(object : ApolloCall.Callback<GetWikicountByKeywordQuery.Data>() {
                override fun onResponse(response: Response<GetWikicountByKeywordQuery.Data>) {
                    response.data()?.wikiCount?.run {
                        showApolloResult("${keyword()}:${totalhits()}")
                    } ?: showApolloResult("No Result")
                }

                override fun onFailure(e: ApolloException) {
                    showApolloResult(e.localizedMessage)
                }
            })
    }

    private fun showApolloResult(result: String) {
        runOnUiThread {
            showResult("Apollo: $result")
        }
    }

    private fun performFetch(searchText: String) {
        resultText.text = ""
        progressIndicator.visibility = View.VISIBLE

        disposable.dispose()
        disposable = Single.just(searchText)
            .map { fetchOnBackground(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { showResult(it) },
                { showResult(it.localizedMessage) })
    }

    private fun showResult(result: String) {
        progressIndicator.visibility = View.GONE
        resultText.text = result
    }

    private fun fetchOnBackground(searchText: String): String {
        val params = HashMap<String, String>()
        params["query"] = queryString
        params["variables"] = "{\"keyword\": \"${searchText}\"}"
        val body = JSONObject(params)
            .toString().toRequestBody("application/json; charset=utf-8".toMediaType())

        val httpUrl = httpUrlBuilder.build()
        val request = Request.Builder()
            .header("Content-Type", "application/json")
            .url(httpUrl)
            .post(body)
            .build()
        val response = httpClient.newCall(request).execute()

        return if (response.isSuccessful) {
            val raw = response.body?.string()
            try {
                val result = Gson().fromJson(raw, Model.Result::class.java)
                with(result.data.wikiCount) {
                    "$keyword:$totalhits"
                }
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
