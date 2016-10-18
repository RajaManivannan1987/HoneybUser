package com.sample.honeybuser.Singleton;


import com.sample.honeybuser.InterFaceClass.SaveCompletedInterface;

/**
 * Created by IM028 on 6/21/16.
 */
public class Complete {
    private static Complete ourInstance = new Complete();
    private static Complete ratingDialogInstance = new Complete();
    private static Complete ratingReloadDialogInstance = new Complete();
    private static Complete offerDialogInstance = new Complete();
    private SaveCompletedInterface completedInterface;

    public static Complete getInstance() {
        return ourInstance;
    }

    public static Complete offerDialogInstance() {
        return offerDialogInstance;
    }

    public static Complete ratingDialogInstance() {
        return ratingDialogInstance;
    }

    public static Complete ratingReloadDialogInstance() {
        return ratingReloadDialogInstance;
    }

    private Complete() {
    }

    public void setListener(SaveCompletedInterface saveCompletedInterface) {
        completedInterface = saveCompletedInterface;
    }

    public void orderCompleted() {
        if (completedInterface != null)
            completedInterface.completed();
    }
}
