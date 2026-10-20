package com.example.fitnesstracker

import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                    HomeScreen()
                }
            }
        }
    }
}

//Головний екран: назва, меню з чотирьох розділів
@Composable
fun HomeScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Fit-Track",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, bottom = 24.dp)
        )
        //чотири вибори
        MenuCard(
            emoji = "🍽",
            title = "Щоденник харчування",
            description = "Запис калорій, води та цукру",
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            onClick = {
                Toast.makeText(context, "Потім", Toast.LENGTH_SHORT).show()
            }
        )

        MenuCard(
            emoji = "📝",
            title = "Замітки",
            description = "Нотатки про харчування та тренування",
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            onClick = {
                Toast.makeText(context, "Потім", Toast.LENGTH_SHORT).show()
            }
        )

        MenuCard(
            emoji = "🏃",
            title = "Матеріали про спорт",
            description = "Статті та поради з тренувань",
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            onClick = {
                Toast.makeText(context, "Потім", Toast.LENGTH_SHORT).show()
            }
        )
        MenuCard(
            emoji = "📒",
            title = "Трекер звичок",
            description = "Контроль свох звичок",
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            onClick = {
                Toast.makeText(context, "Потім", Toast.LENGTH_SHORT).show()
            }
        )
        MenuCard(
            emoji = "⚙️",
            title = "Налаштування",
            description = "Профіль, тема та інші параметри",
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            onClick = {
                Toast.makeText(context, "Потім", Toast.LENGTH_SHORT).show()
            }
        )

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

            //назва та опис
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "›",
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}