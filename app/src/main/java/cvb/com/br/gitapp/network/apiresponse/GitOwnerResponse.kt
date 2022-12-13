package cvb.com.br.gitapp.network.apiresponse

import com.google.gson.annotations.SerializedName

data class GitOwnerResponse(
    val login: String?,

    @SerializedName(value = "avatar_url")
    val url: String?
)
