package com.example.timetablemobile.domain.usecase.schedule

import com.example.timetablemobile.data.remote.dto.ScheduleDto
import com.example.timetablemobile.domain.repository.ScheduleRepository
import javax.inject.Inject

class GetClassroomScheduleUseCase @Inject constructor(
    private val repository: ScheduleRepository
) {

    suspend operator fun invoke(number: Int, startsAt: String, endsAt: String): ScheduleDto =
        repository.getClassroomSchedule(number, startsAt, endsAt)

}