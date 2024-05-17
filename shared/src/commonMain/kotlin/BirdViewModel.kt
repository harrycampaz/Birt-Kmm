import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.BirdImage


data class BirdUIState(
    val images: List<BirdImage> = emptyList()
)

class BirdViewModel: ViewModel(){
    private val _uiState = MutableStateFlow<BirdUIState>(BirdUIState())
    
    val uiState = _uiState.asStateFlow()
    
    private val httpClient = HttpClient{
        install(ContentNegotiation){
            json()
        }
    }
    
    init {
        updateImage()
    }


    override fun onCleared() {
        httpClient.close()
    }
    
    private fun updateImage(){
        viewModelScope.launch {
            val images = getImage()
            _uiState.update {
                it.copy(images = images)
            }
        }
    }
    
    private suspend fun getImage(): List<BirdImage>{
        val images = httpClient.get("https://sebi.io/demo-image-api/pictures.json")
            .body<List<BirdImage>>()
        return  images
    }
}