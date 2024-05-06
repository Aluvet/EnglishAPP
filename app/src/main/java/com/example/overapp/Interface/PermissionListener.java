package com.example.overapp.Interface;

import java.util.List;

public interface PermissionListener {
    //    授权
    void  onGranted();

    //未授权
    void onDenied(List<String> deniedPermission);
}
