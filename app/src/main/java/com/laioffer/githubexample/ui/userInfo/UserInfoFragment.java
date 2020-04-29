package com.laioffer.githubexample.ui.userInfo;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.laioffer.githubexample.base.BaseFragment;
import com.laioffer.githubexample.databinding.UserInfoFragmentBinding;
import com.laioffer.githubexample.remote.response.UserProfile;
import com.laioffer.githubexample.ui.HomeList.HomeListFragment;
import com.laioffer.githubexample.ui.NavigationManager;
import com.laioffer.githubexample.ui.editEdu.EditEduFragment;
import com.laioffer.githubexample.ui.editProfile.EditProfileFragment;
import com.laioffer.githubexample.ui.editWork.EditWorkFragment;
import com.laioffer.githubexample.util.Config;
import com.laioffer.githubexample.util.Utils;

import java.io.File;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class UserInfoFragment extends BaseFragment<UserInfoViewModel, UserInfoRepository> {
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static Uri tempUri;
    private static final int CROP_SMALL_PICTURE = 2;
    private ImageView iv_personal_icon;
    private UserInfoViewModel mViewModel;

    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }
    private UserInfoFragmentBinding binding;
    private NavigationManager navigationManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        navigationManager = (NavigationManager) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = UserInfoFragmentBinding.inflate(inflater, container, false);
        binding.cvProfile.lastName.setText(Config.lastName);
        binding.cvProfile.firstName.setText(Config.firstName);
        viewModel.getUserInfoMutableLiveData().observe(getViewLifecycleOwner(), it->{
            binding.cvProfile.firstName.setText(it.profile.FirstName);
            Config.firstName = it.profile.FirstName;
            binding.cvProfile.lastName.setText(it.profile.LastName);
            Config.lastName = it.profile.LastName;
            binding.cvProfile.address.setText(it.profile.Address);
            binding.cvProfile.eMail.setText(it.profile.Email);
            binding.cvProfile.dateOfBirth.setText(it.profile.DateOfBirth);
            binding.cvProfile.phoneNumber.setText(it.profile.PhoneNumber);
            binding.cvEducation.schoolName.setText(it.education.schoolName);
            binding.cvEducation.schoolStartDate.setText(it.education.startDate);
            binding.cvEducation.schoolEndDate.setText(it.education.endDate);
            binding.cvWork.companyName.setText(it.work.CompanyName);
            binding.cvWork.jobStartDate.setText(it.work.StartDate);
            binding.cvWork.jobTitle.setText(it.work.JobTitle);
            binding.cvWork.jobEndDate.setText(it.work.EndDate);
        });
        binding.name.setText(Config.userId);
        binding.editEducation.setOnClickListener(v -> {
            navigationManager.navigateTo(new EditEduFragment());
        });
        binding.editProfile.setOnClickListener(v -> {
            navigationManager.navigateTo(new EditProfileFragment());
        });
        binding.editWork.setOnClickListener(v -> {
            navigationManager.navigateTo(new EditWorkFragment());
        });
        binding.pimage.setOnClickListener(v->{
            showChoosePicDialog();
        });
        binding.saveProfile.setOnClickListener(v -> {
            navigationManager.navigateTo(new HomeListFragment());
        });
//        binding.cvProfile.address.setText(it.profile.Address);
//        binding.cvProfile.eMail.setText(it.profile.Email);
//        binding.cvProfile.dateOfBirth.setText(it.profile.DateOfBirth);
//        binding.cvProfile.phoneNumber.setText(it.profile.PhoneNumber);
//        binding.cvEducation.schoolName.setText(it.education.schoolName);
//        binding.cvEducation.schoolStartDate.setText(it.education.startDate);
//        binding.cvEducation.schoolEndDate.setText(it.education.endDate);
//        binding.cvWork.companyName.setText(it.work.CompanyName);
//        binding.cvWork.jobStartDate.setText(it.work.StartDate);
//        binding.cvWork.jobTitle.setText(it.work.JobTitle);
//        binding.cvWork.jobEndDate.setText(it.work.EndDate);
        return binding.getRoot();
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    @Override
    protected UserInfoViewModel getViewModel() {
        return new ViewModelProvider(requireActivity(), getFactory()).get(UserInfoViewModel.class);
    }

    @Override
    protected ViewModelProvider.Factory getFactory() {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new UserInfoViewModel(getRepository());
            }
        };
    }

    @Override
    protected UserInfoRepository getRepository() {
        return new UserInfoRepository();
    }

    private void showChoosePicDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle("Set Portrait");
        String[] items = { "Choose a Local photo", "Take Pictures" };
        builder.setNegativeButton("Cancel", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "image.jpg"));
                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     *
     * @param data
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = Utils.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
            iv_personal_icon.setImageBitmap(photo);
            uploadPic(photo);
        }
    }

    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了

        String imagePath = Utils.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), String
                .valueOf(System.currentTimeMillis()));
        Log.e("imagePath", imagePath+"");
        if(imagePath != null){
            // 拿着imagePath上传了
            // ...
        }
    }
}
