package com.snsService.sns.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmArgs {


    //알람을 발생시킨 사람
    private  Integer fromUserId;

    private  Integer targetId;

  //  private List<Integer> targetIds;

    //private  Integer alarmOccurId;
}
