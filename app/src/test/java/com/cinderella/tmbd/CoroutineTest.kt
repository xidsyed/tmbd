package com.cinderella.tmbd

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.handleCoroutineException
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test

class CoroutineTest {

    @Test
    fun `coroutine test`() = runBlocking {
        // Create a coroutineScope
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            println("Exception caught: $exception")
        }
        val job = Job()
        val currentScope = CoroutineScope(job + exceptionHandler)
        coroutineScope {
            launch {
                // Do some work in coroutine 1
                delay(200)
                println("Coroutine 1 completed")
            }

            launch {
                // Do some work in coroutine 2
                delay(800)
                println("Coroutine 2 completed")
            }


            // Cancel all coroutines launched within the scope after 500 milliseconds
            delay(500)
            val scopeJob = coroutineContext[Job]!!
            scopeJob.cancel()
            println("Scope cancelled")
        }


        println("Outside of coroutineScope")
    }

    @Test
    fun `test coroutine scope changing`() = runBlocking {
        println("Out coroutine (Context --> ${coroutineContext}) started")

        // coroutineScope creates a CoroutineScope which inherits the context from the parent ,
        // but overrides the Job
        coroutineScope {
            println("Inn coroutine (Context --> ${coroutineContext}) started")
            coroutineScope {
                println("InnInn coroutine (Context --> ${coroutineContext}) started")
            }
            launch {
                println("InnChild coroutine (Context --> ${coroutineContext}) started")
                delay(1000)
                println("Child coroutine finished")
                coroutineScope {
                    println("InnChildInn coroutine (Context --> ${coroutineContext}) started")
                }
            }

            delay(500)
            println("Inn coroutine finished")
        }

        println("Out coroutine (Context --> ${coroutineContext}) still active")
    }

    @Test
    fun `test scopes`() = runBlocking {
        println("0 : (Context --> ${coroutineContext})")
        val job = Job()

        coroutineScope {
            println("3 : (Context --> ${coroutineContext})")

            val job3_0 = CoroutineScope(Dispatchers.Default).launch {
                println("3.0 : (Context --> ${coroutineContext})")
            }
        }
        return@runBlocking
    }

    @Test
    fun `child coroutine cancellation`(): Unit = runBlocking {
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            println("Exception caught: $exception")
        }
        val coroutineScope = CoroutineScope(Dispatchers.Default + exceptionHandler)
        coroutineScope.launch {
            println("Coroutine started")
            val job = subCoroutine(coroutineScope)
            job.join()
            println("Coroutine finished")

        }.join()
    }

    suspend fun subCoroutine(coroutineScope: CoroutineScope) : Job {
        val job : Job = CoroutineScope(Dispatchers.IO).launch {
            launch {
                println("Sub A started")
                delay(500)
                println("Sub A finished")
            }
            launch {
                println("Sub B started")
                delay(500)
                throw Exception("Sub B exception")
                println("Sub B finished")
            }
        }
        return job
    }
}

