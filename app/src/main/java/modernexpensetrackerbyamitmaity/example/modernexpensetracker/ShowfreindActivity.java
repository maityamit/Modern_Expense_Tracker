package modernexpensetrackerbyamitmaity.example.modernexpensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowfreindActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference FreindRef,TripRef;
    private TextView searchView;
    private String Trip_Key;
    private FirebaseAuth mAuth;
    private  String currentUserID;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showfreind);



        Intent intent = getIntent();
        Trip_Key= intent.getExtras().getString("TRIPANDAND");
        mAuth= FirebaseAuth.getInstance ();
        currentUserID = mAuth.getCurrentUser ().getUid ();

        FreindRef =  FirebaseDatabase.getInstance ().getReference ().child("Users");
        TripRef =  FirebaseDatabase.getInstance ().getReference ().child("AllTrip");
        progressDialog = new ProgressDialog( ShowfreindActivity.this);
        progressDialog.setContentView ( R.layout.loading );
        searchView = findViewById(R.id.Freind_Serach_Actvity);
        progressDialog.setTitle ( "Please Wait..." );
        progressDialog.setCanceledOnTouchOutside ( false );
        progressDialog.setMessage ( "Tips: Please Check your Internet or Wi-fi Connection" );
        recyclerView = findViewById(R.id.Show_All_Freinds_Recycler_View);
        recyclerView.setLayoutManager(new LinearLayoutManager(ShowfreindActivity.this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        progressDialog.show();

        FirebaseRecyclerOptions<FreindModel> optionssst =
                new FirebaseRecyclerOptions.Builder<FreindModel> ()
                        .setQuery ( FreindRef,FreindModel.class )
                        .build ();


        FirebaseRecyclerAdapter<FreindModel, StudentViewHolderY> adapterrrt =
                new FirebaseRecyclerAdapter<FreindModel, StudentViewHolderY> (optionssst) {
                    @Override
                    protected void onBindViewHolder(@NonNull final StudentViewHolderY holder, final int position, @NonNull final FreindModel model) {
                        String string_user_key =  getRef(position).getKey().toString();

                        if (string_user_key.equals(currentUserID))
                        {
                            holder.itemView.setVisibility(View.GONE);
                        }
                        else {

                            holder.name.setText(model.getName());
                            Picasso.get ().load ( model.getImage() ).placeholder ( R.drawable.profile_image ).error ( R.drawable.profile_image ).into ( holder.circleImageView );

                            holder.imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TripKeyAddToUser(string_user_key);
                                    UserAddToTrip(string_user_key);
                                }
                            });
                        }


                        progressDialog.dismiss();

                    }

                    @NonNull
                    @Override
                    public StudentViewHolderY onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view  = LayoutInflater.from ( viewGroup.getContext () ).inflate ( R.layout.freind_show_layout,viewGroup,false );
                        ShowfreindActivity.StudentViewHolderY viewHolder  = new StudentViewHolderY(  view);
                        return viewHolder;

                    }



                };
        recyclerView.setAdapter ( adapterrrt );

        adapterrrt.startListening ();






    }

    private void UserAddToTrip(String user_key) {


        progressDialog.show();
        FreindRef.addValueEventListener ( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String retrieveUserNAme1 = dataSnapshot.child(user_key).child ( "Name" ).getValue ().toString ();
                        String retrieveUserNAme2 = dataSnapshot.child(currentUserID).child ( "Name" ).getValue ().toString ();

                        String chat_key = TripRef.child(Trip_Key).child("Chat").push().getKey();
                        HashMap<String,Object> onlineStatt = new HashMap<> (  );
                        onlineStatt.put ( Trip_Key+"/Member/"+user_key+"/Person",user_key);
                        onlineStatt.put ( Trip_Key+"/Chat/"+chat_key+"/Chat",retrieveUserNAme1 +"\nwas added by "+retrieveUserNAme2+" .");
                        TripRef.updateChildren(onlineStatt);

                     progressDialog.dismiss();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        progressDialog.dismiss();
                        Toast.makeText(ShowfreindActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                    }
                } );





    }

    private void TripKeyAddToUser(String user_key) {

        HashMap<String,Object> onlineStatt = new HashMap<> (  );
        onlineStatt.put(user_key+"/MyTrip/"+Trip_Key+"/ID",Trip_Key);
        FreindRef.updateChildren(onlineStatt);
        Toast.makeText(this, "Ok , Done", Toast.LENGTH_SHORT).show();


    }

    public static class StudentViewHolderY extends  RecyclerView.ViewHolder
    {

        TextView name;
        CircleImageView circleImageView;
        ImageView imageView;
        public StudentViewHolderY(@NonNull View itemView) {
            super ( itemView );
            name = itemView.findViewById ( R.id.Freinds_Show_Profile_Name);
            circleImageView = itemView.findViewById ( R.id.Freinds_Show_Profile_Image);
            imageView = itemView.findViewById ( R.id.tick_box);



        }
    }
}