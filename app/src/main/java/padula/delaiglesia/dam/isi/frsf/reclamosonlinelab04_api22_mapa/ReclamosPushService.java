package padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.content.Context.NOTIFICATION_SERVICE;

public class ReclamosPushService extends FirebaseMessagingService {
    public static int BUSCAR_Y_EDITAR = 5;
    public ReclamosPushService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        int mNotificationId = 001 ;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService( NOTIFICATION_SERVICE );
// el mensaje envia el ID del reclamo y el ID del nuevo estado.
// lo que debera hacer es
// generar una notificacion indicando de dicha actualizacion
// y cuando dicha notificacion se abre, se muestra el formulario detalle del reclamo
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder( this )
                        .setSmallIcon(android.R.drawable. stat_notify_chat )
                        .setContentTitle( "Reclamo Actualizado" )
                        .setContentText( "El reclamo " +remoteMessage.getData().get("idReclamo")
                                        + " pasó al ESTADO "
                                        + remoteMessage.getData().get("nuevoEstado") );

        Intent resultIntent = new Intent( this , FormReclamo. class );
        //le agrego al intent el idReclamo para que pueda cargar los datos del reclamo notificado como actualizado
        resultIntent.putExtra("ID_RECLAMO", Integer.parseInt(remoteMessage.getData().get("idReclamo")));
        resultIntent.putExtra("REQUEST", BUSCAR_Y_EDITAR);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this ,
                        0 ,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
