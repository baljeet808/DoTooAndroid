package com.baljeet.youdotoo.presentation.ui.themechooser

import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.data.local.entities.ColorPaletteEntity
import com.baljeet.youdotoo.domain.use_cases.palettes.GetAllPalettesAsFlowUseCase
import com.baljeet.youdotoo.domain.use_cases.palettes.GetAppliedPaletteAsFlowUseCase
import com.baljeet.youdotoo.domain.use_cases.palettes.UpsertPalettesUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ColorPalettesViewModel @Inject constructor(
    private val upsertPalettesUseCase: UpsertPalettesUseCase,
    private val getAllPalettesAsFlowUseCase: GetAllPalettesAsFlowUseCase,
    private val getAppliedPaletteAsFlowUseCase: GetAppliedPaletteAsFlowUseCase,
): ViewModel() {

    fun getColorPalettes() = getAllPalettesAsFlowUseCase()

    fun getApplied() = getAppliedPaletteAsFlowUseCase()

    private val onlineDB = FirebaseFirestore.getInstance()

    init {
        onlineDB.collection("palettes").addSnapshotListener { snapshot, error ->
            if(error == null && snapshot != null) {
                val palettes = arrayListOf<ColorPaletteEntity>()
                for (palette in snapshot) {
                    palettes.add(
                        ColorPaletteEntity(
                            id = palette.getString("id") ?: "",
                            isApplied = palette.getBoolean("applied") ?: false,
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

    fun updateSelectedColor(palette : ColorPaletteEntity, currentPalette : ColorPaletteEntity?){

        val newAppliedPalette = palette.copy(
            isApplied = true
        )

        val oldUpdatedPalette = currentPalette?.copy(
            isApplied = false
        )

        onlineDB.collection("palettes")
            .document(newAppliedPalette.id)
            .set(newAppliedPalette)
            .addOnSuccessListener {
                CoroutineScope(Dispatchers.IO).launch {
                    upsertPalettesUseCase(listOf(newAppliedPalette))
                }
            }
        oldUpdatedPalette?.let {
            onlineDB.collection("palettes")
                .document(oldUpdatedPalette.id)
                .set(oldUpdatedPalette)
                .addOnSuccessListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        upsertPalettesUseCase(listOf(oldUpdatedPalette))
                    }
                }
        }
    }

}