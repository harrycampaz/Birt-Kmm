import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import model.BirdImage
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource



@Composable
fun BirdAppTheme(
    content: @Composable () -> Unit
){

    MaterialTheme (
        colors = MaterialTheme.colors.copy(
            primary = Color.Black
        ),
        shapes = MaterialTheme.shapes.copy(
           small =  AbsoluteCutCornerShape(0.dp),
            medium = AbsoluteCutCornerShape(0.dp),
            large = AbsoluteCutCornerShape(0.dp),
        )
    ){
        content()
    }

}
@Composable
fun App() {
    BirdAppTheme {

        val birdViewModel = getViewModel(
            Unit, viewModelFactory {
                BirdViewModel()
            }
        )
        BirdPage(birdViewModel)


    }
}

@Composable
fun BirdPage(viewModel: BirdViewModel){

    val uiState = viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            modifier = Modifier.fillMaxWidth().padding(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ){
            for (category in uiState.value.categories){
                Button(
                    onClick = {
                        viewModel.select(category)
                    }
                ){
                    Text(category)
                }
            }
        }


        AnimatedVisibility(uiState.value.selectedImages.isNotEmpty()){
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.fillMaxSize().padding(horizontal = 5.dp),
                content = {
                    items(uiState.value.selectedImages){

                        BirdImageCell(it)
                    }
                }
            )
        }
    }

}

@Composable
fun BirdImageCell(image: BirdImage) {

    KamelImage(
        asyncPainterResource("https://sebi.io/demo-image-api/${image.path}"),
        image.category,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth().aspectRatio(1.0f)
    )

}


expect fun getPlatformName(): String