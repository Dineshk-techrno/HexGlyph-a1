package com.hexglyph.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// ── Entity ────────────────────────────────────────────────────────────────────

@Entity(tableName = "glyphs")
data class GlyphEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val createdAt: Long      = System.currentTimeMillis(),
    val direction: String,       // "sent" or "received"
    val plaintext: String,
    val groupCodeHint: String,   // first 3 chars of group code (for display)
    val filePath: String?        = null,
    val epochDay: Long,
)

// ── DAO ───────────────────────────────────────────────────────────────────────

@Dao
interface GlyphDao {

    @Query("SELECT * FROM glyphs ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<GlyphEntity>>

    @Query("SELECT * FROM glyphs WHERE direction = :dir ORDER BY createdAt DESC")
    fun observeByDirection(dir: String): Flow<List<GlyphEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(glyph: GlyphEntity): Long

    @Delete
    suspend fun delete(glyph: GlyphEntity)

    @Query("DELETE FROM glyphs WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM glyphs")
    suspend fun deleteAll()

    @Query("SELECT * FROM glyphs WHERE id = :id")
    suspend fun getById(id: Long): GlyphEntity?
}

// ── Database ──────────────────────────────────────────────────────────────────

@Database(entities = [GlyphEntity::class], version = 1, exportSchema = false)
abstract class GlyphDatabase : RoomDatabase() {
    abstract fun glyphDao(): GlyphDao
}
