package net.taromurakami.pinecone.repository

import com.google.protobuf.Struct
import com.google.protobuf.Value
import io.pinecone.clients.Index
import io.pinecone.clients.Pinecone
import net.taromurakami.pinecone.domain.QueryResult
import net.taromurakami.pinecone.domain.Vector
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Repository
class PineconeRepository() {
    val logger: Logger = LoggerFactory.getLogger(PineconeRepository::class.java)
    val indexName: String = "quickstart"
    val namespace: String = "ns1"
    val index: Index
    val formatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneOffset.UTC)
    val utcNow: Instant = Instant.now()

    init {
        val pc =
            System.getenv("PINECONE_API_KEY").let { it: String? ->
                if (it == null) {
                    throw Exception("Error: Environment variable PINECONE_API_KEY is not defined")
                }

                Pinecone
                    .Builder(it)
                    .withTlsEnabled(true)
                    .build()
            }

        index = pc.getIndexConnection(indexName)
    }

    fun upsert(
        id: String,
        vector: List<Float>,
    ) {
        val response =
            index.upsert(
                id,
                vector,
                null,
                null,
                Struct.newBuilder()
                    .putFields("registered_at", Value.newBuilder().setStringValue(formatter.format(utcNow)).build())
                    .build(),
                namespace,
            )

        logger.info("upsert executed $id. Response: $response")
    }

    fun query(
        vector: List<Float>,
        limit: Int,
    ): List<QueryResult> {
        val response =
            index.query(
                limit,
                vector,
                null,
                null,
                null,
                namespace,
                null,
                false,
                true,
            )

        logger.info("query executed. Response: $response")

        return response.matchesList.map { QueryResult(it.id, it.score) }
    }

    fun fetch(id: String): Vector {
        val response =
            index.fetch(listOf(id), namespace)

        logger.info("queryByVectorId executed. Response: $response")

        val vector = response.getVectorsOrThrow(id)

        return Vector(vector.id, vector.valuesList)
    }
}
