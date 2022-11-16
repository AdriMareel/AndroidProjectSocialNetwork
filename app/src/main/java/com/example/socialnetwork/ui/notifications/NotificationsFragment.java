package com.example.socialnetwork.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.socialnetwork.NavigationActivity;
import com.example.socialnetwork.R;
import com.example.socialnetwork.databinding.FragmentNotificationsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    String username;
    ArrayList<String> followers = new ArrayList<>();
    ArrayList<String> following = new ArrayList<>();
    StorageReference profilePicLink;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    ImageView avatarIv,bgIv;
    TextView nameIv,nbFollowIv,descriptionIv;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //get data


        nameIv = root.findViewById(R.id.nameIv);
        avatarIv =  root.findViewById(R.id.avatarIv);
        nbFollowIv = root.findViewById(R.id.nbFollowersIv);
        descriptionIv = root.findViewById(R.id.descriptionIv);

        username = ((NavigationActivity)getActivity()).getUsername();
        System.out.println("                                       "+ username);

        db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println("*************** ICI ***************");
                                System.out.println(document.getId() + " => " + document.getData());
                                System.out.println("*************** ICI ***************");
                            }

                        }else {
                            System.out.println("*************************");
                            System.out.println(task.getException());
                            System.out.println("*************************");
                        }
                    }
                });



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}