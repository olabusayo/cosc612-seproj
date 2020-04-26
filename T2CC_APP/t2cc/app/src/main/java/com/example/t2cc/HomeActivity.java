package com.example.t2cc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.t2cc.FirestoreConnections.NotificationsCollectionAccessors;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends BaseActivity implements View.OnClickListener {
  private final static String TAG = "T2CC:Home";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    // prep buttons for onClick
    findViewById(R.id.browseClassesButton).setOnClickListener(this);
    findViewById(R.id.myClassButton).setOnClickListener(this);

    // get firebase token
    FirebaseInstanceId.getInstance().getInstanceId()
        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
          @Override
          public void onComplete(@NonNull Task<InstanceIdResult> task) {
            if (!task.isSuccessful()) {
              Log.w(TAG, "getInstanceId failed", task.getException());
            } else {
              // Get new Instance ID token
              String token = task.getResult().getToken();
              // Log and toast
              Log.d(TAG, "FirebaseToken:" + token);
              sendRegistrationToServer(token);
            }
          }
        });

  }

  @Override
  public void onClick(View v) {
    int i = v.getId();

    if (i == R.id.browseClassesButton) {
      changeToBrowseActivity();
    } else if (i == R.id.myClassButton) {
      changeToMyClassActivity();
    }

  }

  private void changeToMyClassActivity() {
    Intent intent = new Intent(HomeActivity.this, MyClassActivity.class);
    startActivity(intent);
  }

  private void changeToBrowseActivity() {
    Intent intent = new Intent(HomeActivity.this, BrowseClassesActivity.class);
    startActivity(intent);
  }

  private void sendRegistrationToServer(String token) {
    // TODO: Implement this method to send token to your app server.
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
