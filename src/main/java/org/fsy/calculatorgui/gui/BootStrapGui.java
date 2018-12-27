package org.fsy.calculatorgui.gui;

import org.fsy.calculatorgui.StringUtils;
import org.fsy.calculatorgui.domain.Banker;
import org.fsy.calculatorgui.domain.Player;
import org.fsy.calculatorgui.utils.ClipBoardUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;

public final class BootStrapGui extends JFrame {
    private final int length = 960;
    private final int width =  550;

    private final int rowsCount = 11;

    private StringBuffer result = new StringBuffer();
    //闲
    private java.util.List<Player> playerList = new ArrayList<Player>();

    JComboBox<String> zhuangCombo = new JComboBox<>();;

    Box xianBox = Box.createHorizontalBox();

    Box zhuangBox = Box.createHorizontalBox();

    Box zongBox = Box.createHorizontalBox();

    JPanel chouPanel = new JPanel();


    //选择输赢按钮时候显示颜色
    private Color choosedColor = Color.green;

    JLabel zongChouValue = new JLabel("0");

    Box bottomBox = Box.createVerticalBox();

    JPanel gridPanel = new JPanel();

    public BootStrapGui(){
        //设置主题

//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (UnsupportedLookAndFeelException e) {
//            e.printStackTrace();
//        }




        //标题
        setTitle("庄家返佣_计算工具_V1版");

        //关闭模式
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //设置默认居中
//        setLocationRelativeTo(null);

        setSize(length , width);

        //固定大小
        setResizable(false);

        setLayout(new BorderLayout());

        createGridPanel();

        setVisible(true);
    }

    private void createGridPanel() {


        gridPanel.setLayout(new GridLayout(rowsCount , 5));

        //11行　5列



        for(int rowsIndex = 0 ; rowsIndex < rowsCount ; rowsIndex++ ){
            //第一列　下注金额　第二列　倍数　第三列　赢按钮　第四列　输按钮　第五列　名字　

            Player player = Player.builder()
                    .money(new JTextField(8))
                    .rate(new JTextField(8))
                    .name(new JTextField(8))
                    .build();

            if(rowsIndex == 0){
                player.setMoney(new JTextField("庄家"));
                player.setRate(new JTextField("庄家"));
                player.setLoseButton(new JButton("庄家"));
                player.setWinButton(new JButton("庄家"));
            }else{


                JButton loseButton = new JButton("输");
                JButton winButton = new JButton("赢");

                winButton.addActionListener((ActionEvent actionEvent)->{
                    winButton.setForeground(choosedColor);
                    loseButton.setForeground(null);
                    player.setWin(true);
                });

                loseButton.addActionListener((ActionEvent->{
                    loseButton.setForeground(choosedColor);
                    winButton.setForeground(null);
                }));

                player.setWinButton(winButton);
                player.setLoseButton(loseButton);
            }


            playerList.add(player);

            //这是闲家
            gridPanel.add(player.getMoney());
            gridPanel.add(player.getRate());
            gridPanel.add(player.getWinButton());
            gridPanel.add(player.getLoseButton());
            gridPanel.add(player.getName());

        }

        createChouPanel();

        //1
        add(chouPanel , BorderLayout.NORTH);

        //2
        add(gridPanel , BorderLayout.CENTER);

        createBottomBox();

        add(bottomBox , BorderLayout.SOUTH);

    }

