package com.baljeet.youdotoo.presentation.ui.themechooser

import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getSampleDateInLong
import com.baljeet.youdotoo.data.local.entities.ColorPaletteEntity
import com.baljeet.youdotoo.domain.use_cases.palettes.GetAllPalettesAsFlowUseCase
import com.baljeet.youdotoo.domain.use_cases.palettes.UpsertPalettesUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinLocalDateTime
import javax.inject.Inject

@HiltViewModel
class ColorPalettesViewModel @Inject constructor(
    private val upsertPalettesUseCase: UpsertPalettesUseCase,
    private val getAllPalettesAsFlowUseCase: GetAllPalettesAsFlowUseCase
): ViewModel() {

    fun getColorPalettes() = getAllPalettesAsFlowUseCase()

    private val onlineDB = FirebaseFirestore.getInstance()


    private val todayDate = java.time.LocalDateTime.now().toKotlinLocalDateTime()

    private val weekAgoDate = LocalDateTime(
        year = todayDate.year,
        monthNumber = todayDate.monthNumber,
        dayOfMonth = todayDate.dayOfMonth,
        hour = 9,
        minute = 0,
        second = 0
    ).toInstant(TimeZone.currentSystemDefault())
        .minus(
            unit = DateTimeUnit.DAY,
            value = 6,
            timeZone = TimeZone.currentSystemDefault()
        ).epochSeconds




    init {
        /**
         * fetch latest themes once a week
         * **/
        if(SharedPref.lastColorsFetchDate < weekAgoDate ) {
            SharedPref.lastColorsFetchDate = getSampleDateInLong()
            onlineDB.collection("palettes").get().addOnSuccessListener { results ->
                if (results != null) {
                    val palettes = arrayListOf<ColorPaletteEntity>()
                    for (palette in results) {
                        palettes.add(
                            ColorPaletteEntity(
                                id = palette.getString("id") ?: "",
                                dayDark = palette.getLong("dayDark") ?: 0xFFEB06FF,
                                dayLight = palette.getLong("dayLight") ?: 0xFFEB06FF,
                                nightDark = palette.getLong("nightDark") ?: 0xFFEB06FF,
                                nightLight = palette.getLong("nightLight") ?: 0xFFEB06FF,
                                paletteName = palette.getString("paletteName") ?: ""
                            )
                        )
                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        upsertPalettesUseCase(palettes = palettes)
                    }
                }
            }
        }
    }
}