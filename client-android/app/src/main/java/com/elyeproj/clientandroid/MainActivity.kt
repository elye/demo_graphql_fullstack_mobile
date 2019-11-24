package com.elyeproj.clientandroid

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainView {

    companion object {
        const val SCHEME = "http"
        const val BASE = "ENTER_YOUR_HOST_HERE"
        const val PORT = 4000
    }


    private val apolloPresenter = ApolloPresenter(this)
    private val presenter = MainPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun beginSearch(view: View) {
        if (searchText.text.isNotBlank()) {
            presenter.performFetch(searchText.text.toString())
        }
    }

    fun beginApolloSearch(view: View) {
        if (searchText.text.isNotBlank()) {
            apolloPresenter.performFetch(searchText.text.toString())
        }
    }

    override fun showResult(result: String) {
        runOnUiThread {
            progressIndicator.visibility = View.GONE
            resultText.text = result
        }
    }

    override fun showLoading() {
        resultText.text = ""
        progressIndicator.visibility = View.VISIBLE
    }

    override fun onPause() {
        presenter.cancelFetch()
        super.onPause()
    }
}