    private void createBottomBox() {
        //3
        JButton callButton = new JButton("计算输赢结果");
        callButton.addActionListener((ActionEvent->{
            //添加抽水率
            playerList.stream().skip(1)
                    .filter((Player player)->{
                        //必填字段校验
                        return StringUtils.isNotEmpty(player.getMoney().getText())
                                && StringUtils.isNotEmpty(player.getRate().getText())
                                && StringUtils.isNotEmpty(player.getName().getText());
                    })
                    .forEach(
                            (Player player)->{
                                player.setChouRate(Float.valueOf(zhuangCombo.getSelectedItem().toString()));
                            }
                    );

            //
            final Float[] sumBet = {0f}; //TODO why define here so style
            playerList.stream()
                    .peek((Player player)->{
                        if(player.getRate().getText().equals("庄家")){
                            Banker.name = player.getName().getText();
                        }
                    })
                    .skip(1)
                    .filter((Player player)->{
                        //必填字段校验
                        return StringUtils.isNotEmpty(player.getMoney().getText())
                                && StringUtils.isNotEmpty(player.getRate().getText())
                                && StringUtils.isNotEmpty(player.getName().getText());
                    })//跳过庄　
                    .forEach(
                            (Player player)->{
                                sumBet[0] = sumBet[0] +  Float.valueOf(player.getMoney().getText());
                                result = new StringBuffer(player.toString() + "\n");
                                System.out.println(player.toString());
                                player.doChouRate();
//                        System.out.println("庄家赢"+Player.zhuangWinMoney);
                            }
                    );

            result.append("庄家"+Banker.name + "赢"+ Banker.zhuangWinMoney+"\n");
            System.out.println("庄家"+Banker.name + "赢"+ Banker.zhuangWinMoney);

            //设置总抽水
            float zongChouValueCache = Float.valueOf(zongChouValue.getText()) + (sumBet[0] / 100  * Float.valueOf(zhuangCombo.getSelectedItem().toString()) );

            zongChouValue.setText( zongChouValueCache == 0f  ? "" : zongChouValueCache+"");


        }));

        bottomBox.add(callButton);

        //复制玩家数据
        JButton copy = new JButton("复制玩家数据");
        copy.addActionListener((ActionEvent actionEve)->{
            ClipBoardUtil.copyToClip(result.toString());
        });


        JButton restart = new JButton("开始下局游戏");
        restart.addActionListener((ActionEvent actionEvent)->{
            //庄家金额重置
            Banker.zhuangWinMoney = 0f;
            //抽水重置
            zongChouValue.setText("0");
            //表格内容重置
            Arrays.stream( gridPanel.getComponents())
                    .forEach((Component compo)->{
                        if(compo instanceof JTextField){
                            JTextField textField = (JTextField)compo;
                            if(StringUtils.isNotEmpty(textField.getText())){
                                if(!textField.getText().equals("庄家")){
                                    textField.setText("");
                                }
                            }
                        }
                    });
           ;

        });

        JButton reback = new JButton("点击错误返回");
        reback.addActionListener((ActionEvent actionEvent)->{
            //清理
            Banker.zhuangWinMoney = Banker.historyWinMoney;
        });

        bottomBox.add(copy);
        bottomBox.add(restart);
        bottomBox.add(reback);
    }

    private void createChouPanel() {
        JLabel chouDianConfig = new JLabel("抽点设置 : ");



        createZhungChouBox();

        createXianBox();

        createZongBox();



        Box rightBox = Box.createVerticalBox();
        rightBox.add(zhuangBox);
        rightBox.add(xianBox);
        rightBox.add(zongBox);

        Box leftBox = Box.createVerticalBox();
        leftBox.add(chouDianConfig);


        chouPanel.setLayout(new FlowLayout());
        chouPanel.add(leftBox);
        chouPanel.add(rightBox);
    }

    private void createZongBox() {

        JLabel zongChou = new JLabel("总抽水:");



        zongBox.add(zongChou);
        zongBox.add(zongChouValue);

    }

    private void createXianBox() {
        JLabel xianChou = new JLabel("闲抽:");
        JComboBox<String> xianCombo = new JComboBox<>();

        xianBox.add(xianChou);
        xianBox.add(xianCombo);
    }

    private void createZhungChouBox() {

        JLabel zhuangChou = new JLabel("庄抽:");

        zhuangCombo.addItem("1");
        zhuangCombo.addItem("2");
        zhuangCombo.addItem("3");
        zhuangCombo.addItem("4");
        zhuangCombo.addItem("5");
        zhuangCombo.addItem("6");
        zhuangCombo.addItem("7");
        zhuangCombo.addItem("8");
        zhuangCombo.addItem("9");
        zhuangCombo.addItem("10");


        zhuangBox.add(zhuangChou);
        zhuangBox.add(zhuangCombo);

    }

}
