package com.baljeet.youdotoo.data.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.baljeet.youdotoo.data.local.entities.ColorPaletteEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ColorPaletteDao {
    @Upsert
    suspend fun upsertAll(palettes : List<ColorPaletteEntity>)

    @Query("SELECT * FROM palettes")
    fun getAllPalettesAsFlow() : Flow<List<ColorPaletteEntity>>

    @Query("SELECT * FROM palettes")
    suspend fun getAllPalettes() : List<ColorPaletteEntity>

    @Query("SELECT * FROM palettes where id = :paletteId ")
    suspend fun getColorPaletteById(paletteId : String) : ColorPaletteEntity?

    @Query("SELECT * FROM palettes where isApplied")
    fun getAppliedPaletteAsFlow(): Flow<List<ColorPaletteEntity>>

    @Delete
    suspend fun delete(palette : ColorPaletteEntity)

}