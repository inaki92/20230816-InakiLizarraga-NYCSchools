package com.inaki.a20230816_inakilizarraga_nycschools.viewmodel

import com.google.common.truth.Truth.assertThat
import com.inaki.a20230816_inakilizarraga_nycschools.model.scores.Score
import com.inaki.a20230816_inakilizarraga_nycschools.usecases.UseCasesWrapper
import com.inaki.a20230816_inakilizarraga_nycschools.utils.State
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private lateinit var testObject: MainViewModel
    private val mockCasesWrapper = mockk<UseCasesWrapper>(relaxed = true)

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        every { mockCasesWrapper.scoreUseCase } returns mockk {
            every { this@mockk() } returns flowOf(
                State.SUCCESS(
                    listOf(
                        mockk {
                            every { dbn } returns "dbn"
                        },
                        mockk {
                            every { dbn } returns "other"
                        }
                    )
                )
            )
        }

        testObject = MainViewModel(mockCasesWrapper)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `get school scores when selected school`() {
        testObject.selectedSchool = mockk {
            every { dbn } returns "dbn"
        }

        val states = mutableListOf<State<List<Score>>>()

        val job = testScope.launch {
            testObject.score.collect {
                states.add(it)
            }
        }

        testObject.schoolScore()

        assertThat(states).hasSize(2)
        assertThat(states[0]).isInstanceOf(State.LOADING::class.java)
        assertThat(states[1]).isInstanceOf(State.SUCCESS::class.java)
        assertThat((states[1] as State.SUCCESS).result.firstOrNull()).isNotNull()
        assertThat((states[1] as State.SUCCESS).result.firstOrNull()?.dbn).isEqualTo("dbn")

        job.cancel()
    }

    @Test
    fun `get school scores error when no selected school`() {
        testObject.selectedSchool = mockk {
            every { dbn } returns null
        }

        val states = mutableListOf<State<List<Score>>>()

        val job = testScope.launch {
            testObject.score.collect {
                states.add(it)
            }
        }

        testObject.schoolScore()

        assertThat(states).hasSize(2)
        assertThat(states[0]).isInstanceOf(State.LOADING::class.java)
        assertThat(states[1]).isInstanceOf(State.ERROR::class.java)
        assertThat((states[1] as State.ERROR).error.message).isEqualTo("School has no dbn")

        job.cancel()
    }

    @Test
    fun `get school scores error when selected school has no dbn`() {
        testObject.selectedSchool = null

        val states = mutableListOf<State<List<Score>>>()

        val job = testScope.launch {
            testObject.score.collect {
                states.add(it)
            }
        }

        testObject.schoolScore()

        assertThat(states).hasSize(2)
        assertThat(states[0]).isInstanceOf(State.LOADING::class.java)
        assertThat(states[1]).isInstanceOf(State.ERROR::class.java)
        assertThat((states[1] as State.ERROR).error.message).isEqualTo("No school has been selected")

        job.cancel()
    }
}