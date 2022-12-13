package cvb.com.br.gitapp.model

data class GitRepositoryInfo(
    val id: Long,
    val name: String,
    val qtdFork: Int,
    val qtdStar: Int,
    val ownerName: String?,
    val ownerUrl: String?
)