# Android runtime permission util
기존 Android SDK 버전에서는 설치 시 해당 앱에 설정 된 permission을 물어보고 동의를 해야만 설치가 가능했다. 하지만, Android 6.0(SDK 23)이상의 버전에서는 설치 시에 묻는게 아니라 해당 기능이 permission을 필요로 할 때, permission을 활성화 해줘야 사용가능하다. 따라서 기존 6.0미만의 버전에서 개발했던 어플리케이션은 6.0이상의 폰에서는 정상적으로 작동하지 않을 수 있다.

##설명
안드로이드 개발문서( http://developer.android.com/intl/ko/training/permissions/requesting.html )를 보면 자세히 나와 있지만, 좀 더 편리한 사용을 위해 Util을 만들어 사용한다.

##사용방법
- 카메라 기능 켜기. (퍼미션 허용 확인하기 전, Snackbar로 메세지 보여주기)
```java
    RuntimeUtil.checkPermission(getActivity(), getView(), Manifest.permission.CAMERA, RuntimeUtil.PERMISSION_CAMERA, null, new OnPermssionCallBackListener() {
            @Override
            public void OnGrantPermission() {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(intent, RESULT_CAMERA);
                }
            }
        });
```
```java
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RuntimeUtil.PERMISSION_CAMERA) {
            if (RuntimeUtil.verifyPermissions(getActivity(), grantResults)) {
                //카메라 기능 다시 실행
            }
        } 
    }
```
- 갤러리 켜기(해당 퍼미션 허용 거부 시 Snackbar 오픈 - 앱 설정 페이지로 이동)
```java
    RuntimeUtil.checkPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, RuntimeUtil.PERMISSION_ALBUM, new OnPermssionCallBackListener() {
            @Override
            public void OnGrantPermission() {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_ALBUM);
            }
        });
```
```java
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RuntimeUtil.PERMISSION_ALBUM) {
            if (RuntimeUtil.verifyPermissions(getActivity(), getView(), grantResults)) {
                //갤러리 켜기
            }
        }
    }
```

- rootview 설정 시 Snackbar바로 뭔하는 메세지를 노출 시킬 수 있음.

##반드시 설정 처리 해야 할 permission list
Permission Group  | Permissions
------------- | -------------
| WRITE_CALENDAR
CALENDAR  | READ_CALENDAR 
 
Content Cell  | Content Cell
