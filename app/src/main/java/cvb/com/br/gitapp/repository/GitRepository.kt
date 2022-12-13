package cvb.com.br.gitapp.repository

import android.util.Log
import cvb.com.br.gitapp.model.GitRepositoryInfo
import cvb.com.br.gitapp.network.APIService
import cvb.com.br.gitapp.network.ApiException
import cvb.com.br.gitapp.network.apiresponse.convertGitRepositoryResponseToGitRepositoryInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GitRepository(private val apiService: APIService) {
    private companion object {
        private val TAG = GitRepository::class.java.canonicalName
    }


    suspend fun getListGitRepository(page: Int): List<GitRepositoryInfo> {
        return withContext(Dispatchers.IO) {

            val response = apiService.getGitRepositories(page = page)

            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw ApiException("API Error - Invalid response")
            }

            try {
                body.items.map { gitRepositoryResponse -> gitRepositoryResponse.convertGitRepositoryResponseToGitRepositoryInfo() }
            } catch (e: Exception) {
                Log.e(TAG, "Error on getListGitRepository(): " + e.localizedMessage)
                throw ApiException("API Error - Response is not valid!")
            }
        }
    }
}