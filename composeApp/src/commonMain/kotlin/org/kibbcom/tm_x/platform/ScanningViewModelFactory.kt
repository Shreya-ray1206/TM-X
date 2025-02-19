package org.kibbcom.tm_x.platform

import androidx.lifecycle.ViewModelProvider
import org.kibbcom.tm_x.db.AppDatabase

// Expected factory class for ViewModel creation
expect class ScanningViewModelFactory(db: AppDatabase) : ViewModelProvider.Factory