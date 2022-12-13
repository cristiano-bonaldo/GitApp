package cvb.com.br.gitapp.network

import cvb.com.br.gitapp.network.apiresponse.GitResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    companion object {
        private const val PARAM_PAGE = "page"
    }

    @GET("search/repositories?q=language:kotlin&sort=stars")
    suspend fun getGitRepositories(@Query(PARAM_PAGE) page: Int = 1): Response<GitResponse>
}