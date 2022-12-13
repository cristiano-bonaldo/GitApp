package cvb.com.br.gitapp.gui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import cvb.com.br.gitapp.R
import cvb.com.br.gitapp.databinding.ItemGitRepositoryBinding
import cvb.com.br.gitapp.model.GitRepositoryInfo

class GitRepositoryAdapter(val context: Context, listGitRepositoryInfo: MutableList<GitRepositoryInfo>) :
    RecyclerView.Adapter<GitRepositoryAdapter.GitRepositoryInfoViewHolder>() {

    private var listGitRepositoryInfo: MutableList<GitRepositoryInfo>

    init {
        this.listGitRepositoryInfo = listGitRepositoryInfo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitRepositoryInfoViewHolder {
        val itemBinding =
            ItemGitRepositoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return GitRepositoryInfoViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: GitRepositoryInfoViewHolder, position: Int) =
        holder.bind(listGitRepositoryInfo[position])

    override fun getItemCount() = listGitRepositoryInfo.size

    fun updateList(newListGitRepositoryInfo: List<GitRepositoryInfo>) {
        //this.listGitRepositoryInfo = newListGitRepositoryInfo

        val diffCallback = GitRepositoryInfoDiffCallback(this.listGitRepositoryInfo, newListGitRepositoryInfo)
        val diffResult   = DiffUtil.calculateDiff(diffCallback)

        this.listGitRepositoryInfo.clear()
        this.listGitRepositoryInfo.addAll(newListGitRepositoryInfo)
        diffResult.dispatchUpdatesTo(this)

        //notifyDataSetChanged()
    }

    //========

    inner class GitRepositoryInfoViewHolder(private val binding: ItemGitRepositoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(gitRepositoryInfo: GitRepositoryInfo) {
            binding.tvRepository.text = gitRepositoryInfo.name
            binding.tvAuthor.text = gitRepositoryInfo.ownerName
            binding.tvQtdFork.text = gitRepositoryInfo.qtdFork.toString()
            binding.tvQtdStar.text = gitRepositoryInfo.qtdStar.toString()

            gitRepositoryInfo.ownerUrl?.apply {
                Glide.with(context)
                    .load(this)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user)
                    .into(binding.ivProfile)
            } ?: run {
                binding.ivProfile.setImageResource(R.drawable.ic_user)
            }
        }
    }

    //========

    inner class GitRepositoryInfoDiffCallback(private val oldList: List<GitRepositoryInfo>, private val newList: List<GitRepositoryInfo>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldGitRepo = oldList[oldItemPosition]
            val newGitRepo = newList[newItemPosition]

            return (oldGitRepo.name == newGitRepo.name)
        }
    }
}