package com.elyeproj.clientandroid

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException

class ApolloPresenter(private val mainView: MainView) {

    fun performFetch(keyword: String) {
        mainView.showLoading()
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

    private fun showApolloResult(keyword: String) {
        mainView.showResult("Apollo $keyword")
    }
}
