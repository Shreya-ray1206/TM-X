package org.kibbcom.tm_x.platform.viewmoel_factory

import androidx.lifecycle.ViewModelProvider
import org.kibbcom.tm_x.db.AppDatabase

expect class BeaconViewModelFactory(db: AppDatabase) : ViewModelProvider.Factory

