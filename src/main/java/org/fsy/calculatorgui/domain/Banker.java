package org.fsy.calculatorgui.domain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class Banker {

    public static float historyWinMoney; //操作错误之前庄家的钱
    public static float zhuangWinMoney;

    public static String name ;

    public static  float zhuangYuE = 0f; //开始游戏时候,庄余额　

    public static  float zhuangCurrentYuE = 0f; //当前游戏时刻,庄余额
}
