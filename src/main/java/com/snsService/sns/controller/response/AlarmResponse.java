package com.snsService.sns.controller.response;

import com.snsService.sns.model.Alarm;
import com.snsService.sns.model.AlarmArgs;
import com.snsService.sns.model.AlarmType;
import com.snsService.sns.model.User;
import com.snsService.sns.model.entity.AlarmEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AlarmResponse {

    private  Integer id;
    private AlarmType alarmType;

    private AlarmArgs alarmArgs;

    private  String text;

    private Timestamp registeredAt;

    private Timestamp updateAt;

    private  Timestamp deletedAt;

    public static AlarmResponse fromAlarm(Alarm alarm){

        return  new AlarmResponse(
                alarm.getId(),
                alarm.getAlarmType(),
                alarm.getArgs(),
                alarm.getAlarmType().getAlarmText(),
                alarm.getRegisteredAt(),
                alarm.getUpdateAt(),
                alarm.getDeletedAt()
        );
    }
}
