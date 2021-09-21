package com.elyeproj.clientandroid

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), MainView {

    companion object {
        const val SCHEME = "http"
        const val BASE = "ENTER_YOUR_HOST_HERE"
        const val PORT = 4000
    }

    private lateinit var searchText: EditText
    private lateinit var resultText: TextView
    private lateinit var progressIndicator: ProgressBar

    private val apolloPresenter = ApolloPresenter(this)
    private val presenter = MainPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        searchText = findViewById(R.id.searchText)
        resultText = findViewById(R.id.resultText)
        progressIndicator = findViewById(R.id.progressIndicator)
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
