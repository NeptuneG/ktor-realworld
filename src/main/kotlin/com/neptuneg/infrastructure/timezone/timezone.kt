package com.neptuneg.infrastructure.timezone

import java.time.OffsetDateTime
import java.time.ZoneOffset

val JST_ZONE_OFFSET: ZoneOffset = ZoneOffset.of("+09:00")
fun now() = OffsetDateTime.now(JST_ZONE_OFFSET)
