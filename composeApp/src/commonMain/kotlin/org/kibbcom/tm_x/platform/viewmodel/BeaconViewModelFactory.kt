package org.kibbcom.tm_x.platform.viewmodel

import androidx.lifecycle.ViewModelProvider
import org.kibbcom.tm_x.db.AppDatabase

expect class BeaconViewModelFactory(db: AppDatabase) : ViewModelProvider.Factory

