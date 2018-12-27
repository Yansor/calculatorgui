package org.fsy.calculatorgui.domain;


import javax.swing.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player {
     private boolean win = false;
     private JTextField money  = new JTextField(8);
     private JTextField rate = new JTextField(8);
     private JButton winButton = new JButton("赢");
     private JButton loseButton = new JButton("输");
     private JTextField name = new JTextField(8);

     public boolean win(){
          return win;
     }

     private float chouRate = 0f;
     /**
      * 抽水比例
      * @param chouRate
      */
     public void setChouRate(float chouRate){
          this.chouRate = chouRate;
     }

     public void doChouRate(){
          Banker.historyWinMoney = Banker.zhuangWinMoney;
          float zhuangBase = (win ? -1f : 1f -  (chouRate / 100)   );
          Banker.zhuangWinMoney+= zhuangBase *  Float.valueOf(money.getText()) * Float.valueOf(rate.getText());
     }
     @Override
     public String toString(){

          float winBase = (win ? 1f - (chouRate / 100) : -1f  );

          return name.getText() +"赢" +  (winBase  * Float.valueOf(money.getText()) * Float.valueOf(rate.getText()));
     }

}
