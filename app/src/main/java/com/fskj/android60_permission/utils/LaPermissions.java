package com.fskj.android60_permission.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;


import com.fskj.android60_permission.R;

import java.util.ArrayList;
import java.util.List;
/**
 * author: Administrator
 * date: 2018/1/26 0026
 * desc:
 */

public class LaPermissions {
    private static final String TAG = "LaPermissions";
    private String rationaleMessage = "shouldShowRationale should open those permission:";
    private static final int INA_REQUESTCODE = 9;
    public static final int CODE_MICROPHONE = 0;//group.MICROPHONE
    public static final int CODE_GET_CONTACTS = 1;//group.CONTACTS
    public static final int CODE_READ_PHONE_STATE = 2;//group.PHONE
    public static final int CODE_READ_CALENDAR = 3;//group.CALENDAR
    public static final int CODE_CAMERA = 4;//group.CAMERA
    public static final int CODE_SENSORS = 5;//group.SENSORS
    public static final int CODE_ACCESS_COARSE_LOCATION = 6;//group.LOCATION
    public static final int CODE_READ_EXTERNAL_STORAGE = 7;//group.STORAGE
    public static final int CODE_READ_SMS = 8;//group.SMS

    /*group权限只需要申请一个，则会对应申请一组的权限*/
    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;//3
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;//7
    public static final String PERMISSION_READ_CALENDAR = Manifest.permission.READ_CALENDAR;//2
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static final String PERMISSION_BODY_SENSORS = Manifest.permission.BODY_SENSORS;
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;//2
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;//2
    public static final String PERMISSION_READ_SMS = Manifest.permission.READ_SMS;//6

    public static final String[] requestPermissions = {
            PERMISSION_RECORD_AUDIO,
            PERMISSION_GET_ACCOUNTS,
            PERMISSION_READ_PHONE_STATE,
            PERMISSION_READ_CALENDAR,
            PERMISSION_CAMERA,
            PERMISSION_BODY_SENSORS,
            PERMISSION_ACCESS_COARSE_LOCATION,
            PERMISSION_READ_EXTERNAL_STORAGE,
            PERMISSION_READ_SMS
    };

