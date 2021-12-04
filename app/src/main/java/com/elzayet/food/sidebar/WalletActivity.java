package com.elzayet.food.sidebar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elzayet.food.ArchiveModel;
import com.elzayet.food.PointsModel;
import com.elzayet.food.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

public class WalletActivity extends AppCompatActivity {
    private final DatabaseReference USERS_DB = FirebaseDatabase.getInstance().getReference("USERS");

    private ImageView a_w_qrImage,a_w_facebook,a_w_instagram,a_w_twitter;
    private TextView a_w_userPoints,a_w_refellarLink;
    private RecyclerView a_w_waletHistory;
    //User Account
    private String phoneNumber,userRefellar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        //user Account
        SharedPreferences pref = getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        phoneNumber            = pref.getString("phoneNumber", "NOTHING");
        userRefellar           = pref.getString("userRefellar", "NOTHING");

        a_w_qrImage      = findViewById(R.id.a_w_qrImage);
        a_w_facebook     = findViewById(R.id.a_w_facebook);
        a_w_instagram    = findViewById(R.id.a_w_instagram);
        a_w_twitter      = findViewById(R.id.a_w_twitter);
        a_w_userPoints   = findViewById(R.id.a_w_userPoints);
        a_w_refellarLink = findViewById(R.id.a_w_refellarLink);
        a_w_waletHistory = findViewById(R.id.a_w_waletHistory);
        a_w_waletHistory.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        a_w_waletHistory.setHasFixedSize(true);
        a_w_refellarLink.setText(userRefellar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        USERS_DB.child("WALLETS").child(phoneNumber)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        PointsModel pointsModel = snapshot.getValue(PointsModel.class);
                        String points           = pointsModel.getPoints();
                        int x = Integer.parseInt(points);
                        a_w_userPoints.setText(Integer.toString(x / 100) + " جنيه ");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show(); }
                });

        showHistory();
        createAcountQR();
    }

    private void createAcountQR() {
        try {
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            com.google.zxing.Writer codeWriter;
            codeWriter = new Code128Writer();
            BitMatrix byteMatrix = codeWriter.encode(phoneNumber, BarcodeFormat.CODE_128,400, 200, hintMap);
            int width = byteMatrix.getWidth();
            int height = byteMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {    bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);    }
            }
            a_w_qrImage.setImageBitmap(bitmap);
        } catch (Exception e) {    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();    }

    }


    private void showHistory() {
        FirebaseRecyclerOptions<ArchiveModel> options =
                new FirebaseRecyclerOptions.Builder<ArchiveModel>().setQuery(USERS_DB.child("HISTORIES").child(phoneNumber) , ArchiveModel.class).setLifecycleOwner( this).build();
        FirebaseRecyclerAdapter<ArchiveModel, HistoryAdapter> adapter =
                new FirebaseRecyclerAdapter<ArchiveModel, HistoryAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull HistoryAdapter holder, int position, @NonNull ArchiveModel model) {
                        String id   = model.getId();
                        String date = model.getDate();
                        String time = model.getTime();
                        String msg  = model.getMsg();
                        holder.showHistory(msg,date,time);
                        holder.itemView.setOnClickListener(v -> {
                            if(id.equals("1")){ Toast.makeText(getBaseContext(), id, Toast.LENGTH_SHORT).show(); }
                            else{Toast.makeText(getBaseContext(), "user Adapter", Toast.LENGTH_SHORT).show(); }
                        });
                    }
                    @NonNull
                    @Override
                    public HistoryAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        return new HistoryAdapter(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_history_item, parent, false));
                    }
                };
        a_w_waletHistory.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.startListening();
    }

    private static class HistoryAdapter extends RecyclerView.ViewHolder {

        public HistoryAdapter(@NonNull View itemView) {
            super(itemView);
        }

        public void showHistory(String historyDescription,String date,String time) {
            TextView c_h_i_historyDateTime   = itemView.findViewById(R.id.c_h_i_historyDateTime);
            TextView c_h_i_historyDescription= itemView.findViewById(R.id.c_h_i_historyDescription);
            c_h_i_historyDateTime.setText(date+"\n"+time);
            c_h_i_historyDescription.setText(historyDescription);
        }
    }


}