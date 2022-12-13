package cvb.com.br.gitapp.gui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import cvb.com.br.gitapp.model.GitRepositoryInfo
import cvb.com.br.gitapp.network.ApiException
import cvb.com.br.gitapp.usecase.GitUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(private val gitUseCase: GitUseCase) : ViewModel() {

    companion object {
        private val TAG = MainViewModel::class.java.canonicalName

        private const val C_MAX_PAGE = 30
    }

    class MainViewModelFactory(private val gitUseCase: GitUseCase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)){
                return MainViewModel(gitUseCase) as T
            }
            throw IllegalArgumentException("Erro ao instanciar ViewModel: MainViewModel")
        }
    }

    //=============

    sealed class GetListGitRepositoryInfo {
        object StatusLoading: GetListGitRepositoryInfo()
        object StatusNoData: GetListGitRepositoryInfo()
        data class StatusOK(val listGitRepository: List<GitRepositoryInfo>) : GetListGitRepositoryInfo()
        data class StatusError(val error: String) : GetListGitRepositoryInfo()
    }

    private val mStatusGetListGitRepositoryInfo: MutableLiveData<GetListGitRepositoryInfo> =
        MutableLiveData<GetListGitRepositoryInfo>()
    val statusGetListGitRepositoryInfo: LiveData<GetListGitRepositoryInfo>
        get() = mStatusGetListGitRepositoryInfo


    private val mPage: MutableLiveData<Int> = MutableLiveData<Int>(0)

    private var mListRepositoryInfo: MutableLiveData<MutableList<GitRepositoryInfo>> =
        MutableLiveData<MutableList<GitRepositoryInfo>>()

    private fun getPage():Int {
        var page = 0

        mPage.value?.apply {
            page = this + 1
            mPage.value = page
        } ?: run {
            page = 1
        }

        return page
    }

    private fun getFullList(newListGitRepository: List<GitRepositoryInfo>): List<GitRepositoryInfo> {
        var list = mListRepositoryInfo.value
        if (list == null) {
            list = mutableListOf()
        }

        list.apply {
            this.addAll(newListGitRepository)

            mListRepositoryInfo.postValue(this)
        }

        return list.toList()
    }

    fun loadGitRepositories() {
        viewModelScope.launch {
            try {
                val page = getPage()

                if (page > C_MAX_PAGE) {
                    mStatusGetListGitRepositoryInfo.postValue(GetListGitRepositoryInfo.StatusNoData)
                    return@launch
                }

                if (page == 1) {
                    delay(2000) // First Screen -> Presented as a splash screen
                } else {
                    mStatusGetListGitRepositoryInfo.postValue(GetListGitRepositoryInfo.StatusLoading)
                }

                val newListGitRepository = gitUseCase.getListGitRepository(page)

                val fullListGitRepository = getFullList(newListGitRepository)

                mStatusGetListGitRepositoryInfo.postValue(GetListGitRepositoryInfo.StatusOK(fullListGitRepository.toList()))
            } catch (f: ApiException) {
                val error = f.message ?: "-"
                mStatusGetListGitRepositoryInfo.postValue(GetListGitRepositoryInfo.StatusError(error))
            } catch (e: Exception) {
                val error = "Generic error!"
                mStatusGetListGitRepositoryInfo.postValue(GetListGitRepositoryInfo.StatusError(error))
                Log.e(TAG, "Error on loadGitRepositories(): " + e.localizedMessage)
            }
        }
    }
}