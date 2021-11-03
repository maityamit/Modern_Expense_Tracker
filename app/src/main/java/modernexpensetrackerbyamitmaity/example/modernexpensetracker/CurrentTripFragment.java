package modernexpensetrackerbyamitmaity.example.modernexpensetracker;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class CurrentTripFragment extends Fragment {

    private FirebaseAuth mAuth;
    private String currentUserID;
    private DatabaseReference UserRef,MainRef,RootRef;
    private ProgressDialog progressDialog;
    String fg = "0.0";
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_current_trip, container, false);

        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference().child("AllTrip");
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        MainRef = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setContentView(R.layout.loading);
        progressDialog.setTitle("Please Wait...");
        recyclerView = view.findViewById(R.id.ALLTripRecyclerView);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Tips: Please Check your Internet or Wi-fi Connection");


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));






        return  view;

    }




    @Override
    public void onStart() {

        super.onStart();


        progressDialog.show();
        FirebaseUser currentUser = mAuth.getCurrentUser ();

        if (currentUser == null)
        {

            SendUserToLoginActivity();
        }
        else
        {
            currentUserID = mAuth.getCurrentUser ().getUid ();
            progressDialog.show();




            DatabaseReference DODREF = FirebaseDatabase.getInstance ().getReference ().child("Users").child(currentUserID).child("MyTrip");



            progressDialog.show();


            DODREF.addValueEventListener ( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    int r  = (int)dataSnapshot.getChildrenCount();

                    if (r==0)
                    {
                        recyclerView.setVisibility(View.GONE);
                        progressDialog.dismiss();
                    }
                    else
                    {
                        progressDialog.show();

                        FirebaseRecyclerOptions<DODModel> optionsss =
                                new FirebaseRecyclerOptions.Builder<DODModel> ()
                                        .setQuery ( DODREF,DODModel.class )
                                        .build ();


                        FirebaseRecyclerAdapter<DODModel, StudentViewHolderX> adapterrr =
                                new FirebaseRecyclerAdapter<DODModel, StudentViewHolderX> (optionsss) {
                                    @Override
                                    protected void onBindViewHolder(@NonNull final StudentViewHolderX holder, final int position, @NonNull final DODModel model) {

                                        progressDialog.show();








                                        String st = model.getID();

                                        MainRef.child ( "AllTrip" ).child ( st )
                                                .addValueEventListener ( new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {




                                                        String stringkey = getRef(position).getKey().toString();
                                                        String retrieveUserNAme = dataSnapshot.child ( "TripName" ).getValue ().toString ();
                                                        String retrieveEmail = dataSnapshot.child ( "TripDate" ).getValue ().toString ();
                                                        String stringperson = String.valueOf((int)dataSnapshot.child("Member").getChildrenCount());

                                                        holder.name.setText(retrieveUserNAme);
                                                        holder.date.setText(retrieveEmail);
                                                        holder.person.setText(stringperson);




                                                        progressDialog.dismiss();

                                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Intent intenti = new Intent(getContext(),TripviewActivity.class);
                                                                intenti.putExtra("TRIPID",stringkey);
                                                                startActivity(intenti);
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        progressDialog.dismiss();
                                                        Toast.makeText(getContext(), "Error !", Toast.LENGTH_SHORT).show();
                                                    }
                                                } );



                                    }

                                    @NonNull
                                    @Override
                                    public StudentViewHolderX onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                                        View view  = LayoutInflater.from ( viewGroup.getContext () ).inflate ( R.layout.trip_view_layout,viewGroup,false );
                                        StudentViewHolderX viewHolder  = new StudentViewHolderX (  view);
                                        return viewHolder;

                                    }

                                    @Override
                                    public int getItemCount() {


                                        return super.getItemCount();
                                    }



                                };
                        recyclerView.setAdapter ( adapterrr );

                        adapterrr.startListening ();




                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Error !", Toast.LENGTH_SHORT).show();
                }
            } );












        }
    }



    private void SendUserToLoginActivity() {


        Intent loginIntent = new Intent ( getContext(),LoginActivity.class );
        loginIntent.addFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity ( loginIntent );
        getActivity().finish ();
    }





    public static class StudentViewHolderX extends  RecyclerView.ViewHolder
    {

        TextView name,date,person;
        public StudentViewHolderX(@NonNull View itemView) {
            super ( itemView );
            name = itemView.findViewById ( R.id.trip_view_trip_name);
            date = itemView.findViewById ( R.id.trip_view_trip_date);
            person = itemView.findViewById ( R.id.trip_view_trip_person);

        }
    }


}