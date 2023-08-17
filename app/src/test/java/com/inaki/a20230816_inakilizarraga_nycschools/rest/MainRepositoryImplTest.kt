package com.inaki.a20230816_inakilizarraga_nycschools.rest

import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class MainRepositoryImplTest {

    private lateinit var testObject: MainRepository
    private val mockApi = mockk<ServiceApi>(relaxed = true)

    @Before
    fun setUp() {
        testObject = MainRepositoryImpl(mockApi)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `retrieve schools network call`() = runTest {
        coEvery { mockApi.getSchools() } returns Response.success(listOf(mockk(), mockk()))

        val result = testObject.retrieveSchools()

        assertThat(result.body()).hasSize(2)
    }

    @Test
    fun `retrieve scores network call`() = runTest {
        coEvery { mockApi.getScores() } returns Response.success(listOf(mockk(), mockk()))

        val result = testObject.retrieveScores()

        assertThat(result.body()).hasSize(2)
    }
}