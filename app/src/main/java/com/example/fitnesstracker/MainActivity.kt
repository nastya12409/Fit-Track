package com.example.fitnesstracker
import android.os.Bundle
import androidx.compose.ui.graphics.luminance
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fitnesstracker.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //навігація: два маршрути
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "list"
                    ) {
                        //  перший екран
                        composable("list") {
                            HomeScreen(navController = navController)
                        }
                        // другий екран з аргументом itemId
                        composable("details/{itemId}") { backStackEntry ->
                            val itemId =
                                backStackEntry.arguments?.getString("itemId")?.toInt() ?: 1
                            DetailScreen(itemId = itemId, navController = navController)
                        }
                    }
                }
            }
        }
    }
}

//головний екран: назва і меню з п'яти розділів
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel() // ViewModel
) {
    // Підписка на стан (кожна зміна = перемальовування екрана)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.screen_title),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, bottom = 24.dp)
        )

        // Поле пошуку: дія йде у ViewModel, а не в локальний стан
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = { viewModel.onSearchQueryChange(it) },
            label = { Text(stringResource(R.string.search_section)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Прапорець завантаження
        if (uiState.isLoading) {
            Text(
                text = stringResource(R.string.loading),
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            // Фільтр за текстом пошуку
            uiState.sections
                .filter { it.title.contains(uiState.searchQuery, ignoreCase = true) }
                .forEach { section ->
                    MenuCard(
                        emoji = section.emoji,
                        title = section.title,
                        description = section.shortDescription,
                        containerColor = section.containerColor,
                        onClick = {
                            navController.navigate("details/${section.id}")
                        }
                    )
                }
        }
    }
}

// другий екран: деталі обраного розділу
@Composable
fun DetailScreen(
    itemId: Int,
    navController: NavController,
    viewModel: HomeViewModel = viewModel()   // той самий екран отримує
) {
    // Підписка на стан
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    // Шукаємо потрібний розділ за id
    val section = uiState.sections.firstOrNull { it.id == itemId }
    if (section == null) {
        // Якщо не знайшли — показуємо повідомлення
        Column(modifier = Modifier.padding(16.dp)) {
            Text(stringResource(R.string.section_not_found), fontSize = 18.sp)
            Button(onClick = { navController.popBackStack() }) {
                Text(stringResource(R.string.back))
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Кнопка «Назад» у topBar
            Button(onClick = { navController.popBackStack() }) {
                Text(stringResource(R.string.back))
            }
            //заголовок
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(text = section.emoji, fontSize = 40.sp)
                Text(
                    text = section.title,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }

            //повний опис
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = section.containerColor
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = section.fullDescription,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }

            //список функцій розділу
            Text(
                text = stringResource(R.string.what_available),
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            section.features.forEach { feature ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "•", fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                    Text(
                        text = feature,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MenuCard(
    emoji: String,
    title: String,
    description: String,
    containerColor: Color,
    onClick: () -> Unit
) {
    // Підбір кольору тексту під яскравість картки
    val textColor =
        if (containerColor.luminance() > 0.5f) Color.Black.copy(alpha = 0.87f)
        else Color.White.copy(alpha = 0.95f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 24.sp)
            }

            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor // заголовок: білий/чорний
                )
                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = textColor.copy(alpha = 0.85f) //опис
                )
            }
            Text(
                text = "›",
                fontSize = 28.sp,
                color = textColor.copy(alpha = 0.7f)
            )
        }
    }
}