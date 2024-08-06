package com.example.ip_test_task.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ip_test_task.R
import com.example.ip_test_task.domain.entity.Item
import com.example.ip_test_task.ui.theme.IptesttaskTheme

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {

    val state = viewModel.mainStateFlow.collectAsState(MainScreenState.Initial)
    MainScreenContent(
        state,
        { viewModel.onSearchTextChanged(it) },
        { item, amount -> viewModel.onAmountChange(item, amount) },
        { item -> viewModel.onDeleteItem(item) },
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(
    state: State<MainScreenState>,
    onSearchTextChange: (String) -> Unit,
    onAmountChange: (Item, Int) -> Unit,
    onDeleteItem: (Item) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.list_items)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            when (val currentState = state.value) {
                MainScreenState.Initial -> {}
                is MainScreenState.Items -> {
                    ItemList(currentState, onAmountChange, onDeleteItem, onSearchTextChange)
                }
            }

        }
    }
}

@Composable
fun CustomSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = query,
        onValueChange = onQueryChange,
        label = { Text(stringResource(R.string.search_items)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                tint = Color.Black,
                contentDescription = "Search"
            )
        },
        trailingIcon = {
            if (query.isNotBlank()) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear search",
                    tint = Color.Black,
                    modifier = Modifier
                        .clickable {
                            onQueryChange("")
                        }
                )
            }
        },
    )
}

@Composable
fun ItemList(
    state: MainScreenState.Items,
    onAmountChange: (Item, Int) -> Unit,
    onDeleteItem: (Item) -> Unit,
    onSearchTextChange: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {
        item {
            CustomSearchBar(state.search, onQueryChange = { onSearchTextChange(it) })
        }
        items(items = state.items, key = { it.id }) {
            ItemCard(item = it, onAmountChange, onDeleteItem)
        }

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ItemCard(item: Item, onAmountChange: (Item, Int) -> Unit, onDeleteItem: (Item) -> Unit) {
    var showDialogEdit by remember { mutableStateOf(false) }
    var showDialogDelete by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current


    if (showDialogEdit) {
        AmountEditDialog(
            currentAmount = item.amount,
            onQuantityChange = { onAmountChange(item, it) },
            onDismiss = { showDialogEdit = false }
        )
    }

    if (showDialogDelete) {
        DeleteDialog(
            onConfirmDelete = { onDeleteItem(item) },
            onDismiss = { showDialogDelete = false }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = dimensionResource(id = R.dimen.baseline_grid_small))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { focusManager.clearFocus() },
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )

    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.baseline_grid_regular)),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = item.name,
                    style = MaterialTheme.typography.titleLarge
                )

                IconButton(
                    onClick = { showDialogEdit = true },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = Color.Blue
                    )
                }

                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.baseline_grid)))

                IconButton(
                    onClick = { showDialogDelete = true },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
            }

            FlowRow(
                modifier = Modifier
                    .padding(vertical = dimensionResource(id = R.dimen.baseline_grid_regular)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (chip in item.tags) {
                    TagChip(chip)
                }
            }

            Row {
                Column(
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text(
                        text = stringResource(R.string.storage),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(text = item.amount.toString())
                }
                Column(
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text(
                        text = stringResource(R.string.date_add),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(text = item.time)
                }
            }
        }
    }
}

@Composable
fun AmountEditDialog(
    currentAmount: Int,
    onQuantityChange: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var amount by remember { mutableIntStateOf(currentAmount) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.amount_items),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        icon = {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Settings"
            )
        },
        text = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { if (amount > 0) amount-- }) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Decrease"
                    )
                }
                Text(
                    text = amount.toString(),
                    style = MaterialTheme.typography.headlineMedium
                )
                IconButton(onClick = { amount++ }) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowUp,
                        contentDescription = "Increase"
                    )
                }

            }
        },
        confirmButton = {
            TextButton(onClick = {
                onQuantityChange(amount)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun DeleteDialog(
    onConfirmDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.delete_item),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = stringResource(R.string.delete_message),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        icon = {
            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = "Warning"
            )
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirmDelete()
                onDismiss()
            }) {
                Text("Да")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Нет")
            }
        }
    )
}

// Пришлось написать кастомный чип, потому что во встроенном чипе вертикальный отступ
@Composable
fun TagChip(
    name: String,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .padding(2.dp)
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.baseline_grid_small)))
            .border(
                1.dp,
                Color.Gray,
                RoundedCornerShape(dimensionResource(id = R.dimen.baseline_grid_small))
            )
            .clickable(onClick = onClick)
    ) {

        Text(
            text = name,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.baseline_grid_small))
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ItemListPreview() {
    IptesttaskTheme {
//        ItemList(
//            items = listOf(
//                Item(
//                    id = 2496,
//                    name = "iPhone 13",
//                    time = "6651",
//                    tags = listOf("Телефон", "Телефон", "Телефон", "Телефон"),
//                    amount = 3225
//                )
//            ),
//            { _, _ -> }, { }
//        )
    }
}
