package com.baljeet.youdotoo.data.repository_implementations

import com.baljeet.youdotoo.data.local.entities.ColorPaletteEntity
import com.baljeet.youdotoo.data.local.room.YouDoTooDatabase
import com.baljeet.youdotoo.domain.repository_interfaces.ColorPaletteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ColorPaletteRepositoryImpl @Inject constructor(
    localDB: YouDoTooDatabase
) : ColorPaletteRepository {

    private val paletteDao = localDB.colorPaletteDao

    override suspend fun upsertAll(palettes : List<ColorPaletteEntity>) {
        paletteDao.upsertAll(palettes)
    }

    override fun getAllPalettesAsFlow(): Flow<List<ColorPaletteEntity>> {
        return  paletteDao.getAllPalettesAsFlow()
    }

    override suspend fun getAllPalettes(): List<ColorPaletteEntity> {
        return paletteDao.getAllPalettes()
    }

    override suspend fun getColorPaletteById(paletteId: String): ColorPaletteEntity? {
        return paletteDao.getColorPaletteById(paletteId)
    }

    override fun getAppliedPaletteAsFlow(): Flow<List<ColorPaletteEntity>> {
        return paletteDao.getAppliedPaletteAsFlow()
    }

    override suspend fun delete(palette: ColorPaletteEntity) {
        paletteDao.delete(palette)
    }


}