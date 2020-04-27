package com.example.t2cc;

import android.app.Notification;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.t2cc.FirestoreConnections.NotificationsCollectionAccessors;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class AlertNotificationService extends FirebaseMessagingService {

  private static final String TAG = "AlertNotificationService";
  FirebaseAuth mAuth = FirebaseAuth.getInstance();
  FirebaseUser mCurrentUser = mAuth.getCurrentUser();
  String mCurrentUserID;

  @Override
  public void onNewToken(String token) {
    Log.d(TAG, "Refreshed token: " + token);

    // If you want to send messages to this application instance or
    // manage this apps subscriptions on the server side, send the
    // Instance ID token to your app server.
    sendRegistrationToServer(token);
  }

  private void sendRegistrationToServer(String token) {
    Log.d(TAG, "sendRegistrationToServer");
    if (mCurrentUser != null) {
      mCurrentUserID = mCurrentUser.getUid();

      Map<String, Object> notificationData = new HashMap<>();
      notificationData
          .put(NotificationsCollectionAccessors.mNotificationsCollectionFieldToken, token);

      NotificationsCollectionAccessors.mNotificationsRef
          .document(mCurrentUserID)
          .set(notificationData)
          .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
              Log.d(TAG, "tokenAdded:success");
            }
          })
          .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              Log.w(TAG, "tokenAdded:failure", e);
            }
          });
    } else {
      Log.w(TAG, "sendRegistrationToServer: User not logged in");
    }
  }
}
