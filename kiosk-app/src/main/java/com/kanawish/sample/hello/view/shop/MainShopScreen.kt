@file:OptIn(ExperimentalMaterial3Api::class)

package com.kanawish.sample.hello.view.shop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kanawish.sample.hello.theme.AppTheme

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
                title = {
                    Text(
                        style = MaterialTheme.typography.titleMedium,
                        text = "KIOSK DEMO APP"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
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
        Row(modifier = Modifier.Companion.padding(paddingValues)) {
            // Left pane (2/3 of screen) with its own header
            Column(modifier = Modifier.Companion.weight(2f)) {
                StoreActionSection()
            }

            // Right pane (1/3 of screen) with its own header
            Column(modifier = Modifier.Companion.weight(1f)) {
                // Cart panel content with improved layout
                CartPanel()
            }
        }
    }
}

@Composable
private fun StoreActionSection() {
    // Store section header
    TopAppBar(
        title = {
            Text(
                text = "Store Name",
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Start
            )
        },
        actions = {
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    Icons.Filled.QrCodeScanner,
                    modifier = Modifier.size(42.dp),
                    contentDescription = "Scan QR Code",
                )
            }
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    Icons.Filled.Search,
                    modifier = Modifier.size(42.dp),
                    contentDescription = "Search"
                )
            }
        }
    )

    // Main content (setup banner and action cards)
    Column(modifier = Modifier.Companion.padding(16.dp)) {
        SetupBanner()
        Spacer(Modifier.Companion.height(16.dp))
        ActionCardsGrid()
    }
}

@Composable
fun SetupBanner() {
    Surface(
        modifier = Modifier.Companion.fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.Companion.padding(16.dp),
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {
            Icon(
                Icons.Filled.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
            Spacer(Modifier.Companion.width(16.dp))
            Column {
                Text(
                    "SET UP TO SELL",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    "Get started",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun ActionCardsGrid() {
    val actionItems = listOf(
        ActionItem("Choose customer", Icons.Filled.PersonAdd),
        ActionItem("Custom sale", Icons.Filled.Add),
        ActionItem("Give discount", Icons.Filled.Discount),
        ActionItem("Ship items", Icons.Filled.LocalShipping)
    )

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

@Composable
fun ActionCard(item: ActionItem) {
    Surface(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .aspectRatio(2f),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        onClick = { /* TODO */ }
    ) {
        Column(
            modifier = Modifier.Companion
                .padding(24.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.Companion.size(48.dp)
            )
            Text(
                text = item.title,
                style = MaterialTheme.typography.displaySmall.copy(
                    fontSize = 28.sp,
                )
            )
        }
    }
}

data class ActionItem(
    val title: String,
    val icon: ImageVector
)

@Composable
private fun CartPanel() {
    // Cart section header
    TopAppBar(
        title = {
            Text(
                "Cart",
                style = MaterialTheme.typography.displaySmall
            )
        },
        actions = {
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = "More options"
                )
            }
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Clear cart"
                )
            }
        }
    )

    Column(
        modifier = Modifier.Companion.fillMaxHeight()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        // Top section - Customer info
        Box(modifier = Modifier.Companion.fillMaxWidth()) {
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier.Companion.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Add customer")
            }
            // CustomerCard() // Commented out for now
        }

        // Middle section - Line items
        LazyColumn(
            modifier = Modifier.Companion
                .weight(1f)
                .fillMaxWidth()
        ) {
            item {
                LineItem(
                    title = "Thelonious Monk & John Coltrane – Monk/Trane (Vinyl Record)",
                    quantity = 1,
                    price = "US$25.00",
                    originalPrice = "US$36.00"
                )
            }
        }

        // Bottom section - Totals and checkout
        Column(
            modifier = Modifier.Companion.fillMaxWidth()
        ) {
            // Subtotal
            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Subtotal")
                Text("US$25.00")
            }

            // Taxes
            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Taxes")
                Text("US$3.74")
            }

            Spacer(modifier = Modifier.Companion.height(16.dp))

            // Total
            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Total",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text("1 item")
                }
                Text(
                    "US$28.74",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.Companion.height(16.dp))

            // Checkout button
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier.Companion.fillMaxWidth(),
                enabled = true
            ) {
                Text("Checkout")
            }
        }
    }
}

@Composable
private fun LineItem(
    title: String,
    quantity: Int,
    price: String,
    originalPrice: String? = null
) {
    Row(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.Companion.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("$quantity×")
            Column {
                Text(title)
                if (originalPrice != null) {
                    Text(
                        originalPrice,
                        style = MaterialTheme.typography.bodySmall,
                        textDecoration = TextDecoration.Companion.LineThrough
                    )
                }
            }
        }
        Text(price)
    }
}

