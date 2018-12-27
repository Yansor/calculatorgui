package org.fsy.calculatorgui.utils;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

public class ClipBoardUtil {

    public static Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    public static void  copyToClip(String str){
        Transferable trandata = new StringSelection(str);

        clipboard.setContents(trandata, null);
    }

    public static String getClip(){
        Transferable clipT = clipboard.getContents(null); //获取文本中的Transferable对象

        if(clipT!=null){

            if(clipT.isDataFlavorSupported(DataFlavor.stringFlavor)) //判断内容是否为文本类型stringFlavor

            {
                try {
                    return (String)clipT.getTransferData(DataFlavor.stringFlavor); //返回指定flavor类型的数据
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }
        return null;
    }
}
