package cvb.com.br.gitapp.network.apiresponse

import com.google.gson.annotations.SerializedName
import cvb.com.br.gitapp.model.GitRepositoryInfo

data class GitRepositoryResponse(
    val id: Long,
    val name: String,

    @SerializedName(value = "forks_count")
    val forks: Int,

    @SerializedName(value = "stargazers_count")
    val stars: Int,

    val owner: GitOwnerResponse
)

fun GitRepositoryResponse.convertGitRepositoryResponseToGitRepositoryInfo(): GitRepositoryInfo {
    return GitRepositoryInfo(this.id, this.name, this.forks, this.stars, this.owner.login, this.owner.url)
}
