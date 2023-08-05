package com.baljeet.youdotoo.domain.use_cases.palettes

import com.baljeet.youdotoo.data.local.entities.ColorPaletteEntity
import com.baljeet.youdotoo.domain.repository_interfaces.ColorPaletteRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UpsertPalettesUseCase @Inject constructor(
    private val repository: ColorPaletteRepository
){
    suspend operator fun invoke(palettes : List<ColorPaletteEntity>) {
        repository.upsertAll(palettes)
    }
}