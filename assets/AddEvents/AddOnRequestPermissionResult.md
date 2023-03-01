# You need to add the following event to run project without breaking.
Open Sketchware pro.

Go to `developer console`.

Click on `Event manager`.

Click on `Activity events` at the top.

Click on add icon.

Fill the all fields by copy paste as follow or fill the fields as shown in image.

---
event name - `OnRequestPermissionResult`

description - `OnRequestPermissionResult`

parameters - `%d.requestCode %s.permissions %d.grantResults`

spec - `OnRequestPermussionResult %d.requestCode %s.permissions %d.grantResults`

event code - 
```java
@Override
public void onRequestPermissionsResult(int _requestCode, String[] _permissions, int[] _grantResults) {
%1$s
}
```

---
![onStartActivityManually](assets/OnRequestPermissionResult.png)