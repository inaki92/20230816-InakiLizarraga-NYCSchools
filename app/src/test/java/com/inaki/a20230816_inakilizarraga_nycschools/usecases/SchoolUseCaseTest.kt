package com.inaki.a20230816_inakilizarraga_nycschools.usecases

import com.google.common.truth.Truth.assertThat
import com.inaki.a20230816_inakilizarraga_nycschools.model.schools.School
import com.inaki.a20230816_inakilizarraga_nycschools.rest.MainRepository
import com.inaki.a20230816_inakilizarraga_nycschools.utils.NetworkConnection
import com.inaki.a20230816_inakilizarraga_nycschools.utils.State
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class SchoolUseCaseTest {

    private lateinit var testObject: SchoolUseCase
    private val mockRepo = mockk<MainRepository>(relaxed = true)
    private val mockConnection = mockk<NetworkConnection>(relaxed = true)

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        testObject = SchoolUseCase(mockRepo, mockConnection)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke schools get a success list`() {
        every { mockConnection.isNetworkAvailable() } returns true
        coEvery { mockRepo.retrieveSchools() } returns Response.success(listOf(mockk(), mockk()))

        val states = mutableListOf<State<List<School>>>()

        val job = testScope.launch {
            testObject.invoke().collect {
                states.add(it)
            }
        }

        assertThat(states).hasSize(1)
        assertThat(states[0]).isInstanceOf(State.SUCCESS::class.java)
        assertThat((states[0] as State.SUCCESS).result).hasSize(2)

        job.cancel()
    }

    @Test
    fun `invoke schools get a network connection error`() {
        every { mockConnection.isNetworkAvailable() } returns false

        val states = mutableListOf<State<List<School>>>()

        val job = testScope.launch {
            testObject.invoke().collect {
                states.add(it)
            }
        }

        assertThat(states).hasSize(1)
        assertThat(states[0]).isInstanceOf(State.ERROR::class.java)
        assertThat((states[0] as State.ERROR).error.message).isEqualTo("No internet connection available")

        job.cancel()
    }

    @Test
    fun `invoke schools get a null body error`() {
        every { mockConnection.isNetworkAvailable() } returns true
        coEvery { mockRepo.retrieveSchools() } returns Response.success(null)

        val states = mutableListOf<State<List<School>>>()

        val job = testScope.launch {
            testObject.invoke().collect {
                states.add(it)
            }
        }

        assertThat(states).hasSize(1)
        assertThat(states[0]).isInstanceOf(State.ERROR::class.java)
        assertThat((states[0] as State.ERROR).error.message).isEqualTo("Null body in response")

        job.cancel()
    }

    @Test
    fun `invoke schools get a response error`() {
        every { mockConnection.isNetworkAvailable() } returns true
        coEvery { mockRepo.retrieveSchools() } returns mockk {
            every { isSuccessful } returns false
            every { errorBody() } returns mockk {
                every { string() } returns "error"
            }
        }

        val states = mutableListOf<State<List<School>>>()

        val job = testScope.launch {
            testObject.invoke().collect {
                states.add(it)
            }
        }

        assertThat(states).hasSize(1)
        assertThat(states[0]).isInstanceOf(State.ERROR::class.java)
        assertThat((states[0] as State.ERROR).error.message).isEqualTo("error")

        job.cancel()
    }

    @Test
    fun `invoke schools throws any exception error`() {
        every { mockConnection.isNetworkAvailable() } returns true
        coEvery { mockRepo.retrieveSchools() } throws Exception("error")

        val states = mutableListOf<State<List<School>>>()

        val job = testScope.launch {
            testObject.invoke().collect {
                states.add(it)
            }
        }

        assertThat(states).hasSize(1)
        assertThat(states[0]).isInstanceOf(State.ERROR::class.java)
        assertThat((states[0] as State.ERROR).error.message).isEqualTo("error")

        job.cancel()
    }
}