package com.example.covidcontacttracing;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.lang.reflect.Field;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private DatabaseHelper myDb;
    SharedPreferences pref;
    String uid, username, registerDate, email, gender, dob, tel, state;

    private TextView tvName, tvDate, tvEmail, tvGender, tvDob, tvTel, tvState;
    private CircleImageView profileImg;
    private ImageView profileCover;
    private Boolean shouldRefreshOnResume = false;

    private static final int PICK_IMAGE = 100;
    private static final int PICK_COVER = 200;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // To solve: SQLiteQuery: exception: Row too big to fit into CursorWindow requiredPos=0, totalRows=1
        // is when image too big
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        myDb = new DatabaseHelper(view.getContext());
        pref = this.getActivity().getSharedPreferences("user_details", 0);

        tvName = view.findViewById(R.id.tv_username);
        tvDate = view.findViewById(R.id.tv_registeredDate);
        tvEmail = view.findViewById(R.id.email);
        tvGender = view.findViewById(R.id.gender);
        tvDob = view.findViewById(R.id.dob);
        tvTel = view.findViewById(R.id.tel);
        tvState = view.findViewById(R.id.state);
        profileImg = view.findViewById(R.id.profile_image);
        profileCover = view.findViewById(R.id.profile_cover);

        //get data from shared preferences
        uid = pref.getString("uid", null);
        registerDate = "2021-01-15";    // default value if something wrong

        retrieveProfile();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    // options menu - link to other activity
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent;

        switch (item.getItemId()){
            case R.id.personal_info:
                intent = new Intent(getContext(), EditProfileInfoActivity.class);
                startActivity(intent);
                break;

            case R.id.change_pw:
                intent = new Intent(getContext(), ChangePasswordActivity.class);
                startActivity(intent);
                break;

            case R.id.edit_pic:
                checkPermissionPic();
                break;

            case R.id.edit_cover:
                checkPermissionCover();
                break;

            case R.id.logout:
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // load user profile details - read from database
    // if user haven't change their profile image and cover, will use default
    public void retrieveProfile() {
        Bitmap picture = myDb.getProfilePic(uid);
        if (picture == null)
            profileImg.setImageResource(R.drawable.profile_pic1);
        else
            profileImg.setImageBitmap(picture);

        Bitmap cover = myDb.getProfileCover(uid);
        if (cover == null)
            profileCover.setImageResource(R.drawable.profile_cover1);
        else
            profileCover.setImageBitmap(cover);

        //get User data based on user id store in session
        Cursor res = myDb.getUserData(uid);
        if(res.moveToNext()){
            username = res.getString(res.getColumnIndex("name"));
            registerDate = res.getString(res.getColumnIndex("registeredDate"));
            email = res.getString(res.getColumnIndex("email"));
            gender = res.getString(res.getColumnIndex("gender"));
            dob = res.getString(res.getColumnIndex("dob"));
            tel = res.getString(res.getColumnIndex("tel"));
            state = res.getString(res.getColumnIndex("state"));
        }

        // if user just registered and haven't update profile details:
        // the info part become red color text and when clicked, direct to edit profile page
        tvName.setText(username.toUpperCase());
        tvDate.setText("Since " + registerDate);
        tvEmail.setText(email);
        if(gender == null){
            tvGender.setText("Add Gender");
            tvGender.setTextColor(ContextCompat.getColor(requireActivity(), R.color.my_red));
            tvGender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), EditProfileInfoActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            tvGender.setClickable(false);
            tvGender.setTextColor(Color.BLACK);
            tvGender.setText(gender);
        }

        if(dob == null){
            tvDob.setText("Add Date of Birth");
            tvDob.setTextColor(ContextCompat.getColor(requireActivity(), R.color.my_red));
            tvDob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), EditProfileInfoActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            tvDob.setClickable(false);
            tvDob.setTextColor(Color.BLACK);
            tvDob.setText(dob);
        }

        if(tel == null){
            tvTel.setText("Add Contact Number");
            tvTel.setTextColor(ContextCompat.getColor(requireActivity(), R.color.my_red));
            tvTel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), EditProfileInfoActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            tvTel.setClickable(false);
            tvTel.setTextColor(Color.BLACK);
            tvTel.setText("+60 " + tel);
        }

        if(state == null){
            tvState.setText("Add Living Place");
            tvState.setTextColor(ContextCompat.getColor(requireActivity(), R.color.my_red));
            tvState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), EditProfileInfoActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            tvState.setClickable(false);
            tvState.setTextColor(Color.BLACK);
            tvState.setText(state);
        }
    }

    // ask for permission to access gallery if first time
    public void checkPermissionPic() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://media/internal/images/media"));
                startActivityForResult(intent, PICK_IMAGE);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getActivity(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(requireActivity())
                .setPermissionListener(permissionListener)
                .setDeniedMessage("If you reject permission, you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    // ask for permission to access gallery if first time
    public void checkPermissionCover() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://media/internal/images/media"));
                startActivityForResult(intent, PICK_COVER);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getActivity(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(requireActivity())
                .setPermissionListener(permissionListener)
                .setDeniedMessage("If you reject permission, you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    //Get location of images
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == getActivity().RESULT_OK && requestCode == PICK_IMAGE) {
            Uri uri = data.getData();
            String x = getPath(uri);

            if(myDb.updateProfilePic(x, uid)) {
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            }
            else {
                Toast.makeText(getActivity(), "Please enable the storage permission in your app setting.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(resultCode == getActivity().RESULT_OK && requestCode == PICK_COVER) {
            Uri uri = data.getData();
            String x = getPath(uri);

            if(myDb.updateProfileCover(x, uid)) {
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            }
            else {
                Toast.makeText(getActivity(), "Please enable the storage permission in your app setting.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getPath(Uri uri) {
        if(uri == null) return null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        if(cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    // set a boolean variable to indicate, later need to load again
    @Override
    public void onPause() {
        super.onPause();
        shouldRefreshOnResume = true;
    }

    // since after update profile in edit profile page will back pressed,
    // to ensure profile page display the latest update info, call retrieveProfile() again
    @Override
    public void onResume(){
        super.onResume();
        ((HomeActivity) getActivity()).setActionBarTitle("Profile");
        if (shouldRefreshOnResume) {
            retrieveProfile();
        }
    }
}
