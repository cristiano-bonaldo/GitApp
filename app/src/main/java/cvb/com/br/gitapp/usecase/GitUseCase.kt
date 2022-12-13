package cvb.com.br.gitapp.usecase

import cvb.com.br.gitapp.model.GitRepositoryInfo
import cvb.com.br.gitapp.repository.GitRepository

class GitUseCase(private val gitRepository: GitRepository) {

    suspend fun getListGitRepository(page: Int): List<GitRepositoryInfo> {
        return gitRepository.getListGitRepository(page)
    }
}
