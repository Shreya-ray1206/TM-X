package org.kibbcom.tm_x.platform.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import org.kibbcom.tm_x.viewmodel.BeaconViewModel
import kotlin.reflect.KClass

actual class BeaconViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return BeaconViewModel() as T
    }
}