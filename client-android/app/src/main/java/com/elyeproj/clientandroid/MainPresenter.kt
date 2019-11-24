package com.elyeproj.clientandroid

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class MainPresenter(private val mainView: MainView) {

    object Model {
        data class Result(val data: Data)
        data class Data(val wikiCount: WikiCount)
        data class WikiCount(val keyword: String, val totalhits: Int)
    }

    companion object {
        private val queryString = """
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
        .scheme(MainActivity.SCHEME)
        .host(MainActivity.BASE)
        .port(MainActivity.PORT)

    fun performFetch(searchText: String) {
        mainView.showLoading()

        disposable.dispose()
        disposable = Single.just(searchText)
            .map { fetchOnBackground(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { mainView.showResult(it) },
                { mainView.showResult(it.localizedMessage) })
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

    fun cancelFetch() {
        disposable.dispose()
    }
}
