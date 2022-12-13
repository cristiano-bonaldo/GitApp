package cvb.com.br.gitapp.network

import cvb.com.br.gitapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitConfig {

    private companion object {

        const val C_URL = "https://api.github.com/"

        fun getRetrofitInstance() : Retrofit {

            val httpClient = OkHttpClient.Builder()

            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BASIC

                httpClient.addInterceptor(logging)
            }

            return Retrofit.Builder()
                .baseUrl(C_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
        }
    }

    fun getAPIService(): APIService = getRetrofitInstance().create(APIService::class.java)
}