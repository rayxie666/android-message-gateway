package com.messagegateway.data

import androidx.room.TypeConverter
import com.messagegateway.data.model.ForwardMethod
import com.messagegateway.data.model.ForwardStatus
import com.messagegateway.data.model.MatchType

class Converters {
    @TypeConverter fun fromMatchType(value: MatchType): String = value.name
    @TypeConverter fun toMatchType(value: String): MatchType = MatchType.valueOf(value)

    @TypeConverter fun fromForwardMethod(value: ForwardMethod): String = value.name
    @TypeConverter fun toForwardMethod(value: String): ForwardMethod = ForwardMethod.valueOf(value)

    @TypeConverter fun fromForwardStatus(value: ForwardStatus): String = value.name
    @TypeConverter fun toForwardStatus(value: String): ForwardStatus = ForwardStatus.valueOf(value)
}
