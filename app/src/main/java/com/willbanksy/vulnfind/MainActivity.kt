package com.willbanksy.vulnfind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.willbanksy.vulnfind.model.VulnDBModel
import com.willbanksy.vulnfind.ui.MainActivityView
import com.willbanksy.vulnfind.ui.theme.VulnFindTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VulnFindTheme {
//                val model: VulnDBModel by viewModels()
//                val model = ViewModelProvider(this)[VulnDBModel::class.java]
                val model = VulnDBModel(this)
//                model.initialiseDB(this)
                model.populateDefault()
                model.save()
                MainActivityView(model)
            }
        }
    }
}
