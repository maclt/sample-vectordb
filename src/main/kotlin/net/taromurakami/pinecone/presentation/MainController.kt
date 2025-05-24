package net.taromurakami.pinecone.presentation

import net.taromurakami.pinecone.domain.QueryResult
import net.taromurakami.pinecone.service.MainService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/vectors")
class MainController(
    private val service: MainService,
) {
    val logger: Logger = LoggerFactory.getLogger(MainController::class.java)

    @PostMapping
    fun insert(
        @RequestBody vector: List<Float>,
    ): ResponseEntity<CreatedResponse> {
        logger.info("POST /vector. Received vector: $vector")
        val res = service.insert(vector)
        return ResponseEntity
            .status(201)
            .body(CreatedResponse(res.id, res.data))
    }

    @GetMapping("query")
    fun query(
        @RequestBody vector: List<Float>,
        @RequestParam(required = false) limit: Int?,
    ): ResponseEntity<QueryResponse> {
        logger.info("GET /query/query. Received vector: $vector, limit: $limit")
        val res = service.query(vector, limit)
        return ResponseEntity.ok(QueryResponse(res))
    }

    @GetMapping("find-by-id/{id}")
    fun findById(
        @PathVariable id: String,
    ): ResponseEntity<FindOneResponse> {
        logger.info("GET /vector/find-by-id/. Received id: $id")
        val res = service.findById(id)
        return ResponseEntity.ok(FindOneResponse(res.id, res.data))
    }

    @PostMapping("random")
    fun insertRandom(): ResponseEntity<CreatedResponse> {
        logger.info("POST /vector/random.")
        val res = service.insertRamdom()
        return ResponseEntity
            .status(201)
            .body(CreatedResponse(res.id, res.data))
    }

    data class QueryResponse(
        val results: List<QueryResult>,
        val size: Int = results.size,
    )

    data class FindOneResponse(
        val id: String,
        val vector: List<Float>,
    )

    data class CreatedResponse(
        val id: String,
        val vector: List<Float>,
    )
}
