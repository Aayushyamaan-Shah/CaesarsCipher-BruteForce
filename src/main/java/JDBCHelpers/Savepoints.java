package JDBCHelpers;

import com.sun.istack.internal.Nullable;

import java.sql.Savepoint;
import java.util.ArrayList;

public class Savepoints {
    ArrayList<Savepoint> savepoints;
    int currentIndex;

    public Savepoints(){
        savepoints = new ArrayList<>();
        currentIndex=-1;
    }

    public void addSavepoint(Savepoint savepoint){
        savepoints.add(savepoint);
        currentIndex++;
    }


    public void removeSavepoint(@Nullable Savepoint savepoint, int index){
        if(savepoint != null){
            savepoints.remove(savepoint);
            if(currentIndex >= index)
                currentIndex--;
        }else{
            if(index > -1){
                savepoints.remove(savepoints.get(index));
                if(currentIndex >= index)
                    currentIndex--;
            }
        }
    }

    public void removeSavepoint(Savepoint savepoint){
        removeSavepoint(savepoint, -1);
    }

    public void removeSavepoint(int index){
        removeSavepoint(null, index);
    }

}
