@file:OptIn(ExperimentalMaterial3Api::class)

package com.kanawish.sample.hello.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kanawish.sample.hello.theme.AppTheme

@Composable
fun MarketingCard(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFF5E5A1), // Warm yellow
                        Color(0xFFFE5B99), // Soft pink
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Headline text
            Text(
                text = buildAnnotatedString {
                    append("Close\n")
                    append("every deal.")
                },
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )

            // Subtitle text
            Text(
                text = "Radiant helps you sell more by revealing sensitive information about your customers.",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color.Black.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Buttons
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(100)
            ) {
                Text(
                    "Get started",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Button(
                onClick = { /* TODO */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.5f),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(100)
            ) {
                Text(
                    "See pricing",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MarketingCardPreview() {
    AppTheme {
        Surface {
            MarketingCard(
                modifier = Modifier
                    .padding(16.dp)
                    .width(380.dp)
            )
        }
    }
}


@Preview(device = Devices.PIXEL_TABLET)
@Composable
fun MainShopScreenPreview() {
    AppTheme {
        Surface {
            MainShopScreen()
        }
    }
}

@Composable
fun MainShopScreen() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(
                    style = MaterialTheme.typography.displaySmall,
                    text = "Cart"
                ) },
                navigationIcon = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    // QR Code scanner button
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            imageVector = Icons.Filled.QrCodeScanner,
                            contentDescription = "Scan QR Code"
                        )
                    }
                    // Search button
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    }
                    // More options menu
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { /* TODO */ },
                    icon = { Icon(Icons.Filled.Home, "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /* TODO */ },
                    icon = { Icon(Icons.Filled.ShoppingCart, "Orders") },
                    label = { Text("Orders") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /* TODO */ },
                    icon = { Icon(Icons.Filled.LocalShipping, "Products") },
                    label = { Text("Products") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /* TODO */ },
                    icon = { Icon(Icons.Filled.PersonAdd, "Customers") },
                    label = { Text("Customers") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /* TODO */ },
                    icon = { Icon(Icons.Filled.Menu, "More") },
                    label = { Text("More") }
                )
            }
        }
    ) { paddingValues ->
        MainContent(modifier = Modifier.padding(paddingValues))
    }
}

@Composable
fun MainContent(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        // Left pane (2/3 of screen)
        Column(
            modifier = Modifier
                .weight(2f)
                .padding(16.dp)
        ) {
            // Setup banner and action cards (previous implementation)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            "Set up to sell",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            "Get started",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Action Cards Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(actionItems) { item ->
                    ActionCard(item)
                }
            }
        }
        
        // Right pane (1/3 of screen)
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            CartPanel()
        }
    }
}

@Composable
private fun CartPanel() {
    Column {
        Button(
            onClick = { /* TODO */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Add customer")
        }
        
        Spacer(Modifier.height(24.dp))
        
        Text(
            "Total",
            style = MaterialTheme.typography.titleLarge
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("0 items")
            Text("US$0.00")
        }
        
        Spacer(Modifier.height(16.dp))
        
        Button(
            onClick = { /* TODO */ },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        ) {
            Text("Checkout")
        }
    }
}

@Composable
fun ActionCard(item: ActionItem) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.5f),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        onClick = { /* TODO */ }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

data class ActionItem(
    val title: String,
    val icon: ImageVector
)

val actionItems = listOf(
    ActionItem("Add customer", Icons.Filled.PersonAdd),
    ActionItem("Add custom sale", Icons.Filled.Add),
    ActionItem("Apply discount", Icons.Filled.Discount),
    ActionItem("Ship all items", Icons.Filled.LocalShipping)
)
