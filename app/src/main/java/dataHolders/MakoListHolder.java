package dataHolders;


import java.util.ArrayList;
import dbObjects.MakoEvent;

public class MakoListHolder {
    private static ArrayList<MakoEvent> mList;


    public static ArrayList<MakoEvent> getmList() {
        return mList;
    }

    public static void setmList(ArrayList<MakoEvent> mList) {
        MakoListHolder.mList = mList;
    }

    public static boolean isEmpty(){
        return(mList==null);
    }
}
