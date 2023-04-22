package com.neptuneg.infrastructure.timezone

import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

val JST_ZONE_OFFSET: ZoneOffset = ZoneOffset.of("+09:00")
fun now(): OffsetDateTime = OffsetDateTime.now(JST_ZONE_OFFSET).truncatedTo(ChronoUnit.MILLIS)
