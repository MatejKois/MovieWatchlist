import com.example.moviewatchlist.omdbsearch.OmdbApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OmdbClient {
    private const val BASE_URL = "https://www.omdbapi.com/"

    private val httpClient = OkHttpClient.Builder().build()

    val api: OmdbApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create()) // will convert JSON to Kotlin objects
            .build()
            .create(OmdbApi::class.java)
    }
}
