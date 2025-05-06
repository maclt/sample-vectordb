package net.taromurakami.pinecone

import io.pinecone.clients.Index
import io.pinecone.clients.Pinecone
import io.pinecone.proto.DescribeIndexStatsResponse
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.openapitools.db_control.client.model.DeletionProtection
import kotlin.math.sqrt
import kotlin.random.Random

fun main() {
    val logger: Logger = LogManager.getLogger("PineconeApp")
    logger.info("Hello from Kotlin + Maven!")

    val pc: Pinecone =
        System.getenv("PINECONE_API_KEY").let { it: String? ->
            if (it == null) {
                logger.error("Error: Environment variable PINECONE_API_KEY is not defined")
                throw Exception("Error: Environment variable PINECONE_API_KEY is not defined")
            }

            Pinecone.Builder(it).build()
        }

    val indexName: String = "quickstart"

    try {
        pc.describeIndex(indexName)
        logger.info("Index $indexName already exists")
    } catch (e: Exception) {
        logger.debug("Index not found, creating a new one", e)
        pc.createServerlessIndex(indexName, "cosine", 3, "aws", "us-east-1", DeletionProtection.DISABLED, null)
        logger.info("Created new index: $indexName")
    }

    val index: Index = pc.getIndexConnection(indexName)
    logger.info("Connected to index: $indexName")

    logger.info("Waiting for index to be ready...")
    while (!pc.describeIndex(indexName).getStatus().getReady()) {
        logger.debug("Index not ready yet, waiting...")
        Thread.sleep(3000)
    }
    logger.info("Index is ready")

    repeat(10) { i ->
        val value = randomNormalizedVector3()
        logger.info("Upserting vector $i: $value to namespace ns1")
        index.upsert("vec$i", value,"ns1")
    }

    // Wait for the upserted vectors to be indexed
    logger.info("Waiting for vectors to be indexed...")
    Thread.sleep(10000)
    logger.info("Vectors should be indexed now")

    val indexStatsResponse: DescribeIndexStatsResponse = index.describeIndexStats(null)
    logger.info("Index stats: $indexStatsResponse")

    // Query
    val queryVector1: List<Float> = randomNormalizedVector3()

    logger.info("Querying with vector 1: $queryVector1 in namespace ns1")
    val queryResponse1 = index.query(3, queryVector1, null, null, null, "ns1", null, false, true)

    logger.info("Query response 1: $queryResponse1")
}

fun randomNormalizedVector3(): List<Float> {
    // Generate random components
    val a = Random.nextFloat() * 2 - 1
    val b = Random.nextFloat() * 2 - 1
    val c = Random.nextFloat() * 2 - 1

    // Normalize
    val length = sqrt(a * a + b * b + c * c)
    return listOf(a / length, b / length, c / length)
}
