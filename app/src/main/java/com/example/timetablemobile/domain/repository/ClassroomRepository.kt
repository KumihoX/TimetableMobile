package com.example.timetablemobile.domain.repository

import com.example.timetablemobile.data.remote.dto.ScheduleDto

interface ClassroomRepository {

    suspend fun getSchedule(
        number: Int,
        startsAt: String,
        endsAt: String
    ): ScheduleDto

}