    public static final String[] groupContactsPermissons = {
            Manifest.permission.WRITE_CONTACTS,
            PERMISSION_GET_ACCOUNTS,
            Manifest.permission.READ_CONTACTS
    };
    public static final String[] groupPhonePermissons = {
            Manifest.permission.READ_CALL_LOG,
            PERMISSION_READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.USE_SIP,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.ADD_VOICEMAIL
    };
    public static final String[] groupCalendarPermissons = {
            Manifest.permission.WRITE_CALENDAR,
            PERMISSION_READ_CALENDAR,
    };
    public static final String[] groupLocationPermissons = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            PERMISSION_ACCESS_COARSE_LOCATION,
    };
    public static final String[] groupStoragePermissons = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            PERMISSION_READ_EXTERNAL_STORAGE,
    };
    public static final String[] groupSmsPermissons = {
            Manifest.permission.RECEIVE_WAP_PUSH,
            PERMISSION_READ_SMS,
            Manifest.permission.RECEIVE_MMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.SEND_SMS
    };


    private Context mContext;
    private Activity mActivity;
    private PermissionGrant permissionGrant;


    public interface PermissionGrant {
        void onPermissionGranted(String... grantedPermissions);

        void onPermissionDenied(String... deniedPermissions);

        void onPermissionExist();
    }

    public LaPermissions(@NonNull Activity activity) {
        mContext = activity.getApplicationContext();
        mActivity = activity;
    }

    /**
     * 请求单个权限，不申请单个权限组类的权限，单个申请不必显示allRationale
     *
     * @param permissionCode  permissionCode code.权限码
     * @param permissionGrant permissionGrant.接口
     * @param shouldRationale shouldRationale.是否显示权限申请说明
     */
    public void requestPermission(final int permissionCode, boolean shouldRationale,
                                  @NonNull PermissionGrant permissionGrant) {
        if (mActivity == null) {
            return;
        }
        if (permissionCode < 0 || permissionCode > requestPermissions.length) {
            Log.w(TAG, "requestPermission illegal ,the" + permissionCode + "requestCode requestCode must larger number of 9");
            return;
        }

        final int[] permissionCodes = new int[]{permissionCode};
        doRequestPermissions(permissionCodes, shouldRationale, false, false, permissionGrant);
    }

    /**
     * 请求单个权限，申请单个权限组类的权限交给参数决定,单个申请不必显示allRationale
     *
     * @param permissionCode     permissionCode.权限码
     * @param permissionGrant    permissionGrant.接口回掉
     * @param shouldRequestGroup shouldRequestGroup是否申请单个权限包含组内所有权限.
     * @param shouldRationale    shouldRationale.
     */
    public void requestPermission(final int permissionCode, boolean shouldRationale, boolean shouldRequestGroup,
                                  @NonNull PermissionGrant permissionGrant) {
        if (mActivity == null) {
            return;
        }
        if (permissionCode < 0 || permissionCode > requestPermissions.length) {
            Log.w(TAG, "requestPermission illegal ,the" + permissionCode + "requestCode requestCode must larger number of 9");
            return;
        }

        final int[] permissionCodes = new int[]{permissionCode};
        doRequestPermissions(permissionCodes, shouldRationale, false, shouldRequestGroup, permissionGrant);
    }

    /**
     * 申请最高权限
     * 一次申请9组权限,all目前9组，如果已经申请则不去申请
     * 申请最高权限默认不显示所有的权限申请说明
     * 申请最高权限默认不申请组内权限
     */
    public void requestAllPermissions(boolean shouldRationale, @NonNull PermissionGrant permissionGrant) {
        int[] permissionCodes = new int[]{
                CODE_MICROPHONE,
                CODE_GET_CONTACTS,
                CODE_READ_PHONE_STATE,
                CODE_READ_CALENDAR,
                CODE_CAMERA,
                CODE_SENSORS,
                CODE_ACCESS_COARSE_LOCATION,
                CODE_READ_EXTERNAL_STORAGE,
                CODE_READ_SMS};
        doRequestPermissions(permissionCodes, shouldRationale, false, false, permissionGrant);
    }

    /**
     * 请求多个权限，不申请对应组内的权限，不显示所有权限申请说明。
     *
     * @param permissionCodes permissionCode.权限码
     * @param shouldRationale shouldRationale.是否给出权限申请说明
     * @param permissionGrant permissionGrant.接口回掉
     */
    public void requestPermissions(@NonNull int[] permissionCodes, boolean shouldRationale,
                                   @NonNull PermissionGrant permissionGrant) {
        doRequestPermissions(permissionCodes, shouldRationale, false, false, permissionGrant);
    }

    /**
     * 请求多个权限，不申请对应组内的权限，是否显示所有权限申请说明。
     *
     * @param permissionCodes  permissionCode.权限码
     * @param shouldRationale  shouldRationale.是否给出权限申请说明
     * @param permissionGrant  permissionGrant.接口回掉
     * @param showAllRationale showAllRationale.是否给出所有权限申请说明
     */

    public void requestPermissions(@NonNull int[] permissionCodes, boolean shouldRationale,
                                   boolean showAllRationale,
                                   @NonNull PermissionGrant permissionGrant) {
        doRequestPermissions(permissionCodes, shouldRationale, showAllRationale, false, permissionGrant);
    }

    /**
     * 请求多个权限，不申请对应组内的权限，
     *
     * @param permissionCodes    permissionCode.权限码
     * @param shouldRationale    shouldRationale.是否给出权限申请说明
     * @param permissionGrant    permissionGrant.接口回掉
     * @param showAllRationale   showAllRationale.是否给出所有权限申请说明
     * @param shouldRequestGroup shouldRequestGroup.是否申请组类所有权限
     */
    public void requestPermissions(@NonNull int[] permissionCodes, boolean shouldRationale,
                                   boolean showAllRationale, boolean shouldRequestGroup,
                                   @NonNull PermissionGrant permissionGrant) {
        doRequestPermissions(permissionCodes, shouldRationale, showAllRationale, shouldRequestGroup, permissionGrant);
    }

    /**
     * 执行申请权限
     */
    private void doRequestPermissions(@NonNull int[] permissionCodes, boolean shouldRationale,
                                      boolean showAllRationale, boolean shouldRequestGroup,
                                      @NonNull PermissionGrant permissionGrant) {
        if (mActivity == null) {
            return;
        }
        final List<String> requestPermissionsLists = new ArrayList<>();

        for (int i = 0; i < permissionCodes.length; i++) {
            int permissionCode = permissionCodes[i];
            Log.i(TAG, "requestPermission requestCodes:" + permissionCode);
            if (permissionCode < 0 || permissionCode > requestPermissions.length) {
                Log.w(TAG, "requestPermission illegal ,the" + permissionCode + "permissionCode must larger number of 9");
                return;
            }
            requestPermissionsLists.add(requestPermissions[permissionCode]);
        }

        String[] requestPermissions = requestPermissionsLists.toArray(new String[requestPermissionsLists.size()]);

        this.permissionGrant = permissionGrant;

        //获取没有授权的，且被用户拒绝过的权限列表，给出权限申请说明.
        final List<String> permissionsList = getNoGrantedPermission(requestPermissions, false);
        //获取没有授权的权限列表
        final List<String> shouldRationalePermissionsList = getNoGrantedPermission(requestPermissions, true);

        if (permissionsList == null || shouldRationalePermissionsList == null) {
            return;
        }

        if (permissionsList.size() == 0 && shouldRationalePermissionsList.size() == 0) {
            permissionGrant.onPermissionExist();
        }

        if (permissionsList.size() > 0) {
            Log.d(TAG, "showMessageOKCancel permissionsList");
            if (shouldRequestGroup) {
                final List<String> permissionsGroup = getGroupPermission(permissionsList);
                requestPermissions(INA_REQUESTCODE,
                        permissionsGroup.toArray(new String[permissionsGroup.size()]));
            } else {
                requestPermissions(INA_REQUESTCODE,
                        permissionsList.toArray(new String[permissionsList.size()]));
            }
        }

        if (shouldRationalePermissionsList.size() > 0) {
            Log.d(TAG, "showMessageOKCancel shouldRationalePermissionsList");
            if (shouldRequestGroup) {
                final List<String> permissionsGroup = getGroupPermission(shouldRationalePermissionsList);
                if (shouldRationale) {
                    shouldShowRationale(INA_REQUESTCODE,
                            showAllRationale,
                            permissionCodes,
                            permissionsGroup.toArray(new String[permissionsGroup.size()]));
                } else {
                    requestPermissions(INA_REQUESTCODE,
                            permissionsGroup.toArray(new String[permissionsGroup.size()]));
                }
            } else {
                if (shouldRationale) {
                    shouldShowRationale(INA_REQUESTCODE,
                            showAllRationale,
                            permissionCodes,
                            shouldRationalePermissionsList.toArray(new String[shouldRationalePermissionsList.size()]));
                } else {
                    requestPermissions(INA_REQUESTCODE,
                            shouldRationalePermissionsList.toArray(new String[shouldRationalePermissionsList.size()]));
                }
            }
        }

    }

    /**
     * requestPermissionsResult
     * 申请一组权限返回的结果，
     *
     * @param requestCode  请求的权限码
     * @param permissions  请求的权限数组
     * @param grantResults 权限授予状态集合
     *                     使用参考：LaPermissions.requestPermissionsResult(requestCode, permissions, grantResults);
     */
    public void requestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissionGrant == null) {
            return;
        }
        Log.i(TAG, "requestPermissionsResult requestCode:" + requestCode);
        if (requestCode < 0 || requestCode > requestPermissions.length) {
            Log.w(TAG, "requestPermission illegal ,the" + requestCode + "requestCode requestCode must larger number of 9");
            return;
        }

        List<String> grantPermissions = new ArrayList<>();

        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                grantPermissions.add(permissions[i]);
            }
        }

        if (deniedPermissions.size() > 0) {
            permissionGrant.onPermissionDenied(deniedPermissions.toArray(new String[deniedPermissions.size()]));
        } else {
            permissionGrant.onPermissionGranted(grantPermissions.toArray(new String[grantPermissions.size()]));
        }
    }

    /**
     * requestPermissionsResult
     * 如果权限拒绝，是否打开系统权限设置权限，如果打开则不回掉接口。
     *
     * @param requestCode  请求的权限码
     * @param permissions  请求的权限数组
     * @param grantResults 权限授予状态集合
     * @param openSettings 是否打开系统设置授予权限
     *                     使用参考：LaPermissions.requestPermissionsResult(requestCode, permissions, grantResults,true);
     */
    public void requestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, boolean openSettings) {
        if (permissionGrant == null) {
            return;
        }
        Log.i(TAG, "requestPermissionsResult requestCode:" + requestCode);
        if (requestCode < 0 || requestCode > requestPermissions.length) {
            Log.w(TAG, "requestPermission illegal ,the" + requestCode + "requestCode requestCode must larger number of 9");
            return;
        }

        List<String> grantPermissions = new ArrayList<>();

        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                grantPermissions.add(permissions[i]);
            }
        }

        if (deniedPermissions.size() == PackageManager.PERMISSION_GRANTED) {
            permissionGrant.onPermissionGranted(grantPermissions.toArray(new String[grantPermissions.size()]));
            Toast.makeText(mActivity, "all permission success" + deniedPermissions, Toast.LENGTH_SHORT).show();
        } else {
            if (openSettings) {
//                openSettingActivity("those permission need granted!");
                String[] permissionsHint = mActivity.getResources().getStringArray(R.array.permissions);
                openSettingActivity("Result" + permissionsHint[requestCode]);
            } else {
                permissionGrant.onPermissionDenied(deniedPermissions.toArray(new String[deniedPermissions.size()]));
            }

        }
    }


    private void shouldShowRationale(final int requestCode, boolean showAllRationale, int[] permissionCodes, final String[] requestPermissions) {
        if (showAllRationale) {
            String[] permissionsHint = mActivity.getResources().getStringArray(R.array.permissions);

            for (int i = 0; i < permissionCodes.length; i++) {
                int permissionCode = permissionCodes[i];
                Log.i(TAG, "shouldShowRationale permissionCode:" + permissionCodes[i]);
                showMessageOKCancel("Rationale: " + permissionsHint[permissionCode], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(requestCode, requestPermissions);
                    }
                });
            }
        } else {
            Log.i(TAG, "shouldShowRationale should open those permission:");
            showMessageOKCancel("Rationale: " + rationaleMessage, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requestPermissions(requestCode, requestPermissions);
                }
            });
        }
    }

    public void setRationalMessage(String rationaleMessage){
        this.rationaleMessage = rationaleMessage;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(mActivity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();

    }

    private void openSettingActivity(String message) {

        showMessageOKCancel(message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Log.d(TAG, "getPackageName(): " + mActivity.getPackageName());
                Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
                intent.setData(uri);
                mActivity.startActivity(intent);
            }
        });
    }

    /**
     * 得到组的权限
     */
    private List<String> getGroupPermission(List<String> permissionsList) {
        List<String> permissionsGroup = new ArrayList<>();
        for (String permissions : permissionsList) {
            if (permissions.equals(PERMISSION_GET_ACCOUNTS)) {
                for (String group : groupContactsPermissons) {
                    permissionsGroup.add(group);
                }
            }
            if (permissions.equals(PERMISSION_READ_PHONE_STATE)) {
                for (String group : groupPhonePermissons) {
                    permissionsGroup.add(group);
                }
            }
            if (permissions.equals(PERMISSION_READ_CALENDAR)) {
                for (String group : groupCalendarPermissons) {
                    permissionsGroup.add(group);
                }
            }
            if (permissions.equals(PERMISSION_ACCESS_COARSE_LOCATION)) {
                for (String group : groupLocationPermissons) {
                    permissionsGroup.add(group);
                }
            }
            if (permissions.equals(PERMISSION_READ_EXTERNAL_STORAGE)) {
                for (String group : groupStoragePermissons) {
                    permissionsGroup.add(group);
                }
            }
            if (permissions.equals(PERMISSION_READ_SMS)) {
                for (String group : groupSmsPermissons) {
                    permissionsGroup.add(group);
                }
            }
        }
        return permissionsGroup;
    }


    /**
     * 获取没有授权的权限列表集合
     * true: return no granted and shouldShowRequestPermissionRationale permissions,
     * false:return no granted and !shouldShowRequestPermissionRationale
     *
     * @param requestPermissions 请求的权限集合
     * @return permissions 权限集合
     */
    private ArrayList<String> getNoGrantedPermission(String[] requestPermissions, boolean shouldRationale) {
        ArrayList<String> permissions = new ArrayList<>();

        for (int i = 0; i < requestPermissions.length; i++) {
            String requestPermission = requestPermissions[i];
            if (lacksPermissions(requestPermission)) {
                Log.i(TAG, "getNoGrantedPermission ActivityCompat.checkSelfPermission != PackageManager.PERMISSION_GRANTED:" + requestPermission);

                if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, requestPermission)) {
                    Log.d(TAG, "shouldShowRequestPermissionRationale if");
                    if (shouldRationale) {
                        permissions.add(requestPermission);
                    }
                } else {
                    if (!shouldRationale) {
                        permissions.add(requestPermission);
                    }
                    Log.d(TAG, "shouldShowRequestPermissionRationale else");
                }
            }
        }

        return permissions;
    }

    // 判断是否缺少权限集合

    public boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_DENIED) {
                return true;
            }
        }
        return false;
    }

    //申请权限
    private void requestPermissions(int requestCode, String... permissions) {
        ActivityCompat.requestPermissions(mActivity, permissions, requestCode);
    }
}
