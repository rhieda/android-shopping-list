package com.example.shoppinglist

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class ShoppingItem(
    val id: Int,
    var name: String,
    var quantity: Int,
    var isEditing: Boolean = false
) {

}

@Composable
fun ShoppingListApp() {

    var shoppingItems: List<ShoppingItem> by remember {
        mutableStateOf(listOf<ShoppingItem>())
    }

    var shouldShowDialog: Boolean by remember {
        mutableStateOf(false)
    }

    var inputValue: String by remember {
        mutableStateOf("")
    }

    var quantityOfItems: String by remember {
        mutableStateOf("")
    }

    var isRowShoppingItemEditing: Boolean by remember {
        mutableStateOf(false)
    }

    var currentShoppingItemEditing: ShoppingItem? by remember {
        mutableStateOf(null)
    }

    fun displayDialog() {
        //automatically displays a dialog
        shouldShowDialog = true
    }

    fun onCreateShoppingItem() {
        if(inputValue.isNotBlank()) {
            val newItem = ShoppingItem(
                id = shoppingItems.size + 1,
                name = inputValue,
                quantity = quantityOfItems.toIntOrNull() ?: 0
            )
            shoppingItems += newItem
        }
    }

    fun clearFields() {
        shouldShowDialog = false
        inputValue = String()
        quantityOfItems = String()
        isRowShoppingItemEditing = false
        currentShoppingItemEditing = null
    }

    fun onEditShoppingItem() {
        var unwrappedShoppingItem: ShoppingItem

        // unwrap nullable value 🐈
        currentShoppingItemEditing?.let { it ->

            unwrappedShoppingItem = it
            // rebuild list with actual values changed
            val newList = shoppingItems.map { it ->
                if (it.id == unwrappedShoppingItem.id) {
                    it.name = inputValue
                    it.quantity = quantityOfItems.toIntOrNull() ?: 0
                }
                it.copy()
            }

            // refresh
            shoppingItems = listOf()
            // update
            shoppingItems = newList
        }
    }

    fun handleConfirmButton() {
        if (isRowShoppingItemEditing) {
            onEditShoppingItem()
        } else {
            onCreateShoppingItem()
        }

        clearFields()
    }

    fun onHandleEditShoppingItem(item: ShoppingItem) {
        isRowShoppingItemEditing = true
        shouldShowDialog = true
        currentShoppingItemEditing = item
    }

    fun onHandleDeleteShoppingItem(item: ShoppingItem) {
        var unwrappedShoppingItem: ShoppingItem
        // unwrap nullable value 🐈
        item?.let { it ->

            unwrappedShoppingItem = it

//            // rebuild list with actual values changed
//            val newList = shoppingItems.filter { it -> it.id != unwrappedShoppingItem.id }
//
//            // refresh
//            shoppingItems = listOf()
//            // update
//            shoppingItems = newList
            shoppingItems -= unwrappedShoppingItem
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 32.dp),
            onClick = ::displayDialog
        ) {
            Text("Add Item")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            items(shoppingItems) {
                ShopListItem(
                    shoppingItem = it,
                    onEditClick = { onHandleEditShoppingItem(it) },
                    onDeleteClick =  { onHandleDeleteShoppingItem(it) }
                )
            }
        }
    }

    if(shouldShowDialog) {
        AlertDialog(
            onDismissRequest = {
                shouldShowDialog = false
            },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Button(onClick = ::handleConfirmButton) {
                        Text("Add")
                    }

                    Button(onClick = { shouldShowDialog = false }) {
                        Text(text = "Cancel")
                    }
                }
            },
            title = {
                Text(text = "Add shopping item")
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = inputValue,
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        onValueChange = {
                            inputValue = it
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = quantityOfItems,
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        onValueChange = {
                        quantityOfItems = it
                    })


                }
            }
        )
    }
}

@SuppressLint("Range")
@Composable
fun ShopListItem(
    shoppingItem: ShoppingItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val modifier: Modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .border(
            width = 2.dp,
            color = Color(0XFF018786),
            shape = RoundedCornerShape(20)
        )

    val paddingModifier: Modifier = Modifier.padding(8.dp)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = shoppingItem.name,
            modifier = paddingModifier
        )

        Text(
            text = "Qty: ${shoppingItem.quantity}",
            modifier = paddingModifier
        )

        Row {
            IconButton(
                onClick = onEditClick,
                modifier = paddingModifier
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )
            }

            IconButton(
                onClick = onDeleteClick,
                modifier = paddingModifier
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null
                )
            }
        }
    }
}