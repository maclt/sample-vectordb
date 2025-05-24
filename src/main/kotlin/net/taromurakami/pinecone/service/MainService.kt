package net.taromurakami.pinecone.service

import net.taromurakami.pinecone.domain.QueryResult
import net.taromurakami.pinecone.domain.Vector
import net.taromurakami.pinecone.repository.PineconeRepository
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.math.sqrt
import kotlin.random.Random

@Service
class MainService(private val db: PineconeRepository) {
    fun insert(vector: List<Float>): Vector {
        val id = UUID.randomUUID().toString()
        db.upsert(id, vector)
        return Vector(id, vector)
    }

    fun query(
        vector: List<Float>,
        limit: Int?,
    ): List<QueryResult> {
        return db.query(vector, limit ?: 3)
    }

    fun findById(id: String): Vector {
        return db.fetch(id)
    }

    fun insertRamdom(): Vector {
        val id = UUID.randomUUID().toString()
        val vector = generateRandomNormalizedVector()
        db.upsert(id, vector)
        return Vector(id, vector)
    }

    private fun generateRandomNormalizedVector(): List<Float> {
        // Generate random components
        val a = Random.nextFloat() * 2 - 1
        val b = Random.nextFloat() * 2 - 1
        val c = Random.nextFloat() * 2 - 1

        // Normalize
        val length = sqrt(a * a + b * b + c * c)
        return listOf(a / length, b / length, c / length)
    }
}
