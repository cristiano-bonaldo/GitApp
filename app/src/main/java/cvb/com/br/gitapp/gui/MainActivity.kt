package cvb.com.br.gitapp.gui

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cvb.com.br.gitapp.R
import cvb.com.br.gitapp.databinding.ActivityMainBinding
import cvb.com.br.gitapp.gui.adapter.GitRepositoryAdapter
import cvb.com.br.gitapp.gui.viewmodel.MainViewModel
import cvb.com.br.gitapp.model.GitRepositoryInfo
import cvb.com.br.gitapp.network.RetrofitConfig
import cvb.com.br.gitapp.repository.GitRepository
import cvb.com.br.gitapp.usecase.GitUseCase
import cvb.com.br.gitapp.util.DialogUtil
import cvb.com.br.gitapp.util.NetworkUtil


class MainActivity : AppCompatActivity() {
    private companion object {
        private const val C_VIEW_FLIPPER_LIST = 1
    }

    private lateinit var mBinding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel

    private lateinit var gitRepositoryAdapter: GitRepositoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        configViewModel()

        setObservable()

        configList()

        configNestedScrollViewForPaging()

        if (savedInstanceState == null) {
            loadData()
        }
    }

    private fun loadData() {
        if (NetworkUtil.checkForInternet(this)) {
            viewModel.loadGitRepositories()
        } else {
            showDialogNoInternet()
        }
    }

    private fun configNestedScrollViewForPaging() {
        mBinding.apply {
            nestedScrollView.setOnScrollChangeListener { v, _, scrollY, _, _ ->

                val nestedScrollView = (v as NestedScrollView)
                val listView = nestedScrollView.getChildAt(0)

                if (scrollY == listView.measuredHeight - nestedScrollView.measuredHeight) {
                    loadData()
                }
            }
        }
    }

    private fun configViewModel() {
        val gitRepository = GitRepository(RetrofitConfig().getAPIService())
        val gitUseCase = GitUseCase(gitRepository)

        val viewModelFactory = MainViewModel.MainViewModelFactory(gitUseCase)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    private fun setObservable() {
        viewModel.statusGetListGitRepositoryInfo.observe(this, this::onStatusGetListGitRepositoryInfo)
    }

    private fun enableLoading(isLoading: Boolean) {
        if (isLoading) {
            mBinding.lottieLoading.playAnimation()
            mBinding.lottieLoading.visibility = View.VISIBLE
        } else {
            mBinding.lottieLoading.pauseAnimation()
            mBinding.lottieLoading.visibility = View.GONE
        }
    }

    private fun onStatusGetListGitRepositoryInfo(statusGetListGitRepositoryInfo: MainViewModel.GetListGitRepositoryInfo) {
        when (statusGetListGitRepositoryInfo) {
            is MainViewModel.GetListGitRepositoryInfo.StatusLoading -> {
               enableLoading(true)
            }

            is MainViewModel.GetListGitRepositoryInfo.StatusNoData -> {
                enableLoading(false)
                showDialogNoData()
            }

            is MainViewModel.GetListGitRepositoryInfo.StatusError -> {
                enableLoading(false)
                showDialogError(statusGetListGitRepositoryInfo.error)
            }

            is MainViewModel.GetListGitRepositoryInfo.StatusOK -> {
                enableLoading(false)

                mBinding.viewFlipper.displayedChild = C_VIEW_FLIPPER_LIST
                updateList(statusGetListGitRepositoryInfo.listGitRepository)
            }
        }
    }

    private fun configList() {
        mBinding.recyclerView.layoutManager = LinearLayoutManager(this)

        gitRepositoryAdapter = GitRepositoryAdapter(applicationContext, mutableListOf())

        mBinding.recyclerView.adapter = gitRepositoryAdapter
    }

    private fun updateList(newListGitRepositoryInfo: List<GitRepositoryInfo>) {
        updateQtdRepositoriesLoaded(newListGitRepositoryInfo.size)

        gitRepositoryAdapter.updateList(newListGitRepositoryInfo)
    }

    private fun updateQtdRepositoriesLoaded(qtd: Int) {
        supportActionBar?.apply {
            val msg = getString(R.string.repositories_loaded, qtd.toString())
            subtitle = msg
        }
    }

    private fun showDialogNoData() {
        DialogUtil.showDialog(this, R.string.no_data, R.string.bt_ok, null, null, null)
    }

    private fun showDialogNoInternet() {
        val onClickRetry  = DialogInterface.OnClickListener { _, _ -> loadData() }
        val onClickCancel = DialogInterface.OnClickListener { _, _ -> finish() }

        DialogUtil.showDialog(this, R.string.no_internet, R.string.bt_retry, onClickRetry, R.string.bt_cancel, onClickCancel)
    }

    private fun showDialogError(error: String) {
        val onClickRetry  = DialogInterface.OnClickListener { _, _ -> loadData() }
        val onClickCancel = DialogInterface.OnClickListener { _, _ -> finish() }

        val msg = getString(R.string.error, error)

        DialogUtil.showDialog(this, msg, R.string.bt_retry, onClickRetry, R.string.bt_cancel, onClickCancel)
    }
}