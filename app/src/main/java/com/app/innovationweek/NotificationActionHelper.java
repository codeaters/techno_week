package com.app.innovationweek;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Madeyedexter on 08-03-2017.
 */

public class NotificationActionHelper {
        public static void startActivity(String className, Bundle extras, Context context){
            Class cls=null;
            try {
                cls = Class.forName(className);
            }catch(ClassNotFoundException e){
                //means you made a wrong input in firebase console
            }
            Intent i = new Intent(context, cls);
            i.putExtras(extras);
            context.startActivity(i);
        }
}
