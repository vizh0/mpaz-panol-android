package com.example.mpaz_panol.ui.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpaz_panol.data.local.entity.ItemEntity
import com.example.mpaz_panol.data.repository.InventoryRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class InventoryViewModel(private val repository: InventoryRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val filteredItems: StateFlow<List<ItemEntity>> = combine(
        repository.allItems,
        _searchQuery
    ) { items, query ->
        if (query.isBlank()) items
        else items.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.category.contains(query, ignoreCase = true)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun saveItem(item: ItemEntity) {
        viewModelScope.launch {
            if (item.id == 0) repository.insertItem(item)
            else repository.updateItem(item)
        }
    }

    fun deleteItem(item: ItemEntity) {
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }
}