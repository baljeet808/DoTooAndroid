package com.baljeet.youdotoo.domain.use_cases.palettes

import com.baljeet.youdotoo.data.local.entities.ColorPaletteEntity
import com.baljeet.youdotoo.domain.repository_interfaces.ColorPaletteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetAppliedPaletteAsFlowUseCase @Inject constructor(
    private val repository: ColorPaletteRepository
){
    operator fun invoke(): Flow<List<ColorPaletteEntity>> {
        return repository.getAppliedPaletteAsFlow()
    }
}